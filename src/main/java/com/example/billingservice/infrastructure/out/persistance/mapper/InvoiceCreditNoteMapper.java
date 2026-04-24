package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.model.*;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteEventEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceCreditNoteItemEntity;
import com.example.billingservice.shared.CurrencyCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceCreditNoteMapper {
    private final InvoiceMapper invoiceMapper;
    private final InvoiceCreditNoteItemMapper invoiceCreditNoteItemMapper;
    private final InvoiceCreditNoteEventMapper invoiceCreditNoteEventMapper;
    private final DocumentMapper documentMapper;
    private final CurrencyCalculator currencyCalculator;

    // =========================
    // ENTITY -> DOMAIN
    // =========================
    public InvoiceCreditNote toDomain(InvoiceCreditNoteEntity entity) {
        if (entity == null) {
            return null;
        }

        InvoiceCreditNote invoiceCreditNote =  InvoiceCreditNote.builder()
                .motif(entity.getMotif())
                .description(entity.getDescription())
                .invoice(
                        entity.getInvoice() != null
                                ? invoiceMapper.toDomain(
                                        entity.getInvoice(),
                                        entity.getInvoice().getInvoiceType())
                                : null
                )
                .invoiceCreditNoteItems(
                        entity.getInvoiceCreditNoteItems() != null
                                ? entity.getInvoiceCreditNoteItems().stream()
                                .map(invoiceCreditNoteItemMapper::toDomain)
                                .toList()
                                : List.of()
                )
                .invoiceCreditNoteEvents(
                        entity.getInvoiceCreditNoteEvents() != null
                                ? entity.getInvoiceCreditNoteEvents().stream()
                                .map(invoiceCreditNoteEventMapper::toDomain)
                                .toList()
                                : List.of()
                )
                .idInvoiceCreditNote(entity.getIdInvoiceCreditNoteEntity())
                .invoiceCreditNoteNumber(entity.getInvoiceCreditNoteNumber())
                .issueDate(entity.getIssueDate())
                .qrCode(entity.getQrCode())
                .complianceStatus(entity.getComplianceStatus())
                .invoiceCreditNoteStatus(entity.getInvoiceCreditNoteStatus())
                .build();
        invoiceCreditNote
                .setInvoiceCreditNoteDocument(documentMapper.toDomain(entity.getInvoiceCreditNoteDocument()));

        return invoiceCreditNote;

    }



    // =========================
    // DOMAIN -> ENTITY
    // =========================
    public InvoiceCreditNoteEntity toEntity(InvoiceCreditNote domain) {
        if (domain == null) {
            return null;
        }

        InvoiceCreditNoteEntity entity = new InvoiceCreditNoteEntity();
        entity.setIdInvoiceCreditNoteEntity(domain.getIdInvoiceCreditNote());
        entity.setMotif(domain.getMotif());
        entity.setDescription(domain.getDescription());
        entity.setInvoiceCreditNoteStatus(domain.getInvoiceCreditNoteStatus());
        entity.setInvoiceCreditNoteNumber(domain.getInvoiceCreditNoteNumber());
        entity.setComplianceStatus(domain.getComplianceStatus());
        entity.setIssueDate(domain.getIssueDate());

        if (domain.getInvoice() != null) {
            entity.setInvoice(invoiceMapper.toEntity(domain.getInvoice()));
        }

        List<InvoiceCreditNoteItemEntity> itemEntities = domain.getInvoiceCreditNoteItems().stream()
                    .map(invoiceCreditNoteItemMapper::toEntity)
                    .toList();

        itemEntities.forEach(item -> item.setInvoiceCreditNote(entity));
        entity.setInvoiceCreditNoteItems(itemEntities);


        entity.setInvoiceCreditNoteDocument(
                documentMapper.toEntity(domain.getInvoiceCreditNoteDocument(), DocumentType.INVOICE)
        );


        List<InvoiceCreditNoteEventEntity> invoiceEventEntities = domain.getInvoiceCreditNoteEvents() != null
                ? domain.getInvoiceCreditNoteEvents()
                .stream()
                .map(invoiceCreditNoteEventMapper::toEntity)
                .toList()
                : List.of();

        invoiceEventEntities.forEach(event -> event.setInvoiceCreditNote(entity));

        entity.setInvoiceCreditNoteEvents(invoiceEventEntities);


        return entity;
    }


    // =========================
    // DTO -> DOMAIN
    // =========================
    public InvoiceCreditNote toDomain(InvoiceCreditNoteCreateDTO dto, Document document, Invoice invoice) {
        if (dto == null) {
            return null;
        }



         InvoiceCreditNote invoiceCreditNote = InvoiceCreditNote.builder()
                .motif(dto.getMotif())
                .description(dto.getDescription())
                // ⚠️ on ne mappe pas toute la facture ici
                .invoice(invoice)
                .invoiceCreditNoteItems(
                        dto.getInvoiceItems() != null
                                ? dto.getInvoiceItems().stream()
                                .map(invoiceCreditNoteItemMapper::toDomain)
                                .toList()
                                : List.of()
                )
                .invoiceCreditNoteDocument(document)
                .invoiceCreditNoteNumber(dto.getInvoiceCreditNoteNumber())
                .qrCode(null)
                 .issueDate(new Date())
                 .complianceStatus(InvoiceComplianceStatus.TTN_PENDING)
                 .invoiceCreditNoteStatus(InvoiceCreditNoteStatus.DRAFT)
                .build();

        InvoiceCreditNoteEvent invoiceCreditNoteEvent =
                InvoiceCreditNoteEvent.builder()
                        .invoiceCreditNoteEventType(InvoiceCreditNoteEventType.CREATED)
                        .eventDate(new Date())
                        .description("Facture d'avoir créé")
                        .eventTrigger(InvoiceEventTrigger.USER)
                        .triggeredBy("user: wassef ammar")
                        .build();

        List<InvoiceCreditNoteEvent> invoiceCreditNoteEvents= new ArrayList<>();

        invoiceCreditNoteEvents.add(invoiceCreditNoteEvent);

        invoiceCreditNote.setInvoiceCreditNoteEvents(invoiceCreditNoteEvents);


        return invoiceCreditNote;
    }

    public InvoiceCreditNotePageItemDTO toPageItemDTO(InvoiceCreditNote domain) {
        if (domain == null) {
            return null;
        }

        List<InvoiceCreditNoteItem> items = domain.getInvoiceCreditNoteItems() != null
                ? domain.getInvoiceCreditNoteItems()
                : List.of();

        double totalExclTax = items.stream()
                .mapToDouble(item -> item.getInvoiceItem().getItemTotalExclTax() != null ? item.getInvoiceItem().getItemTotalExclTax() : 0.0)
                .sum();

        double totalInclTax = items.stream()
                .mapToDouble(item -> item.getInvoiceItem().getItemTotalInclTax() != null ? item.getInvoiceItem().getItemTotalInclTax(): 0.0)
                .sum();

        CurrencyTotals totals = currencyCalculator.calculateTotals(
                domain.getInvoice().getCurrency().name(),
                totalExclTax,
                totalInclTax,
                domain.getInvoice().getAppliedExchangeRate()
        );


        return InvoiceCreditNotePageItemDTO.builder()
                .idInvoiceCreditNote(domain.getIdInvoiceCreditNote())
                .invoiceCreditNoteNumber(domain.getInvoiceCreditNoteNumber())
                .motif(domain.getMotif())
                .issueDate(domain.getIssueDate())
                .invoiceCreditNoteStatus(domain.getInvoiceCreditNoteStatus())
                .invoiceCreditNoteComplianceStatus(domain.getComplianceStatus())
                .total(totalInclTax)
                .totalExclTaxEUR(totals.totalExclTaxEUR())
                .totalInclTaxEUR(totals.totalInclTaxEUR())
                .totalExclTaxTND(totals.totalExclTaxEUR())
                .totalInclTaxTND(totals.totalInclTaxTND())
                .totalExclTaxUSD(totals.totalExclTaxUSD())
                .totalInclTaxUSD(totals.totalInclTaxUSD())
                .invoice(
                        domain.getInvoice() != null
                                ? invoiceMapper.toSummaryDTO(domain.getInvoice())
                                : null
                )
                .build();
    }

    public InvoiceCreditNoteDTO toDTO(InvoiceCreditNote invoiceCreditNote){
        if(invoiceCreditNote==null){
            return null;
        }
        List<InvoiceCreditNoteItem> items = invoiceCreditNote.getInvoiceCreditNoteItems() != null
                ? invoiceCreditNote.getInvoiceCreditNoteItems()
                : List.of();

        double totalInclTax = items.stream()
                .mapToDouble(
                        item -> item.getQuantity() != null
                                ? item.getQuantity() * item.getInvoiceItem().getUnityPriceEXclTax() *(1+item.getInvoiceItem().getVatRate())
                                : 0.0
                )
                .sum();
        List<InvoiceCreditNoteEvent> invoiceCreditNoteEvents = invoiceCreditNote.getInvoiceCreditNoteEvents() != null
                ? invoiceCreditNote.getInvoiceCreditNoteEvents()
                : List.of();
        return
                InvoiceCreditNoteDTO.builder()
                        .idInvoiceCreditNote(invoiceCreditNote.getIdInvoiceCreditNote())
                        .invoiceCreditNoteNumber(invoiceCreditNote.getInvoiceCreditNoteNumber())
                        .motif(invoiceCreditNote.getMotif())
                        .description(invoiceCreditNote.getDescription())
                        .issueDate(invoiceCreditNote.getIssueDate())
                        .invoiceCreditNoteStatus(invoiceCreditNote.getInvoiceCreditNoteStatus())
                        .invoiceCreditNoteComplianceStatus(invoiceCreditNote.getComplianceStatus())
                        .total(totalInclTax)
                        .invoice(invoiceMapper.toSummaryDTO(invoiceCreditNote.getInvoice()))
                        .invoiceCreditNoteEvents(invoiceCreditNoteEvents)
                        .build();
    }

    public InvoiceCreditNoteDetailsDTO toDetailsDTO(InvoiceCreditNote invoiceCreditNote){
        if(invoiceCreditNote==null){
            return null;
        }

        List<InvoiceCreditNoteItem> items = invoiceCreditNote.getInvoiceCreditNoteItems() != null
                ? invoiceCreditNote.getInvoiceCreditNoteItems()
                : List.of();

        double totalExclTax = items.stream()
                .mapToDouble(item -> item.getInvoiceItem().getItemTotalExclTax() != null ? item.getInvoiceItem().getItemTotalExclTax() : 0.0)
                .sum();

        double totalInclTax = items.stream()
                .mapToDouble(item -> item.getInvoiceItem().getItemTotalInclTax() != null ? item.getInvoiceItem().getItemTotalInclTax(): 0.0)
                .sum();

        CurrencyTotals totals = currencyCalculator.calculateTotals(
                invoiceCreditNote.getInvoice().getCurrency().name(),
                totalExclTax,
                totalInclTax,
                invoiceCreditNote.getInvoice().getAppliedExchangeRate()
        );

        return InvoiceCreditNoteDetailsDTO.builder()
                .idInvoiceCreditNote(invoiceCreditNote.getIdInvoiceCreditNote())
                .invoiceCreditNoteNumber(invoiceCreditNote.getInvoiceCreditNoteNumber())
                .motif(invoiceCreditNote.getMotif())
                .description(invoiceCreditNote.getDescription())
                .issueDate(invoiceCreditNote.getIssueDate())
                .invoiceCreditNoteStatus(invoiceCreditNote.getInvoiceCreditNoteStatus())
                .invoiceCreditNoteComplianceStatus(invoiceCreditNote.getComplianceStatus())
                .total(0.0)
                .invoice(invoiceMapper.toSummaryDTO(invoiceCreditNote.getInvoice()))
                .invoiceCreditNoteItems(invoiceCreditNote.getInvoiceCreditNoteItems())
                .invoiceCreditNoteEvents(invoiceCreditNote.getInvoiceCreditNoteEvents())
                .invoiceCreditNoteDocument(documentMapper.toDocumentSummary(invoiceCreditNote.getInvoiceCreditNoteDocument()))
                .totalExclTaxEUR(totals.totalExclTaxEUR())
                .totalInclTaxEUR(totals.totalInclTaxEUR())
                .totalExclTaxTND(totals.totalExclTaxEUR())
                .totalInclTaxTND(totals.totalInclTaxTND())
                .totalExclTaxUSD(totals.totalExclTaxUSD())
                .totalInclTaxUSD(totals.totalInclTaxUSD())
                .build();
    }


}
