package com.example.billingservice.infrastructure.in.web;

import com.example.billingservice.application.ports.in.SendEmailUseCase;
import com.example.billingservice.infrastructure.out.persistance.dto.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
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

    @PostMapping("/{invoiceId}/send-email")
    public ResponseEntity<Void> sendInvoiceEmail(
            @PathVariable String invoiceId,
            @RequestBody SendEmailRequest request
    ) {
        sendEmailUseCase.sendInvoiceEmail(UUID.fromString(invoiceId), request);
        return ResponseEntity.accepted().build();
    }


}
