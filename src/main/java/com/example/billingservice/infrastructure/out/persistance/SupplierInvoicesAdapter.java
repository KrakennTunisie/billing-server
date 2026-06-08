package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.Utils.StatusMapper;
import com.example.billingservice.application.ports.in.CurrencyConversionUseCase;
import com.example.billingservice.application.ports.out.SupplierInvoicesRepositoryPort;
import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;

import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.entity.ClientInvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;

import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.SupplierInvoiceEntity;

import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceMapper;
import com.example.billingservice.infrastructure.out.persistance.projections.ClientInvoiceDashboardStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.projections.PartnerInvoiceAmountStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.projections.PartnerInvoiceCountStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.repository.InvoiceItemRepository;
import com.example.billingservice.infrastructure.out.persistance.repository.SupplierInvoicesRepository;
import com.example.billingservice.shared.StatsHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SupplierInvoicesAdapter implements SupplierInvoicesRepositoryPort {

    private final SupplierInvoicesRepository supplierInvoicesRepository;
    private final InvoiceMapper invoiceMapper;
    private final CurrencyConversionUseCase currencyConversionUseCase;
    private final InvoiceItemRepository invoiceItemRepository;

    @Override
    public Page<InvoicePageItemDTO> findAllInvoices(String keyword, InvoiceStatus status, int page, InvoiceType type) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("issueDate").descending());
            Page<InvoiceEntity> entities = supplierInvoicesRepository.getInvoices(keyword, status, pageRequest);

            List<InvoicePageItemDTO> invoices = entities.getContent()
                    .stream()
                    .map(invoiceEntity -> invoiceMapper.toDomain(invoiceEntity, InvoiceType.PURCHASE))
                    .map(invoiceMapper::toInvoicePageItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(invoices, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }
    }

    @Override
    public List<InvoicePageItemDTO> getSupplierTopInvoices(UUID idSupplier) {
        List<SupplierInvoiceEntity> supplierInvoiceEntities = supplierInvoicesRepository
                .findTop3ByPartner_IdPartnerAndInvoiceStatusNotInOrderByIssueDateDesc(
                        idSupplier,
                        List.of(InvoiceStatus.DRAFT, InvoiceStatus.CANCELLED)
                );
        List<InvoicePageItemDTO> invoicePageItemDTOS =
                supplierInvoiceEntities.stream()
                        .map(i -> invoiceMapper.toDomain(i, InvoiceType.PURCHASE))
                        .map(invoiceMapper::toInvoicePageItemDTO)
                        .toList();
        return invoicePageItemDTOS;
    }

    @Override
    public InvoiceDTO save(Invoice invoice) {
        SupplierInvoiceEntity entity = (SupplierInvoiceEntity) invoiceMapper.toEntity(invoice);
        SupplierInvoiceEntity savedEntity = supplierInvoicesRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity, invoice.getInvoiceType());



        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO update(Invoice invoice) {
        SupplierInvoiceEntity entity = (SupplierInvoiceEntity) invoiceMapper.toEntity(invoice);
        entity.setIdInvoice(invoice.getIdInvoice());
        InvoiceEntity savedEntity = supplierInvoicesRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity, invoice.getInvoiceType());/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/

        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO updateStatus(UUID invoiceId, InvoiceStatus newStatus) {

        SupplierInvoiceEntity entity = supplierInvoicesRepository.getSupplierInvoiceEntityByIdInvoice(invoiceId);

        entity.setInvoiceStatus(newStatus);

        SupplierInvoiceEntity saved = supplierInvoicesRepository.save(entity);


        return invoiceMapper.toDTO(
                invoiceMapper.toDomain(saved, InvoiceType.PURCHASE)
        );
    }

    @Override
    public InvoiceDTO getById(UUID idInvoice) {
        SupplierInvoiceEntity entity = supplierInvoicesRepository.getSupplierInvoiceEntityByIdInvoice(idInvoice);
        Invoice invoice = invoiceMapper.toDomain(entity, InvoiceType.PURCHASE);

        return invoiceMapper.toDTO(invoice);    }

    @Override
    public InvoiceDTO getInvoiceByInvoiceNumber(String invoiceNumber) {
        SupplierInvoiceEntity supplierInvoiceEntity = supplierInvoicesRepository.getSupplierInvoiceEntityByReference(invoiceNumber);
        Invoice invoice = invoiceMapper.toDomain(supplierInvoiceEntity, InvoiceType.PURCHASE);
        return invoiceMapper.toDTO(invoice);
    }

    @Override
    public Invoice getInvoice(UUID idInvoice) {
        SupplierInvoiceEntity entity = supplierInvoicesRepository.getSupplierInvoiceEntityByIdInvoice(idInvoice);
        return invoiceMapper.toDomain(entity, InvoiceType.PURCHASE);
    }

    @Override
    public InvoicesStatsResponse getSuppliersInvoicesStats(int year) {
        return null;
    }

    @Override
    public ConvertedInvoiceStats getSupplierInvoicesStats(UUID idPartner) {
        BigDecimal totalAmountTND = BigDecimal.ZERO;
        BigDecimal pendingAmountTND = BigDecimal.ZERO;
        BigDecimal totalAmountEUR = BigDecimal.ZERO;
        BigDecimal pendingAmountEUR = BigDecimal.ZERO;
        BigDecimal totalAmountUSD = BigDecimal.ZERO;
        BigDecimal pendingAmountUSD = BigDecimal.ZERO;


        PartnerInvoiceCountStatsProjection countStats =
                supplierInvoicesRepository.getPartnerInvoiceCountStats(
                        idPartner,
                        InvoiceStatus.TO_PAY
                );

        List<PartnerInvoiceAmountStatsProjection> statsByCurrency =
                supplierInvoicesRepository.getPartnerInvoiceAmountStatsGroupedByCurrency(
                        idPartner,
                        InvoiceStatus.TO_PAY
                );

        System.out.println("statsByCurrency: "+statsByCurrency);
        for (PartnerInvoiceAmountStatsProjection row : statsByCurrency) {

            BigDecimal totalAmount = row.getTotalAmount() != null
                    ? row.getTotalAmount()
                    : BigDecimal.ZERO;

            BigDecimal pendingAmount = row.getPendingAmount() != null
                    ? row.getPendingAmount()
                    : BigDecimal.ZERO;
            totalAmountTND = totalAmountTND.add(totalAmount);
            pendingAmountTND = pendingAmountTND.add(pendingAmount);

            if(row.getInvoiceCurrency()== InvoiceCurrency.EUR){
                BigDecimal usdToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.USD.name(),convertToLocalDate(row.getExchangeRateReferenceDate())).getQuote();
                totalAmountEUR = totalAmountEUR.add(totalAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                pendingAmountEUR = pendingAmountEUR.add(pendingAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                totalAmountUSD = totalAmountUSD.add(totalAmount.multiply(usdToTndquote));
                pendingAmountUSD = pendingAmountUSD.add(pendingAmount.multiply(usdToTndquote));

            }

            if(row.getInvoiceCurrency()==InvoiceCurrency.USD){
                BigDecimal euroToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.EUR.name(),convertToLocalDate(row.getExchangeRateReferenceDate())).getQuote();
                totalAmountUSD = totalAmountUSD.add(totalAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                pendingAmountUSD = pendingAmountUSD.add(pendingAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                totalAmountEUR = totalAmountEUR.add(totalAmount.multiply(euroToTndquote));
                pendingAmountEUR = pendingAmountEUR.add(pendingAmount.multiply(euroToTndquote));

            }
            if(row.getInvoiceCurrency() == InvoiceCurrency.TND){
                BigDecimal euroToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.EUR.name(),convertToLocalDate(row.getExchangeRateReferenceDate())).getQuote();
                BigDecimal usdToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.USD.name(),convertToLocalDate(row.getExchangeRateReferenceDate())).getQuote();
                totalAmountUSD = totalAmountUSD.add(totalAmount.multiply(usdToTndquote));
                pendingAmountUSD = pendingAmountUSD.add(pendingAmount.multiply(usdToTndquote));
                totalAmountEUR = totalAmountEUR.add(totalAmount.multiply(euroToTndquote));
                pendingAmountEUR = pendingAmountEUR.add(pendingAmount.multiply(euroToTndquote));

            }
        }

        System.out.println("totalAmountTND: "+totalAmountTND);
        System.out.println("pendingAmountTND: "+pendingAmountTND);
        System.out.println("totalAmountTND: "+totalAmountEUR);
        System.out.println("pendingAmountTND: "+pendingAmountEUR);
        System.out.println("totalAmountTND: "+totalAmountUSD);
        System.out.println("pendingAmountTND: "+pendingAmountUSD);

        return StatsHelper.getStats(totalAmountTND,pendingAmountTND,
                totalAmountEUR,pendingAmountEUR,
                totalAmountUSD,pendingAmountUSD,
                countStats);
    }

    @Override
    public List<ClientInvoiceDashboardStatsMultiCurrencyDTO> getSupplierInvoicesDashboardStats(int year) {

        List<ClientInvoiceDashboardStatsMultiCurrencyDTO> clientInvoiceDashboardStatsMultiCurrencyDTOS = new ArrayList<>();
        List<ClientInvoiceDashboardStatsProjection> clientInvoiceDashboardStatsProjections =  supplierInvoicesRepository.getAllClientInvoiceAmountStatsGroupedByCurrencyAndClientAndMonth(year);

        System.out.println("clientInvoiceDashboardStatsProjections: "+clientInvoiceDashboardStatsProjections);

        for (ClientInvoiceDashboardStatsProjection row : clientInvoiceDashboardStatsProjections) {
            BigDecimal totalAmountTND = BigDecimal.ZERO;
            BigDecimal totalAmountEUR = BigDecimal.ZERO;
            BigDecimal totalAmountUSD = BigDecimal.ZERO;
            BigDecimal totalAmount = row.getAmount() != null
                    ? row.getAmount()
                    : BigDecimal.ZERO;

            totalAmountTND = totalAmountTND.add(totalAmount);

            if(row.getInvoiceCurrency()==InvoiceCurrency.EUR){
                BigDecimal usdToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.USD.name(),row.getExchangeRateReferenceDate()).getQuote();
                totalAmountEUR = totalAmountEUR.add(totalAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                totalAmountUSD = totalAmountUSD.add(totalAmount.multiply(usdToTndquote));

            }

            if(row.getInvoiceCurrency()==InvoiceCurrency.USD){
                BigDecimal euroToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.EUR.name(),row.getExchangeRateReferenceDate()).getQuote();
                totalAmountUSD = totalAmountUSD.add(totalAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                totalAmountEUR = totalAmountEUR.add(totalAmount.multiply(euroToTndquote));

            }
            if(row.getInvoiceCurrency() == InvoiceCurrency.TND){
                BigDecimal euroToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.EUR.name(),row.getExchangeRateReferenceDate()).getQuote();
                BigDecimal usdToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.USD.name(),row.getExchangeRateReferenceDate()).getQuote();
                totalAmountUSD = totalAmountUSD.add(totalAmount.multiply(usdToTndquote));
                totalAmountEUR = totalAmountEUR.add(totalAmount.multiply(euroToTndquote));

                System.out.println("totalAmount.multiply(euroToTndquote) in TND: "+ totalAmount.multiply(euroToTndquote));

                System.out.println("euroToTndquote in TND: "+ euroToTndquote);
                System.out.println("usdToTndquote in TND: "+ usdToTndquote);
                System.out.println("totalAmountEUR in TND: "+ totalAmountEUR);
            }

            clientInvoiceDashboardStatsMultiCurrencyDTOS.add(StatsHelper.getDetailedStats(row, totalAmountTND, totalAmountEUR, totalAmountUSD));
        }

        return  clientInvoiceDashboardStatsMultiCurrencyDTOS;
    }

    @Override
    public ConvertedInvoiceStats getAllSupplierInvoiceCountStats(InvoiceStatus pendingStatus) {
        BigDecimal totalAmountTND = BigDecimal.ZERO;
        BigDecimal pendingAmountTND = BigDecimal.ZERO;
        BigDecimal totalAmountEUR = BigDecimal.ZERO;
        BigDecimal pendingAmountEUR = BigDecimal.ZERO;
        BigDecimal totalAmountUSD = BigDecimal.ZERO;
        BigDecimal pendingAmountUSD = BigDecimal.ZERO;


        PartnerInvoiceCountStatsProjection countStats =
                supplierInvoicesRepository.getAllClientInvoiceCountStats(
                        InvoiceStatus.TO_PAY
                );

        List<PartnerInvoiceAmountStatsProjection> statsByCurrency =
                supplierInvoicesRepository.getAllClientInvoiceAmountStatsGroupedByCurrency(
                        InvoiceStatus.TO_PAY
                );

        System.out.println("statsByCurrency: "+statsByCurrency);
        for (PartnerInvoiceAmountStatsProjection row : statsByCurrency) {

            BigDecimal totalAmount = row.getTotalAmount() != null
                    ? row.getTotalAmount()
                    : BigDecimal.ZERO;

            BigDecimal pendingAmount = row.getPendingAmount() != null
                    ? row.getPendingAmount()
                    : BigDecimal.ZERO;
            totalAmountTND = totalAmountTND.add(totalAmount);
            pendingAmountTND = pendingAmountTND.add(pendingAmount);

            if(row.getInvoiceCurrency()==InvoiceCurrency.EUR){
                BigDecimal usdToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.USD.name(),convertToLocalDate(row.getExchangeRateReferenceDate())).getQuote();
                totalAmountEUR = totalAmountEUR.add(totalAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                pendingAmountEUR = pendingAmountEUR.add(pendingAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                totalAmountUSD = totalAmountUSD.add(totalAmount.multiply(usdToTndquote));
                pendingAmountUSD = pendingAmountUSD.add(pendingAmount.multiply(usdToTndquote));

            }

            if(row.getInvoiceCurrency()==InvoiceCurrency.USD){
                BigDecimal euroToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.EUR.name(),convertToLocalDate(row.getExchangeRateReferenceDate())).getQuote();
                totalAmountUSD = totalAmountUSD.add(totalAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                pendingAmountUSD = pendingAmountUSD.add(pendingAmount.multiply(BigDecimal.valueOf(row.getAppliedExchangeRate())));
                totalAmountEUR = totalAmountEUR.add(totalAmount.multiply(euroToTndquote));
                pendingAmountEUR = pendingAmountEUR.add(pendingAmount.multiply(euroToTndquote));

            }
            if(row.getInvoiceCurrency() == InvoiceCurrency.TND){
                BigDecimal euroToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.EUR.name(),convertToLocalDate(row.getExchangeRateReferenceDate())).getQuote();
                BigDecimal usdToTndquote = currencyConversionUseCase.convert(InvoiceCurrency.TND.name(), InvoiceCurrency.USD.name(),convertToLocalDate(row.getExchangeRateReferenceDate())).getQuote();
                totalAmountUSD = totalAmountUSD.add(totalAmount.multiply(usdToTndquote));
                pendingAmountUSD = pendingAmountUSD.add(pendingAmount.multiply(usdToTndquote));
                totalAmountEUR = totalAmountEUR.add(totalAmount.multiply(euroToTndquote));
                pendingAmountEUR = pendingAmountEUR.add(pendingAmount.multiply(euroToTndquote));

            }
        }

        System.out.println("totalAmountTND: "+totalAmountTND);
        System.out.println("pendingAmountTND: "+pendingAmountTND);
        System.out.println("totalAmountTND: "+totalAmountEUR);
        System.out.println("pendingAmountTND: "+pendingAmountEUR);
        System.out.println("totalAmountTND: "+totalAmountUSD);
        System.out.println("pendingAmountTND: "+pendingAmountUSD);

        return StatsHelper.getStats(totalAmountTND,pendingAmountTND,
                totalAmountEUR,pendingAmountEUR,
                totalAmountUSD,pendingAmountUSD,
                countStats);
    }

    @Override
    public void delete(UUID idInvoice) {
        SupplierInvoiceEntity entity = supplierInvoicesRepository.getSupplierInvoiceEntityByIdInvoice(idInvoice);
        if(entity.getInvoiceStatus()!=InvoiceStatus.DRAFT){
            entity.setInvoiceStatus(InvoiceStatus.CANCELLED);
        }
        else {
            supplierInvoicesRepository.delete(entity);
        }
    }

    @Override
    public boolean existsByInvoiceNumber(String invoiceNumber) {
        return supplierInvoicesRepository.existsByReference(invoiceNumber);
    }

    @Override
    public boolean existsByInvoiceId(UUID invoiceId) {
        return supplierInvoicesRepository.existsByIdInvoice(invoiceId);
    }

    @Override
    public List<SummaryInvoiceDTO> getSupplierInvoices(UUID idpartner) {
         List <SupplierInvoiceEntity> supplierInvoices = supplierInvoicesRepository.getAllSupplierInvoices(idpartner, PageRequest.of(0, 3));
         return  supplierInvoices.stream().map(invoice-> invoiceMapper.toInvoicePageItemDTO(invoice)).toList();

    }

    @Override
    public List<ClientRevenueStats> getSupplierDespensesByPeriod(UUID idPartner, String period) {
        LocalDateTime dateFin   = LocalDateTime.now();
        LocalDateTime dateDebut = dateFin.minusMonths(Long.parseLong(period));

        List<SupplierInvoiceEntity> invoices = supplierInvoicesRepository
                .getSupplierInvoicesByPeriod(idPartner, dateDebut, dateFin);

        // Grouper les factures par mois
        Map<YearMonth, List<SupplierInvoiceEntity>> facturesParMois = invoices.stream()
                .collect(Collectors.groupingBy(
                        invoice -> YearMonth.from(invoice.getCreatedAt())
                ));

        List<ClientRevenueStats> stats = new ArrayList<>();

        // Itérer sur chaque mois de la période
        YearMonth moisDebut = YearMonth.from(dateDebut);
        YearMonth moisFin   = YearMonth.from(dateFin);

        YearMonth current = moisDebut;
        while (!current.isAfter(moisFin)) {

            List<SupplierInvoiceEntity> facturesDuMois = facturesParMois
                    .getOrDefault(current, Collections.emptyList());

            // Calculer HT et TTC pour ce mois
            double totalHT  = 0.0;
            double totalTTC = 0.0;

            for (SupplierInvoiceEntity invoice : facturesDuMois) {
                List<InvoiceItemEntity> items = invoiceItemRepository
                        .findByInvoice_IdInvoice(invoice.getIdInvoice());

                for (InvoiceItemEntity item : items) {
                    totalTTC += item.getTotalPriceIncTax();
                    totalHT  += item.getUnityPriceEXclTax() * item.getQuantity();
                }
            }

            double totalTVA = totalTTC - totalHT;

            ClientRevenueStats dto = new ClientRevenueStats();
            dto.setPeriod(current.toString());
            dto.setMonthLabel(current.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)));
            dto.setRevenueHT(totalHT);
            dto.setRevenueTVA(totalTVA);
            dto.setRevenueTTC(totalTTC);
            dto.setNombreFactures(facturesDuMois.size());

            stats.add(dto);
            current = current.plusMonths(1);
        }

        return stats;
    }

    @Override
    public List<ClientRevenueStats> getAllSupplierDespensesByPeriod(UUID idPartner, String period) {
        LocalDateTime dateFin   = LocalDateTime.now();
        LocalDateTime dateDebut = dateFin.minusMonths(Long.parseLong(period));

        List<SupplierInvoiceEntity> invoices = supplierInvoicesRepository
                .getAllSupplierInvoicesByPeriod(idPartner, dateDebut, dateFin);

        // Grouper les factures par mois
        Map<YearMonth, List<SupplierInvoiceEntity>> facturesParMois = invoices.stream()
                .collect(Collectors.groupingBy(
                        invoice -> YearMonth.from(invoice.getCreatedAt())
                ));

        List<ClientRevenueStats> stats = new ArrayList<>();

        // Itérer sur chaque mois de la période
        YearMonth moisDebut = YearMonth.from(dateDebut);
        YearMonth moisFin   = YearMonth.from(dateFin);

        YearMonth current = moisDebut;
        while (!current.isAfter(moisFin)) {

            List<SupplierInvoiceEntity> facturesDuMois = facturesParMois
                    .getOrDefault(current, Collections.emptyList());

            // Calculer HT et TTC pour ce mois
            double totalHT  = 0.0;
            double totalTTC = 0.0;

            for (SupplierInvoiceEntity invoice : facturesDuMois) {
                List<InvoiceItemEntity> items = invoiceItemRepository
                        .findByInvoice_IdInvoice(invoice.getIdInvoice());

                for (InvoiceItemEntity item : items) {
                    totalTTC += item.getTotalPriceIncTax();
                    totalHT  += item.getUnityPriceEXclTax() * item.getQuantity();
                }
            }

            double totalTVA = totalTTC - totalHT;

            ClientRevenueStats dto = new ClientRevenueStats();
            dto.setPeriod(current.toString());                                              // "2024-01"
            dto.setMonthLabel(current.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH))); // "janvier 2024"
            dto.setRevenueHT(totalHT);
            dto.setRevenueTVA(totalTVA);
            dto.setRevenueTTC(totalTTC);
            dto.setNombreFactures(facturesDuMois.size());

            stats.add(dto);
            current = current.plusMonths(1);
        }

        return stats;
    }


    public LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.of("Europe/Paris"))
                .toLocalDate();
    }
}
