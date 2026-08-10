package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.out.PaymentRepositoryPort;
import com.example.billingservice.domain.enums.PaymentMethod;
import com.example.billingservice.domain.enums.PaymentStatus;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Payment;
import com.example.billingservice.infrastructure.out.persistance.dto.CreatePaymentDto;
import com.example.billingservice.infrastructure.out.persistance.dto.PaymentPageListItemDto;
import com.example.billingservice.infrastructure.out.persistance.dto.UpdatePaymentDTO;
import com.example.billingservice.infrastructure.out.persistance.entity.PaymentEntity;
import com.example.billingservice.infrastructure.out.persistance.mapper.PaymentMapper;
import com.example.billingservice.infrastructure.out.persistance.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment getPaymentById(UUID idPayment) {
        PaymentEntity paymentEntity = paymentRepository.getReferenceById(idPayment);
        return paymentMapper.entityToModel(paymentEntity);
    }

    @Override
    public Page<PaymentPageListItemDto> getPayments(String keyword, PaymentMethod paymentMethod, int page) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("paymentDate").descending());
            Page<PaymentEntity> entities = paymentRepository.getPayments(keyword, paymentMethod, pageRequest);

            List<PaymentPageListItemDto> payments = entities.getContent()
                    .stream()
                    .map(paymentMapper::entityToModel)
                    .map(paymentMapper::modelToPageListItem)
                    .collect(Collectors.toList());

            return new PageImpl<>(payments, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }
    }

    @Override
    public Page<PaymentPageListItemDto> getPaymentsByPartner(UUID partnerId, String keyword, String filtre, int page) {
        try{

            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("paymentDate").descending());
            Page<PaymentEntity> entities = paymentRepository.getPaymentsByPartner(keyword,partnerId, pageRequest);

            List<PaymentPageListItemDto> payments = entities.getContent()
                    .stream()
                    .map(paymentMapper::entityToModel)
                    .map(paymentMapper::modelToPageListItem)
                    .collect(Collectors.toList());

            return new PageImpl<>(payments, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }
    }

    @Override
    public Page<PaymentPageListItemDto> getPaymentsByInvoice(UUID invoiceId, String keyword , PaymentMethod paymentMethod, int page) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("paymentDate").descending());
            Page<PaymentEntity> entities = paymentRepository.getPaymentsByInvoice(keyword,invoiceId, pageRequest);

            List<PaymentPageListItemDto> payments = entities.getContent()
                    .stream()
                    .map(paymentMapper::entityToModel)
                    .map(paymentMapper::modelToPageListItem)
                    .collect(Collectors.toList());

            return new PageImpl<>(payments, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentMapper.entityToModel(paymentRepository.save(paymentMapper.modelToEntity(payment)));
    }

    @Override
    public Payment updatePayment(Payment payment) {
        PaymentEntity paymentEntity = paymentRepository.save(paymentMapper.modelToEntity(payment));
        return paymentMapper.entityToModel(paymentEntity);
    }

    @Override
    public void updatePaymentStatus(UUID idPayment, PaymentStatus paymentStatus) {
        PaymentEntity payment = paymentRepository.getReferenceById(idPayment);
        payment.setPaymentStatus(paymentStatus);
        PaymentEntity updatedEntity = paymentRepository.save(payment);

    }

    @Override
    public void deletePayment(UUID idPayment) {
        paymentRepository.deleteById(idPayment);
    }

    @Override
    public boolean existsByIdPayment(UUID idPayment) {
        return paymentRepository.existsByIdPayment(idPayment);
    }

    @Override
    public boolean existsByReference(String reference) {
        return paymentRepository.existsByReference(reference);
    }
}
