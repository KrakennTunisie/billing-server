package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.InvoiceCreditNoteUseCase;
import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.ports.in.PurchaseOrderUseCase;
import com.example.billingservice.application.ports.in.SendEmailUseCase;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.application.ports.out.DocumentReaderPort;
import com.example.billingservice.application.ports.out.EmailJobPublisherPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.domain.model.InvoiceCreditNote;
import com.example.billingservice.domain.model.MailAttachment;
import com.example.billingservice.domain.model.MailJob;
import com.example.billingservice.infrastructure.out.persistance.dto.DocumentReadFile;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.PurchaseOrderDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.SendEmailRequest;
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
    private final DocumentReaderPort documentReaderPort;
    private final EmailJobPublisherPort emailJobPublisherPort;

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
                partnerEmail,
                request.subject(),
                request.body(),
                true,
                List.of(
                        new MailAttachment(
                                "facture-" + invoice.getInvoiceNumber()+".pdf",
                                documentReadFile.mimeType(),
                                documentReadFile.content()
                        )
                )
        );

        emailJobPublisherPort.publish(job);
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
                partnerEmail,
                request.subject(),
                request.body(),
                true,
                List.of(
                        new MailAttachment(
                                "facture-" + invoiceCreditNote.getInvoiceCreditNoteNumber()+".pdf",
                                documentReadFile.mimeType(),
                                documentReadFile.content()
                        )
                )
        );

        emailJobPublisherPort.publish(job);
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
                partnerEmail,
                request.subject(),
                request.body(),
                true,
                List.of(
                        new MailAttachment(
                                "Bon-commande-" + purchaseOrderDTO.getPurchaseOrderNumber()+".pdf",
                                documentReadFile.mimeType(),
                                documentReadFile.content()
                        )
                )
        );

        emailJobPublisherPort.publish(job);
    }

    @Override
    public void sendEmail(SendEmailRequest request) {
        MailJob job = new MailJob(
                request.toEmail(),
                request.subject(),
                request.body(),
                true,
                List.of()
        );

        emailJobPublisherPort.publish(job);
    }
}
