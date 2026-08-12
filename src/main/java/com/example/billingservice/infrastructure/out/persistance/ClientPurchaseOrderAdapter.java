package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.AuditEventPublisherPort;
import com.example.billingservice.application.ports.out.ClientPurchaseOrderPort;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.domain.enums.PurchaseOrderType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.messaging.AuditEvent;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderPageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderPartnerSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderSummaryDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.ClientInvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.ClientPurchaseOrderEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PurchaseOrderMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.ClientPurchaseOrderRepository;
import com.example.billingservice.shared.PurchaseOrderAuditEventFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ClientPurchaseOrderAdapter implements ClientPurchaseOrderPort {
    private final ClientPurchaseOrderRepository clientPurchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderAuditEventFactory purchaseOrderAuditEventFactory;
    private final AuditEventPublisherPort auditEventPublisherPort;

    @Override
    public Page<PurchaseOrderPageItemDTO> findAllPurchaseOrders(String keyword, PurchaseOrderStatus status, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("issueDate").descending());
        Page<ClientPurchaseOrderEntity> entities = clientPurchaseOrderRepository.getPurchaseOrders(keyword, status,pageRequest);

        List<PurchaseOrderPageItemDTO> purchaseOrders = entities.getContent()
                .stream()
                .map(entity -> purchaseOrderMapper.toDomain(entity, PurchaseOrderType.SALE))
                .map(purchaseOrderMapper::domainToPageItem)
                .collect(Collectors.toList());
        return new PageImpl<>(purchaseOrders, pageRequest, entities.getTotalElements());
    }

    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        ClientPurchaseOrderEntity entity = (ClientPurchaseOrderEntity) purchaseOrderMapper.toEntity(purchaseOrder);
        ClientPurchaseOrderEntity savedEntity = clientPurchaseOrderRepository.save(entity);
        PurchaseOrder savedPurchaseOrder = purchaseOrderMapper.toDomain(savedEntity, purchaseOrder.getPurchaseOrderType());
        return  savedPurchaseOrder;
    }

    @Override
    public PurchaseOrderDTO getById(UUID idPurchaseOrder) {
        ClientPurchaseOrderEntity purchaseOrderEntity = clientPurchaseOrderRepository.getReferenceById(idPurchaseOrder);
        PurchaseOrder purchaseOrder =  purchaseOrderMapper.toDomain(purchaseOrderEntity,PurchaseOrderType.SALE);
        return purchaseOrderMapper.domainToPurchaseOrderDTO(purchaseOrder);
    }

    @Override
    public PurchaseOrder getDomainePurchaseOrderById(UUID idPurchaseOrder) {
        ClientPurchaseOrderEntity purchaseOrderEntity = clientPurchaseOrderRepository.getReferenceById(idPurchaseOrder);
        return purchaseOrderMapper.toDomain(purchaseOrderEntity,PurchaseOrderType.SALE);
    }

    @Override
    public PurchaseOrderDTO update(PurchaseOrder purchaseOrder) {
        ClientPurchaseOrderEntity entity = (ClientPurchaseOrderEntity) purchaseOrderMapper.toEntity(purchaseOrder);
        entity.setIdPurchaseOrder(purchaseOrder.getIdPurchaseOrder());
        PurchaseOrderEntity savedEntity = clientPurchaseOrderRepository.save(entity);
        PurchaseOrder purchaseOrder1 = purchaseOrderMapper.toDomain(savedEntity,purchaseOrder.getPurchaseOrderType());
        return  purchaseOrderMapper.domainToPurchaseOrderDTO(purchaseOrder1);
    }

    @Override
    public PurchaseOrderDTO updateStatus(UUID purchaseOrderId, PurchaseOrderStatus newStatus) {
        ClientPurchaseOrderEntity entity = clientPurchaseOrderRepository.getReferenceById(purchaseOrderId);

        AuditEvent auditEvent = purchaseOrderAuditEventFactory.purchaseOrderStatusChanged(
                entity.getIdPurchaseOrder(),
                String.valueOf(entity.getIdPurchaseOrder()),
                entity.getPurchaseOrderStatus().name(),
                newStatus.name(),
                null
        );

        entity.setPurchaseOrderStatus(newStatus);

        PurchaseOrder purchaseOrder = purchaseOrderMapper.toDomain(clientPurchaseOrderRepository.save(entity), PurchaseOrderType.SALE);

        auditEventPublisherPort.publish(auditEvent);

        return  purchaseOrderMapper.domainToPurchaseOrderDTO(purchaseOrder);
    }

    @Override
    public void delete(UUID idPurchaseOrder) {
        ClientPurchaseOrderEntity purchaseOrderEntity = clientPurchaseOrderRepository.getReferenceById(idPurchaseOrder);

        clientPurchaseOrderRepository.delete(purchaseOrderEntity);

        AuditEvent auditEvent = purchaseOrderAuditEventFactory.purchaseOrderDeleted(
                purchaseOrderEntity.getIdPurchaseOrder(),
                String.valueOf(purchaseOrderEntity.getIdPurchaseOrder()),
                Map.of("Purchase order number", purchaseOrderEntity.getReference()),
                null
        );

        auditEventPublisherPort.publish(auditEvent);
    }

    @Override
    public boolean existsByPurchaseOrderNumber(String purchaseOrderNumber) {
        return clientPurchaseOrderRepository.existsByReference(purchaseOrderNumber);
    }

    @Override
    public boolean existsByPurchaseOrderId(UUID purchaseOrderId) {
        return clientPurchaseOrderRepository.existsByIdPurchaseOrder(purchaseOrderId);
    }

    @Override
    public List<PurchaseOrderSummaryDTO> getPurchaseOrderSummary() {
        List<ClientPurchaseOrderEntity>  purchaseOrderEntities= clientPurchaseOrderRepository.getPurchaseOrdersByStatus(List.of(PurchaseOrderStatus.DRAFT, PurchaseOrderStatus.IN_DELIVERY,PurchaseOrderStatus.PARTIALLY_INVOICED));
        return purchaseOrderEntities.stream()
                .map(entity->purchaseOrderMapper.toDomain(entity,PurchaseOrderType.PURCHASE))
                .map(purchaseOrderMapper::toSummaryDTO)
                .toList();
    }

    @Override
    public Page<PurchaseOrderPageItemDTO> getPurchaseOrderByClientId(UUID idClient, int page) {

        try{

            PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("issueDate").descending());
            Page<ClientPurchaseOrderEntity> entities = clientPurchaseOrderRepository.getClientPurchaseOrders(idClient, pageRequest);

            List<PurchaseOrderPageItemDTO> purchaseOrders = entities.getContent()
                    .stream()
                    .map(entity -> purchaseOrderMapper.toDomain(entity, PurchaseOrderType.SALE))
                    .map(purchaseOrderMapper::domainToPageItem)
                    .collect(Collectors.toList());
            return new PageImpl<>(purchaseOrders, pageRequest, entities.getTotalElements());
        }
        catch (DataAccessException ex){
          throw BillingException.internalError("Erreur de fetch des bons de commandes: " + ex.getMessage());

        }
    }
}
