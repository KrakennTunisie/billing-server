package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.PurchaseOrderRepoistoryPort;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.domain.model.PurchaseOrder;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderPageItemDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.PurchaseOrderEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PurchaseOrderMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PurchaseOrderRepositoryAdapter implements PurchaseOrderRepoistoryPort {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public Page<PurchaseOrderPageItemDTO> findAllPurchaseOrders(String keyword, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("issueDate").descending());
        Page<PurchaseOrderEntity> entities = purchaseOrderRepository.getPurchaseOrders(keyword, pageRequest);

        List<PurchaseOrderPageItemDTO> invoices = entities.getContent()
                .stream()
                .map(purchaseOrderMapper::toDomain)
                .map(purchaseOrderMapper::domainToPageItem)
                .collect(Collectors.toList());

        return new PageImpl<>(invoices, pageRequest, entities.getTotalElements());
    }

    @Override
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        PurchaseOrderEntity entity = purchaseOrderMapper.toEntity(purchaseOrder);
        PurchaseOrderEntity savedEntity = purchaseOrderRepository.save(entity);
        PurchaseOrder savedPurchaseOrder = purchaseOrderMapper.toDomain(savedEntity);/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/


        return  savedPurchaseOrder;
    }

    @Override
    public PurchaseOrder getById(UUID idPurchaseOrder) {
        PurchaseOrderEntity purchaseOrderEntity = purchaseOrderRepository.getReferenceById(idPurchaseOrder);
        return purchaseOrderMapper.toDomain(purchaseOrderEntity);
    }

    @Override
    public PurchaseOrderDTO update(PurchaseOrder purchaseOrder) {
        PurchaseOrderEntity entity = purchaseOrderMapper.toEntity(purchaseOrder);
        entity.setIdPurchaseOrder(purchaseOrder.getIdPurchaseOrder());
        PurchaseOrderEntity savedEntity = purchaseOrderRepository.save(entity);
        PurchaseOrder purchaseOrder1 = purchaseOrderMapper.toDomain(savedEntity);/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/

        return  purchaseOrderMapper.domainToPurchaseOrderDTO(purchaseOrder1);
    }

    @Override
    public void delete(UUID idPurchaseOrder) {
        PurchaseOrderEntity purchaseOrderEntity = purchaseOrderRepository.getReferenceById(idPurchaseOrder);
        purchaseOrderRepository.delete(purchaseOrderEntity);
    }

    @Override
    public boolean existsByPurchaseOrderNumber(String purchaseOrderNumber) {
        return purchaseOrderRepository.existsByReference(purchaseOrderNumber);
    }

    @Override
    public boolean existsByPurchaseOrderId(UUID purchaseOrderId) {
        return purchaseOrderRepository.existsByIdPurchaseOrder(purchaseOrderId);
    }
}
