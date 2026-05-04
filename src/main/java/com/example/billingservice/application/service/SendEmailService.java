package com.example.billingservice.application.service;

import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.application.ports.in.SendEmailUseCase;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.application.ports.out.DocumentReaderPort;
import com.example.billingservice.application.ports.out.EmailJobPublisherPort;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.domain.model.MailAttachment;
import com.example.billingservice.domain.model.MailJob;
import com.example.billingservice.infrastructure.out.persistance.dto.DocumentReadFile;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoiceDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.SendEmailRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SendEmailService implements SendEmailUseCase {

    private final InvoiceUseCase invoiceUseCase;
    private final DocumentReaderPort documentReaderPort;
    private final EmailJobPublisherPort emailJobPublisherPort;

    @Override
    public void sendInvoiceEmail(UUID invoiceId, SendEmailRequest request) {

        if(!invoiceUseCase.clientInvoiceExistsByInvoiceId(invoiceId)){
            throw BillingException.notFound("Facture client", String.valueOf(invoiceId));
        }

        InvoiceDTO invoice = invoiceUseCase.getClientInvoiceById(invoiceId);

        String partnerEmail = invoice.getPartner().getEmail();

        DocumentReadFile documentReadFile = documentReaderPort.getFileAttachment(invoice.getInvoiceDocument().getIdDocument());

        if (!partnerEmail.equalsIgnoreCase(request.toEmail())) {
            throw new RuntimeException("Email does not match invoice partner email");
        }

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
