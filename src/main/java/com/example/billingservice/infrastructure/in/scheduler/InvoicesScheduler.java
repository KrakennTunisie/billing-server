package com.example.billingservice.infrastructure.in.scheduler;

import com.example.billingservice.application.ports.in.InvoiceUseCase;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.infrastructure.out.persistance.dto.InvoicePageItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoicesScheduler {
    private final InvoiceUseCase invoiceUseCase;

    /**
     * Every day at 7 am Tunisia time
     */
    @Scheduled(cron = "0 0 7 * * *", zone = "Africa/Tunis")
    public void updateClientsOverdueInvoices(){
        Date referenceDate = new Date();

        try{

            log.info("Starting daily clients invoices overdue updates date: {}", referenceDate);

            List<InvoicePageItemDTO> invoices = invoiceUseCase.getClientsOverdueInvoices(referenceDate);

            if(invoices.isEmpty()){
                log.info("Nothing to update for today: {}", referenceDate);
            }

            for(InvoicePageItemDTO invoice: invoices){

                log.info("Updating client invoice: {} ...", invoice.getInvoiceNumber());

                invoiceUseCase.updateClientInvoiceStatus(invoice.getIdInvoice(), InvoiceStatus.OVERDUE);

            }

        } catch (Exception e) {
            log.error(
                    "Failed apply updates",  e
            );
        }
        finally {
            log.info("Work done for today: {}", referenceDate);

        }
    }

    @Scheduled(cron = "0 30 7 * * *", zone = "Africa/Tunis")
    public void updateSuppliersOverdueInvoices(){
        Date referenceDate = new Date();

        try{

            log.info("Starting daily suppliers invoices overdue updates date: {}", referenceDate);

            List<InvoicePageItemDTO> invoices = invoiceUseCase.getSuppliersOverdueInvoices(referenceDate);

            if(invoices.isEmpty()){
                log.info("Nothing to update for today: {}", referenceDate);
            }

            for(InvoicePageItemDTO invoice: invoices){

                log.info("Updating supplier invoice: {} ...", invoice.getInvoiceNumber());

                invoiceUseCase.updateInvoiceStatus(invoice.getIdInvoice(), InvoiceStatus.OVERDUE);

            }

        } catch (Exception e) {
            log.error(
                    "Failed apply updates",  e
            );
        }
        finally {
            log.info("Work done for today: {}", referenceDate);

        }
    }
}
