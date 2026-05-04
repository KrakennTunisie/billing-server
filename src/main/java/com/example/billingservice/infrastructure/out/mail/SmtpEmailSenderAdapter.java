package com.example.billingservice.infrastructure.out.mail;

import com.example.billingservice.application.ports.out.EmailSenderPort;
import com.example.billingservice.domain.model.MailAttachment;
import com.example.billingservice.domain.model.MailJob;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SmtpEmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender javaMailSender;


    @Override
    public void sendEmail(MailJob job) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            boolean hasAttachments =
                    job.attachments() != null && !job.attachments().isEmpty();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, hasAttachments, "UTF-8");

            helper.setTo(job.to());
            helper.setSubject(job.subject());
            helper.setText(job.body(), job.html());

            if (hasAttachments) {
                for (MailAttachment attachment : job.attachments()) {
                    helper.addAttachment(
                            attachment.filename(),
                            new ByteArrayResource(attachment.content())
                    );
                }
            }

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to prepare mail", e);
        } catch (MailException e) {
            throw e;
        }
    }
}
