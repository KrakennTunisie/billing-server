package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.*;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.enums.MailEventType;
import com.example.billingservice.domain.exceptions.BillingException;

import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.infrastructure.out.persistance.dto.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SendMailNotificationService implements SendEmailUseCase {

    private final InvoiceUseCase invoiceUseCase;
    private final MailJobUseCase mailJobUseCase;
    private final PartnerUseCase partnerUseCase;
    private final InvoiceCreditNoteUseCase invoiceCreditNoteUseCase;
    private final PurchaseOrderUseCase purchaseOrderUseCase;
    private final PaymentUseCase paymentUseCase;

    @Override
    public void sendInvoiceEmail(UUID invoiceId, SendEmailRequest request) {
        if(!invoiceUseCase.clientInvoiceExistsByInvoiceId(invoiceId)){
            throw BillingException.notFound("Facture client", String.valueOf(invoiceId));
        }

        InvoiceDTO invoice = invoiceUseCase.getClientInvoiceById(invoiceId);

        String partnerEmail = invoice.getPartner().getEmail();

        if (!partnerEmail.equalsIgnoreCase(request.toEmail())) {
            throw new RuntimeException("Email does not match invoice partner email");
        }
        MailJobAttachmentRequest mailJobAttachmentRequest = new MailJobAttachmentRequest(
                invoice.getInvoiceDocument().getIdDocument(),
                invoice.getInvoiceDocument().getFileName(),
                invoice.getInvoiceDocument().getStorageURL()
        );
        MailJobRequest mailJobRequest = new MailJobRequest(
                partnerEmail,
                request.subject(),
                request.body(),
                MailEventType.INVOICE_CREATED,
                List.of(mailJobAttachmentRequest)
        );

        mailJobUseCase.createMailJob(mailJobRequest);


        if(invoice.getInvoiceType() == InvoiceType.SALE && invoice.getInvoiceStatus()== InvoiceStatus.DRAFT){
            invoiceUseCase.updateClientInvoiceStatus(invoiceId, InvoiceStatus.TO_COLLECT);
        }


    }

    @Override
    public void sendCreditNoteEmail(UUID invoiceCreditNoteId, SendEmailRequest request) {
        if(!invoiceCreditNoteUseCase.existsByInvoiceCreditNoteId(invoiceCreditNoteId)){
            throw BillingException.notFound("Facture avoir", String.valueOf(invoiceCreditNoteId));
        }

        InvoiceCreditNote invoiceCreditNote = invoiceCreditNoteUseCase.getInvoiceCreditNote(invoiceCreditNoteId);

        String partnerEmail = invoiceCreditNote.getInvoice().getPartner().getEmail();

        if (!partnerEmail.equalsIgnoreCase(request.toEmail())) {
            throw new RuntimeException("Email does not match invoice partner email");
        }

        MailJobAttachmentRequest mailJobAttachmentRequest = new MailJobAttachmentRequest(
                invoiceCreditNote.getInvoiceCreditNoteDocument().getIdDocument(),
                invoiceCreditNote.getInvoiceCreditNoteDocument().getFileName(),
                invoiceCreditNote.getInvoiceCreditNoteDocument().getStorageURL()
        );
        MailJobRequest mailJobRequest = new MailJobRequest(
                partnerEmail,
                request.subject(),
                request.body(),
                MailEventType.INVOICE_CREATED,
                List.of(mailJobAttachmentRequest)
        );
        mailJobUseCase.createMailJob(mailJobRequest);
    }

    @Override
    public void sendPurchaseOrderEmail(UUID purchaseOrderId, SendEmailRequest request) {
        if(!purchaseOrderUseCase.existsBySupplierPurchaseOrderId(purchaseOrderId)){
            throw BillingException.notFound("Bon de commande", String.valueOf(purchaseOrderId));
        }

        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderUseCase.getSupplierPurchaseOrderById(purchaseOrderId);

        String partnerEmail = purchaseOrderDTO.getPartner().getEmail();

        if (!partnerEmail.equalsIgnoreCase(request.toEmail())) {
            throw new RuntimeException("Email does not match invoice partner email");
        }

        MailJobAttachmentRequest mailJobAttachmentRequest = new MailJobAttachmentRequest(
                purchaseOrderDTO.getPurchaseOrderDocument().getIdDocument(),
                purchaseOrderDTO.getPurchaseOrderDocument().getFileName(),
                purchaseOrderDTO.getPurchaseOrderDocument().getStorageURL()
        );
        MailJobRequest mailJobRequest = new MailJobRequest(
                partnerEmail,
                request.subject(),
                request.body(),
                MailEventType.INVOICE_CREATED,
                List.of(mailJobAttachmentRequest)
        );
        mailJobUseCase.createMailJob(mailJobRequest);
    }

    @Override
    public void sendPaymentEmail(UUID paymentId, SendEmailRequest request) {
        if(!paymentUseCase.existsByIdPayment(paymentId)){
            throw BillingException.notFound("Paiement", String.valueOf(paymentId));
        }

        PaymentDTO paymentDTO = paymentUseCase.getPaymentById(paymentId);

        String partnerEmail = paymentDTO.getInvoice().getPartner().getEmail();

        if (!partnerEmail.equalsIgnoreCase(request.toEmail())) {
            throw new RuntimeException("Email does not match invoice partner email");
        }

        MailJobAttachmentRequest mailJobAttachmentRequest = new MailJobAttachmentRequest(
                paymentDTO.getPaymentDocument().getIdDocument(),
                paymentDTO.getPaymentDocument().getFileName(),
                paymentDTO.getPaymentDocument().getStorageURL()
        );
        MailJobRequest mailJobRequest = new MailJobRequest(
                partnerEmail,
                request.subject(),
                request.body(),
                MailEventType.INVOICE_CREATED,
                List.of(mailJobAttachmentRequest)
        );
        mailJobUseCase.createMailJob(mailJobRequest);
    }

    @Override
    public void sendEmail(SendEmailRequest request) {
        if(!partnerUseCase.customerExistsByEmail(request.toEmail()) &&
           !partnerUseCase.supplierExistsByEmail(request.toEmail())){
            throw BillingException.notFound("Partenaire", request.toEmail());
        }


        MailJobRequest mailJobRequest = new MailJobRequest(
                request.toEmail(),
                request.subject(),
                request.body(),
                MailEventType.INVOICE_CREATED,
                List.of()
        );

        mailJobUseCase.createMailJob(mailJobRequest);
    }
}
