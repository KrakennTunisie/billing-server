package com.example.billingservice.infrastructure.out.persistance.mapper;

import com.example.billingservice.application.ports.in.PartnerUseCase;
import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.*;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.entity.*;
import com.example.billingservice.shared.CurrencyCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {

    private final InvoiceItemMapper invoiceItemMapper;
    private final DocumentMapper documentMapper;
    private final PartnerMapper partnerMapper;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PartnerUseCase partnerUseCase;
    private final InvoiceEventMapper invoiceEventMapper;

    public InvoiceEntity toEntity(Invoice dto) {
        if (dto == null) {
            return null;
        }

        InvoiceEntity invoice = createEntityByInvoiceType(dto.getInvoiceType());
        invoice.setIdInvoice(dto.getIdInvoice());
        invoice.setReference(dto.getReference());
        invoice.setIssueDate(dto.getIssueDate());
        invoice.setDueDate(dto.getDueDate());
       // invoice.setInvoiceType(dto.getInvoiceType());
        invoice.setInvoiceStatus(dto.getInvoiceStatus() != null ? dto.getInvoiceStatus() : InvoiceStatus.DRAFT);
        invoice.setInvoiceComplianceStatus(
                dto.getInvoiceComplianceStatus() != null
                        ? dto.getInvoiceComplianceStatus()
                        : InvoiceComplianceStatus.TTN_PENDING
        );
        invoice.setVatRate(dto.getVatRate());
        invoice.setPaymentMethod(dto.getPaymentMethod());
        invoice.setExchangeRateReferenceDate(dto.getExchangeRateReferenceDate());
        invoice.setAppliedExchangeRate(dto.getAppliedExchangeRate());
        invoice.setExchangeRateSource(
                dto.getExchangeRateSource() != null && !dto.getExchangeRateSource().name().isBlank()
                        ? dto.getExchangeRateSource()
                        : null
        );
        invoice.setComplianceQRcode(dto.getComplianceQRcode());
        invoice.setInvoiceDocument(documentMapper.toEntity(dto.getInvoiceDocument(), DocumentType.INVOICE));
        invoice.setPurchaseOrder(purchaseOrderMapper.toEntity(dto.getPurchaseOrder()));
        invoice.setPartner(partnerMapper.toEntity(dto.getPartner()));
        invoice.setCurrency(dto.getCurrency());

        List<InvoiceItemEntity> items = dto.getInvoiceItems() != null
                ? dto.getInvoiceItems().stream()
                .map(invoiceItemMapper::invoiceItemToInvoiceEntity)
                .toList()
                : List.of();

        items.forEach(item -> item.setInvoice(invoice));
        invoice.setInvoiceItems(items);

        List<InvoiceEventEntity> invoiceEventEntities = dto.getInvoiceEvents() != null
                ? dto.getInvoiceEvents()
                .stream()
                .map(invoiceEventMapper::toEntity)
                .toList()
                : List.of();

        invoiceEventEntities.forEach(event -> event.setInvoice(invoice));
        invoice.setInvoiceEvents(invoiceEventEntities);

        System.out.println("getPartner: "+dto.getPartner());

        return invoice;
    }


    public Invoice toDomain(InvoiceEntity entity, InvoiceType invoiceType) {
        if (entity == null) {
            return null;
        }
       // PartnerType partnerType = invoiceType == InvoiceType.PURCHASE ? PartnerType.SUPPLIER : PartnerType.CLIENT;
        PartnerType partnerType = entity instanceof SupplierInvoiceEntity ?
                PartnerType.SUPPLIER
                :
                PartnerType.CLIENT;

        Invoice dto =  Invoice.builder()
                .idInvoice(entity.getIdInvoice())
                .reference(entity.getReference())
                .issueDate(entity.getIssueDate())
                .dueDate(entity.getDueDate())
                .invoiceType(invoiceType)
                .invoiceStatus(entity.getInvoiceStatus())
                .invoiceComplianceStatus(entity.getInvoiceComplianceStatus())
                .currency(entity.getCurrency())
                .vatRate(entity.getVatRate())
                .paymentMethod(entity.getPaymentMethod())
                .exchangeRateReferenceDate(entity.getExchangeRateReferenceDate())
                .appliedExchangeRate(entity.getAppliedExchangeRate())
                .exchangeRateSource(entity.getExchangeRateSource())
                .complianceQRcode(entity.getComplianceQRcode())
                .partner(partnerMapper.toDomain(entity.getPartner(), partnerType))
                .invoiceDocument(documentMapper.toDomain(entity.getInvoiceDocument()))
                .build();


        List<InvoiceItem> items = entity.getInvoiceItems() != null
                ? entity.getInvoiceItems()
                .stream()
                .map(invoiceItemMapper::invoiceItemtoDomain)
                .toList()
                : List.of();

        dto.setInvoiceItems(items);

        List<InvoiceEvent> invoiceEvents = entity.getInvoiceEvents() != null
                ? entity.getInvoiceEvents()
                .stream()
                .map(invoiceEventMapper::toDomain)
                .toList()
                : List.of();
        dto.setInvoiceEvents(invoiceEvents);


        double totalExclTax = items.stream()
                .mapToDouble(item -> item.getItemTotalExclTax() != null ? item.getItemTotalExclTax() : 0.0)
                .sum();

        double totalInclTax = items.stream()
                .mapToDouble(item -> item.getItemTotalInclTax() != null ? item.getItemTotalInclTax() : 0.0)
                .sum();

        CurrencyTotals totals = CurrencyCalculator.calculateTotals(
                entity.getCurrency().name(),
                totalExclTax,
                totalInclTax,
                dto.getAppliedExchangeRate()
        );


        dto.setTotalExclTaxEUR(totals.totalExclTaxEUR());
        dto.setTotalInclTaxEUR(totals.totalInclTaxEUR());
        dto.setTotalExclTaxTND(totals.totalExclTaxTND());
        dto.setTotalInclTaxTND(totals.totalExclTaxTND());

        return dto;
    }


    public InvoicePageItemDTO toInvoicePageItemDTO(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        return InvoicePageItemDTO.builder()
                .idInvoice(invoice.getIdInvoice())
                .invoiceNumber(invoice.getReference())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .invoiceType(invoice.getInvoiceType())
                .invoiceStatus(invoice.getInvoiceStatus())
                .invoiceComplianceStatus(invoice.getInvoiceComplianceStatus())
                .invoiceCurrency(invoice.getCurrency())

                // 💰 Currency split
                .totalExclTaxEUR(CurrencyCalculator.getTotalExclTaxEUR(invoice))
                .totalInclTaxEUR(CurrencyCalculator.getTotalInclTaxEUR(invoice))
                .totalExclTaxTND(CurrencyCalculator.getTotalExclTaxTND(invoice))
                .totalInclTaxTND(CurrencyCalculator.getTotalInclTaxTND(invoice))

                .vatRate(invoice.getVatRate())
                .appliedExchangeRate(invoice.getAppliedExchangeRate())

                // ⚠️ Keep light (avoid deep mapping for page/list)
                .purchaseOrder(purchaseOrderMapper.toSummaryDTO(invoice.getPurchaseOrder()))
                .partner(partnerMapper.toSummaryDTO(invoice.getPartner()))

                .build();
    }

    public Invoice invoiceCreateDTOtoDomain(InvoiceCreateDTO invoiceCreateDTO, Document document) throws BillingException{
        if (invoiceCreateDTO == null) {
            return null;
        }
        try{
            Invoice invoice =  Invoice.builder()
                    .reference(invoiceCreateDTO.getInvoiceNumber())
                    .issueDate(invoiceCreateDTO.getIssueDate())
                    .dueDate(invoiceCreateDTO.getDueDate())
                    .invoiceType(InvoiceType.valueOf(invoiceCreateDTO.getInvoiceType()))
                    .invoiceStatus(InvoiceStatus.DRAFT)
                    .invoiceComplianceStatus(InvoiceComplianceStatus.TTN_PENDING)
                    .currency(InvoiceCurrency.valueOf(invoiceCreateDTO.getInvoiceCurrency()))
                    .vatRate(invoiceCreateDTO.getVatRate())
                    .appliedExchangeRate(invoiceCreateDTO.getAppliedExchangeRate())
                    .invoiceDocument(document)
                    .paymentMethod(PaymentMethod.valueOf(invoiceCreateDTO.getPaymentMethod()))
                    .exchangeRateReferenceDate(invoiceCreateDTO.getExchangeRateReferenceDate())
                    .exchangeRateSource(ExchangeRateSource.valueOf(invoiceCreateDTO.getExchangeRateSource()))
                    .complianceQRcode(null)


                    // 🔗 relations (light mapping)
                    // .purchaseOrder()

                    .build();
            Partner partner = getPartner(InvoiceType.valueOf(invoiceCreateDTO.getInvoiceType()), invoiceCreateDTO.getPartner());

            invoice.setPartner(partner);


            List<InvoiceItem> items = invoiceCreateDTO.getInvoiceItems() != null
                    ? invoiceCreateDTO.getInvoiceItems()
                    .stream()
                    .map(invoiceItemMapper::invoiceItemCreateDTOtoDomain)
                    .toList()
                    : List.of();

            invoice.setInvoiceItems(items);


            InvoiceEvent invoiceEvent = InvoiceEvent.builder()
                    .invoiceEventType(InvoiceEventType.CREATED)
                    .eventDate(new Date())
                    .description("Nouvelle facture créé par : "+InvoiceEventTrigger.USER.name())
                    .eventTrigger(InvoiceEventTrigger.USER)
                    .triggeredBy("user: wassef")
                    .build();

            List<InvoiceEvent> invoiceEvents= new ArrayList<>();

            invoiceEvents.add(invoiceEvent);

            invoice.setInvoiceEvents(invoiceEvents);

            double totalExclTax = items.stream()
                    .mapToDouble(item -> item.getItemTotalExclTax() != null ? item.getItemTotalExclTax() : 0.0)
                    .sum();

            double totalInclTax = items.stream()
                    .mapToDouble(item -> item.getItemTotalInclTax() != null ? item.getItemTotalInclTax() : 0.0)
                    .sum();

            CurrencyTotals totals = CurrencyCalculator.calculateTotals(
                    invoiceCreateDTO.getInvoiceCurrency(),
                    totalExclTax,
                    totalInclTax,
                    invoice.getAppliedExchangeRate()
            );


            invoice.setTotalExclTaxEUR(totals.totalExclTaxEUR());
            invoice.setTotalInclTaxEUR(totals.totalInclTaxEUR());
            invoice.setTotalExclTaxTND(totals.totalExclTaxTND());
            invoice.setTotalInclTaxTND(totals.totalExclTaxTND());

            return invoice;
        }
        catch (Exception exception){
            throw BillingException.badRequest(exception.getMessage());
        }



    }


    public InvoiceDTO toDTO(Invoice invoice) {
        if (invoice == null) return null;

        InvoiceDTO invoiceDTO =  InvoiceDTO.builder()
                .idInvoice(invoice.getIdInvoice())
                .invoiceNumber(invoice.getReference())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .invoiceType(invoice.getInvoiceType())
                .invoiceStatus(invoice.getInvoiceStatus())
                .invoiceComplianceStatus(invoice.getInvoiceComplianceStatus())
                .invoiceCurrency(invoice.getCurrency())
                .totalExclTaxEUR(invoice.getTotalExclTaxEUR())
                .totalInclTaxEUR(invoice.getTotalInclTaxEUR())
                .totalExclTaxTND(invoice.getTotalExclTaxTND())
                .totalInclTaxTND(invoice.getTotalInclTaxTND())
                .vatRate(invoice.getVatRate())
                .paymentMethod(invoice.getPaymentMethod())
                .exchangeRateReferenceDate(invoice.getExchangeRateReferenceDate())
                .appliedExchangeRate(invoice.getAppliedExchangeRate())
                .exchangeRateSource(invoice.getExchangeRateSource())
                .complianceQRcode(invoice.getComplianceQRcode())
                .partner(partnerMapper.toSummaryDTO(invoice.getPartner()))
                .purchaseOrder(purchaseOrderMapper.toSummaryDTO(invoice.getPurchaseOrder()))
                .invoiceItems(invoice.getInvoiceItems())
                .invoiceDocument(documentMapper.toDocumentSummary(invoice.getInvoiceDocument()))
                .build();

        List<InvoiceEvent> invoiceEvents = invoice.getInvoiceEvents() != null
                ? invoice.getInvoiceEvents()
                .stream()
                .toList()
                : List.of();

        invoiceDTO.setInvoiceEvents(invoiceEvents);

        //invoiceDTO.setHasInvoiceCreditNotes(invoice.getInvoiceCreditNotes().isEmpty());

        invoiceEvents.forEach(e-> System.out.println(e.toString()));

        return invoiceDTO;
    }


    public Invoice updateDTOtoDomain(InvoiceUpdateDTO invoiceUpdateDTO, InvoiceDTO invoiceDTO, Document document) {
        if (invoiceUpdateDTO == null || invoiceDTO == null) {
            return null;
        }

        try {

            Invoice invoice = Invoice.builder()
                    .idInvoice(invoiceDTO.getIdInvoice())
                    .reference(invoiceUpdateDTO.getInvoiceNumber())
                    .issueDate(invoiceUpdateDTO.getIssueDate())
                    .dueDate(invoiceUpdateDTO.getDueDate())
                    .invoiceType(invoiceDTO.getInvoiceType())
                    .invoiceStatus(InvoiceStatus.valueOf(invoiceUpdateDTO.getInvoiceStatus()))
                    .invoiceComplianceStatus(InvoiceComplianceStatus.TTN_PENDING)
                    .currency(InvoiceCurrency.valueOf(invoiceUpdateDTO.getInvoiceCurrency()))
                    .vatRate(invoiceUpdateDTO.getVatRate())
                    .appliedExchangeRate(invoiceUpdateDTO.getAppliedExchangeRate())
                    .invoiceDocument(document)
                    .paymentMethod(PaymentMethod.valueOf(invoiceUpdateDTO.getPaymentMethod()))
                    .exchangeRateReferenceDate(invoiceUpdateDTO.getExchangeRateReferenceDate())
                    .exchangeRateSource(ExchangeRateSource.valueOf(invoiceUpdateDTO.getExchangeRateSource()))
                    .complianceQRcode(invoiceDTO.getComplianceQRcode())
                    .build();


            String idPartner = String.valueOf(invoiceDTO.getPartner().getIdPartner());
            Partner partner = getPartner(invoiceDTO.getInvoiceType(), idPartner);
            invoice.setPartner(partner);


            List<InvoiceEvent> invoiceEvents = invoiceDTO.getInvoiceEvents() != null
                    ? invoiceDTO.getInvoiceEvents()
                    : List.of();

            InvoiceEvent invoiceEvent = InvoiceEvent.builder()
                    .invoiceEventType(InvoiceEventType.UPDATED)
                    .eventDate(new Date())
                    .description("Mise à jour de facture : "+InvoiceEventTrigger.USER.name())
                    .eventTrigger(InvoiceEventTrigger.USER)
                    .triggeredBy("user: wassef")
                    .build();


            List<InvoiceEvent> updatedEvents = new ArrayList<>(invoiceEvents);

            updatedEvents.add(invoiceEvent);

            invoice.setInvoiceEvents(updatedEvents);

            List<InvoiceItem> items = invoiceUpdateDTO.getInvoiceItems() != null
                    ? invoiceUpdateDTO.getInvoiceItems()
                    .stream()
                    .toList()
                    : List.of();


            invoice.setInvoiceItems(items);

            double totalExclTax = items.stream()
                    .mapToDouble(item -> item.getItemTotalExclTax() != null ? item.getItemTotalExclTax() : 0.0)
                    .sum();

            double totalInclTax = items.stream()
                    .mapToDouble(item -> item.getItemTotalInclTax() != null ? item.getItemTotalInclTax() : 0.0)
                    .sum();

            CurrencyTotals totals = CurrencyCalculator.calculateTotals(
                    invoiceUpdateDTO.getInvoiceCurrency(),
                    totalExclTax,
                    totalInclTax,
                    invoice.getAppliedExchangeRate()
            );


            invoice.setTotalExclTaxEUR(totals.totalExclTaxEUR());
            invoice.setTotalInclTaxEUR(totals.totalInclTaxEUR());
            invoice.setTotalExclTaxTND(totals.totalExclTaxTND());
            invoice.setTotalInclTaxTND(totals.totalExclTaxTND());

            return invoice;

        } catch (Exception exception) {
            throw BillingException.badRequest(exception.getMessage());
        }
    }

    public InvoiceSummaryDTO toSummaryDTO(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        return InvoiceSummaryDTO.builder()
                .idInvoice(invoice.getIdInvoice())
                .invoiceNumber(invoice.getReference())
                .issueDate(invoice.getIssueDate())
                .invoiceType(invoice.getInvoiceType())
                .invoiceStatus(invoice.getInvoiceStatus())
                .invoiceComplianceStatus(invoice.getInvoiceComplianceStatus())
                .invoiceCurrency(invoice.getCurrency())
                .totalExclTaxEUR(invoice.getTotalExclTaxEUR())
                .totalInclTaxEUR(invoice.getTotalInclTaxEUR())
                .totalExclTaxTND(invoice.getTotalExclTaxTND())
                .totalInclTaxTND(invoice.getTotalInclTaxTND())
                .build();
    }

    public Partner getPartner(InvoiceType invoiceType, String idPartner){
        if(invoiceType == InvoiceType.PURCHASE){
            return partnerUseCase.getSupplierById(idPartner).get();
        }
        if (invoiceType == InvoiceType.SALE){
            return partnerUseCase.findCustomerById(idPartner).get();
        }
        else {
            throw BillingException.notFound("Partner ", idPartner);
        }
    }

    private InvoiceEntity createEntityByInvoiceType(InvoiceType invoiceType) {
        if (invoiceType == null) {
            throw new IllegalArgumentException("PartnerType must not be null");
        }

        return switch (invoiceType) {
            case SALE -> new ClientInvoiceEntity();
            case PURCHASE -> new SupplierInvoiceEntity();
        };
    }

}
