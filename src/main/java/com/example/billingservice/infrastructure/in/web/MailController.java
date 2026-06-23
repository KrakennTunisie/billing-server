package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.MailJobUseCase;
import com.example.billingservice.application.ports.in.SendEmailUseCase;
import com.example.billingservice.domain.model.MailJobModel;
import com.example.billingservice.infrastructure.out.persistance.dto.MailJobListItemDTO;
import com.example.billingservice.infrastructure.out.persistance.dto.SendEmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/mailing")
@RequiredArgsConstructor
public class MailController {

    private final SendEmailUseCase sendEmailUseCase;

    private final MailJobUseCase mailJobUseCase;

    @GetMapping("/partner/{emailPartner}")
    @Operation(summary = "Liste des factures d'un fournisseur")
    public ResponseEntity<Page<MailJobListItemDTO>> getPartnerMails(
            @Parameter(description = "Partner email") @PathVariable String emailPartner,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String filter,
            @RequestParam int page)
    {

        return ResponseEntity.status(200).body(mailJobUseCase.getEmailByPartner(emailPartner, keyword, filter, page));
    }

    @GetMapping("/mail/{idMail}")
    @Operation(summary = "Liste des factures d'un fournisseur")
    public ResponseEntity<MailJobModel> getMailDetails(
            @Parameter(description = "Partner email") @PathVariable String idMail)
    {

        return ResponseEntity.status(200).body(mailJobUseCase.getMailById(UUID.fromString(idMail)));
    }

    @PostMapping("/send-email")
    public ResponseEntity<Void> sendEmail(
            @RequestBody SendEmailRequest request
    ) {
        sendEmailUseCase.sendEmail(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/invoice/{invoiceId}/send-email")
    public ResponseEntity<Void> sendInvoiceEmail(
            @PathVariable String invoiceId,
            @RequestBody SendEmailRequest request
    ) {
        sendEmailUseCase.sendInvoiceEmail(UUID.fromString(invoiceId), request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/creditNote/{creditNoteId}/send-email")
    public ResponseEntity<Void> sendCreditNoteEmail(
            @PathVariable String creditNoteId,
            @RequestBody SendEmailRequest request
    ) {
        sendEmailUseCase.sendCreditNoteEmail(UUID.fromString(creditNoteId), request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/purchase-order/{purchaseOrderId}/send-email")
    public ResponseEntity<Void> sendPurchaseOrderEmail(
            @PathVariable String purchaseOrderId,
            @RequestBody SendEmailRequest request
    ) {
        sendEmailUseCase.sendPurchaseOrderEmail(UUID.fromString(purchaseOrderId), request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/payment/{paymentId}/send-email")
    public ResponseEntity<Void> sendPaymentEmail(
            @PathVariable String paymentId,
            @RequestBody SendEmailRequest request
    ) {
        sendEmailUseCase.sendPaymentEmail(UUID.fromString(paymentId), request);
        return ResponseEntity.accepted().build();
    }


}
