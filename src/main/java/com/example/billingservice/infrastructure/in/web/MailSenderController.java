package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.SendEmailUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/mailing")
@RequiredArgsConstructor
public class MailSenderController {

    private final SendEmailUseCase sendEmailUseCase;

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


}
