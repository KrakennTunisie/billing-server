package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.GenerateInvoiceNumberUseCase;
import com.example.billingservice.application.ports.in.InvoicePaymentSnchronizeUseCase;
import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.ports.in.PaymentUseCase;
import com.example.billingservice.application.ports.out.AuditEventPublisherPort;
import com.example.billingservice.application.ports.out.NotificationPublisherPort;
import com.example.billingservice.application.ports.out.PaymentRepositoryPort;
import com.example.billingservice.domain.enums.DocumentType;
import com.example.billingservice.domain.enums.PaymentMethod;
import com.example.billingservice.domain.enums.PaymentStatus;
import com.example.billingservice.domain.enums.SequenceNumberType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Document;
import com.example.billingservice.domain.model.Payment;
import com.example.billingservice.infrastructure.out.messaging.AuditEvent;
import com.example.billingservice.infrastructure.out.messaging.NotificationEvent;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.mapper.PaymentMapper;
import com.example.billingservice.shared.NotificationEventFactory;
import com.example.billingservice.shared.ParseEnum;
import com.example.billingservice.shared.PaymentAuditEventFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentService implements PaymentUseCase {

    private final InvoicePaymentSnchronizeUseCase invoicePaymentSnchronizeUseCase;
    private final GenerateInvoiceNumberUseCase generateInvoiceNumberUseCase;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final UploadDocumentService uploadDocumentService;
    private final InvoiceUseCase invoiceUseCase;
    private final PaymentMapper paymentMapper;
    private final NotificationPublisherPort notificationPublisherPort;
    private final NotificationEventFactory notificationEventFactory;
    private final AuditEventPublisherPort auditEventPublisherPort;
    private final PaymentAuditEventFactory paymentAuditEventFactory;

    @Override
    public PaymentDTO getPaymentById(UUID idPayment) {
        Payment payment = paymentRepositoryPort.getPaymentById(idPayment);
        return paymentMapper.modelToPaymentDTO(payment);
    }

    @Override
    public Page<PaymentPageListItemDto> getPaymentsByInvoice(UUID invoiceId, String keyword, String filtre, int page) {
        PaymentMethod status = ParseEnum.parseEnum(filtre, PaymentMethod.class);
        return paymentRepositoryPort.getPaymentsByInvoice(invoiceId, keyword,  status, page);
    }

    @Override
    public Page<PaymentPageListItemDto> getPaymentsByPartner(UUID partnerId, String keyword, String filtre, int page) {
        return paymentRepositoryPort.getPaymentsByPartner(partnerId, keyword, filtre, page);
    }

    @Override
    public Page<PaymentPageListItemDto> getPayments(String keyword, String filtre, int page) {
        PaymentMethod status = ParseEnum.parseEnum(filtre, PaymentMethod.class);

        return paymentRepositoryPort.getPayments(keyword, status, page);
    }

    @Override
    public PaymentDTO createPayment(CreatePaymentDto createPaymentDto) throws IOException {

        if(!invoiceUseCase.clientInvoiceExistsByInvoiceId(UUID.fromString(createPaymentDto.getInvoiceNumber())))
        {
            throw BillingException.notFound("Facture Client", createPaymentDto.getInvoiceNumber());
        }

        invoicePaymentSnchronizeUseCase.applyPayment(
                UUID.fromString(createPaymentDto.getInvoiceNumber()),
                createPaymentDto.getAmount()
         );

        String paymentNumber = generateInvoiceNumberUseCase.generate(SequenceNumberType.PAYMENT);

        createPaymentDto.setPaymentNumber(paymentNumber);

        Document paymentDocument = null;
        if (createPaymentDto.getPaymentDocument() != null && !createPaymentDto.getPaymentDocument().isEmpty()) {
            UploadedFile document = new UploadedFile(
                    createPaymentDto.getPaymentDocument().getOriginalFilename(),
                    createPaymentDto.getPaymentDocument().getContentType(),
                    createPaymentDto.getPaymentDocument().getInputStream(),
                    createPaymentDto.getPaymentDocument().getSize()
            );

            paymentDocument = uploadDocumentService.upload(
                    paymentNumber,
                    DocumentType.PAYMENT,
                    document
            );
        }

        Payment payment = paymentMapper.createDtoToModel(createPaymentDto, paymentDocument);


        Payment createdPayment = paymentRepositoryPort.createPayment(payment);

        generateInvoiceNumberUseCase.validateNextSequence(SequenceNumberType.PAYMENT, paymentNumber);

        PaymentDTO paymentDTO = paymentMapper.modelToPaymentDTO(createdPayment);

        NotificationEvent notificationEvent = notificationEventFactory.createPaymentCreated(
                paymentDTO.getIdPayment(),
                paymentDTO.getInvoice().getIdInvoice(),
                paymentDTO.getInvoice().getInvoiceNumber(),
                paymentDTO.getAmount(),
                paymentDTO.getCurrency()
        );
        notificationPublisherPort.publish(notificationEvent);

        AuditEvent auditEvent = paymentAuditEventFactory.paymentCreated(
                paymentDTO.getIdPayment(),
                String.valueOf(paymentDTO.getIdPayment()),
                Map.of("Payment number", paymentNumber),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return paymentDTO;
    }

    @Override
    public PaymentDTO updatePayment(UUID idPayment, UpdatePaymentDTO updatePaymentDTO) throws IOException {

        if(!paymentRepositoryPort.existsByIdPayment(idPayment)){
            throw BillingException.notFound("Paiement", String.valueOf(idPayment));
        }

        Payment oldPayment = paymentRepositoryPort.getPaymentById(idPayment);
        BigDecimal updatedAmount = updatePaymentDTO.getAmount().subtract(oldPayment.getAmount());


        invoicePaymentSnchronizeUseCase.applyPayment(
                UUID.fromString(updatePaymentDTO.getInvoiceNumber()),
                updatedAmount
        );


        Document paymentDocument = null;
        if (updatePaymentDTO.getPaymentDocument() != null && !updatePaymentDTO.getPaymentDocument().isEmpty()) {
            UploadedFile document = new UploadedFile(
                    updatePaymentDTO.getPaymentDocument().getOriginalFilename(),
                    updatePaymentDTO.getPaymentDocument().getContentType(),
                    updatePaymentDTO.getPaymentDocument().getInputStream(),
                    updatePaymentDTO.getPaymentDocument().getSize()
            );

            paymentDocument = uploadDocumentService.upload(
                    updatePaymentDTO.getPaymentNumber(),
                    DocumentType.PAYMENT,
                    document
            );
        }

        Payment payment = paymentMapper.updateDtoToModel(updatePaymentDTO, paymentDocument);

        Payment updatedPayment = paymentRepositoryPort.updatePayment(payment);

        PaymentDTO paymentDTO =  paymentMapper.modelToPaymentDTO(updatedPayment);

        AuditEvent auditEvent = paymentAuditEventFactory.paymentUpdated(
                paymentDTO.getIdPayment(),
                String.valueOf(paymentDTO.getIdPayment()),
                Map.of("amount", oldPayment.getAmount()),
                Map.of("new amount", paymentDTO.getAmount()),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

        return paymentDTO;
    }

    @Override
    public void updatePaymentStatus(UUID idPayment, PaymentStatus paymentStatus) {
        paymentRepositoryPort.updatePaymentStatus(idPayment, paymentStatus);


    }

    @Override
    public void deletePayment(UUID idPayment) {

        if(!paymentRepositoryPort.existsByIdPayment(idPayment)){
            throw BillingException.notFound("Paiement", String.valueOf(idPayment));
        }

        PaymentDTO paymentDTO = getPaymentById(idPayment);

        invoicePaymentSnchronizeUseCase.applyPayment(
                paymentDTO.getInvoice().getIdInvoice(),
                paymentDTO.getAmount().negate()
        );

        paymentRepositoryPort.deletePayment(idPayment);

        AuditEvent auditEvent = paymentAuditEventFactory.paymentDeleted(
                idPayment,
                String.valueOf(idPayment),
                Map.of("payment", paymentDTO),
                null
        );

        auditEventPublisherPort.publish(auditEvent);

    }

    @Override
    public boolean existsByIdPayment(UUID idPayment) {
        return paymentRepositoryPort.existsByIdPayment(idPayment);
    }

    @Override
    public boolean existsByReference(String reference) {
        return paymentRepositoryPort.existsByReference(reference);
    }
}
