package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.*;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.application.ports.out.DocumentReaderPort;
import com.example.billingservice.application.ports.out.EmailJobPublisherPort;
import com.example.billingservice.application.ports.out.MailJobRepositoryPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.domain.model.MailAttachment;
import com.example.billingservice.domain.model.MailJob;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SendEmailService implements SendEmailUseCase {

    private final InvoiceUseCase invoiceUseCase;
    private final InvoiceCreditNoteUseCase invoiceCreditNoteUseCase;
    private final PurchaseOrderUseCase purchaseOrderUseCase;
    private final PaymentUseCase paymentUseCase;
    private final DocumentReaderPort documentReaderPort;
    private final EmailJobPublisherPort emailJobPublisherPort;
    private final MailJobRepositoryPort mailJobRepositoryPort;

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

        DocumentReadFile documentReadFile = documentReaderPort.getFileAttachment(invoice.getInvoiceDocument().getIdDocument());

        MailJob job = new MailJob(
              //  UUID.randomUUID(),
                partnerEmail,
                request.subject(),
                request.body(),
                true,
                List.of(
                        new MailAttachment(
                                "facture-" + invoice.getInvoiceNumber()+".pdf",
                                invoice.getInvoiceDocument().getStorageURL(),
                                documentReadFile.mimeType(),
                                documentReadFile.content()
                        )
                )
        );
        mailJobRepositoryPort.save(job);

        emailJobPublisherPort.publish(job);
      //  mailJobRepositoryPort.save(job);
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

        DocumentReadFile documentReadFile = documentReaderPort.getFileAttachment(invoiceCreditNote.getInvoiceCreditNoteDocument().getIdDocument());

        MailJob job = new MailJob(
               // UUID.randomUUID(),
                partnerEmail,
                request.subject(),
                request.body(),
                true,
                List.of(
                        new MailAttachment(
                                "facture-" + invoiceCreditNote.getInvoiceCreditNoteNumber()+".pdf",
                                invoiceCreditNote.getInvoiceCreditNoteDocument().getStorageURL(),
                                documentReadFile.mimeType(),
                                documentReadFile.content()
                        )
                )
        );
        mailJobRepositoryPort.save(job);

        emailJobPublisherPort.publish(job);
       // mailJobRepositoryPort.save(job);

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

        DocumentReadFile documentReadFile = documentReaderPort.getFileAttachment(purchaseOrderDTO.getPurchaseOrderDocument().getIdDocument());

        MailJob job = new MailJob(
               // UUID.randomUUID(),
                partnerEmail,
                request.subject(),
                request.body(),
                true,
                List.of(
                        new MailAttachment(
                                "Bon-commande-" + purchaseOrderDTO.getPurchaseOrderNumber()+".pdf",
                                purchaseOrderDTO.getPurchaseOrderDocument().getStorageURL(),
                                documentReadFile.mimeType(),
                                documentReadFile.content()
                        )
                )
        );

        mailJobRepositoryPort.save(job);

        emailJobPublisherPort.publish(job);
     //   mailJobRepositoryPort.save(job);

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

        DocumentReadFile documentReadFile = documentReaderPort.getFileAttachment(paymentDTO.getPaymentDocument().getIdDocument());

        MailJob job = new MailJob(
               // UUID.randomUUID(),
                partnerEmail,
                request.subject(),
                request.body(),
                true,
                List.of(
                        new MailAttachment(
                                "Paiement-" + paymentDTO.getReference()+".pdf",
                                paymentDTO.getPaymentDocument().getStorageURL(),
                                documentReadFile.mimeType(),
                                documentReadFile.content()
                        )
                )
        );

        mailJobRepositoryPort.save(job);

        emailJobPublisherPort.publish(job);
       // mailJobRepositoryPort.save(job);

    }

    @Override
    public void sendEmail(SendEmailRequest request) {
        MailJob job = new MailJob(
              //  UUID.randomUUID(),
                request.toEmail(),
                request.subject(),
                request.body(),
                true,
                List.of()
        );

        mailJobRepositoryPort.save(job);

        emailJobPublisherPort.publish(job);
     //   mailJobRepositoryPort.save(job);

    }
}
