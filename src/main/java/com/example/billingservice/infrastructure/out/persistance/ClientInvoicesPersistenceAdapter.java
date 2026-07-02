package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.Utils.StatusMapper;
import com.example.billingservice.application.ports.in.CurrencyConversionUseCase;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.domain.enums.*;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;

import com.example.billingservice.domain.model.InvoiceItem;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceEntity;
import com.example.billingservice.infrastructure.out.persistance.entity.ClientInvoiceEntity;

import com.example.billingservice.infrastructure.out.persistance.entity.InvoiceItemEntity;

import com.example.billingservice.infrastructure.out.persistance.mapper.InvoiceMapper;
import com.example.billingservice.infrastructure.out.persistance.projections.ClientInvoiceDashboardStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.repository.ClientInvoicesRepository;
import com.example.billingservice.infrastructure.out.persistance.repository.InvoiceItemRepository;
import com.example.billingservice.infrastructure.out.persistance.projections.PartnerInvoiceAmountStatsProjection;
import com.example.billingservice.infrastructure.out.persistance.projections.PartnerInvoiceCountStatsProjection;
import com.example.billingservice.shared.StatsHelper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ClientInvoicesPersistenceAdapter implements ClientInvoicesRepositoryPort {

    private final ClientInvoicesRepository clientInvoicesRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceMapper invoiceMapper;
    private final CurrencyConversionUseCase currencyConversionUseCase;

    @Override
    public Page<InvoicePageItemDTO> findAllInvoices(String keyword, InvoiceStatus status, int page, InvoiceType type) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("issueDate").descending());
            Page<InvoiceEntity> entities = clientInvoicesRepository.getInvoices(keyword, status, pageRequest);
            List<InvoicePageItemDTO> invoices = entities.getContent()
                    .stream()
                    .map(invoiceEntity -> invoiceMapper.toDomain(invoiceEntity, InvoiceType.SALE))
                    .map(invoiceMapper::toInvoicePageItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(invoices, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }
    }

    @Override
    public List<InvoicePageItemDTO> getClientTopInvoices(UUID idClient) {
        List<ClientInvoiceEntity> clientInvoiceEntities = clientInvoicesRepository
                .findTop3ByPartner_IdPartnerAndInvoiceStatusNotInOrderByIssueDateDesc(
                        idClient,
                        List.of(InvoiceStatus.DRAFT, InvoiceStatus.CANCELLED)
                );
        List<InvoicePageItemDTO> invoicePageItemDTOS =
                clientInvoiceEntities.stream()
                        .map(i -> invoiceMapper.toDomain(i, InvoiceType.SALE))
                        .map(invoiceMapper::toInvoicePageItemDTO)
                        .toList();

        return invoicePageItemDTOS;
    }

    @Override
    public List<InvoicePageItemDTO> getOverdueInvoices(Date date) {
        List<ClientInvoiceEntity> clientInvoiceEntities = clientInvoicesRepository.getOverdueInvoices(date);
        List<InvoicePageItemDTO> invoicePageItemDTOS = clientInvoiceEntities.stream()
                .map(clientInvoiceEntity -> invoiceMapper.toDomain(clientInvoiceEntity, InvoiceType.SALE))
                .map(invoiceMapper::toInvoicePageItemDTO)
                .toList();
        return invoicePageItemDTOS;
    }

    @Override
    @Transactional
    public InvoiceDTO save(Invoice invoice) {
        ClientInvoiceEntity entity = (ClientInvoiceEntity) invoiceMapper.toEntity(invoice);
        ClientInvoiceEntity savedEntity = clientInvoicesRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity, invoice.getInvoiceType());
        List<InvoiceItemEntity> invoiceItemEntities =  entity.getInvoiceItems();
        invoiceItemEntities.forEach(
                invoiceItemEntity -> invoiceItemEntity.setInvoice(savedEntity)
        );
        invoiceItemRepository.saveAll(invoiceItemEntities);


        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO update(Invoice invoice) {
        ClientInvoiceEntity entity = (ClientInvoiceEntity) invoiceMapper.toEntity(invoice);
        entity.setIdInvoice(invoice.getIdInvoice());
        InvoiceEntity savedEntity = clientInvoicesRepository.save(entity);
        Invoice invoice1 = invoiceMapper.toDomain(savedEntity, invoice.getInvoiceType());/*
        entity.getInvoiceEvents().forEach(
                invoiceEventEntity -> invoiceEventEntity.setInvoice(savedEntity)
        );
        jpaInvoiceEventRepository.saveAll(entity.getInvoiceEvents());*/

        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO updateStatus(UUID invoiceId, InvoiceStatus newStatus) {

        ClientInvoiceEntity entity =
                clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(invoiceId);


        entity.setInvoiceStatus(newStatus);


        ClientInvoiceEntity saved = clientInvoicesRepository.save(entity);


        return invoiceMapper.toDTO(
                invoiceMapper.toDomain(saved, InvoiceType.SALE)
        );
    }

    @Override
    public InvoiceDTO updateRemainingAmount(UUID invoiceId, double amount) {
        ClientInvoiceEntity entity =
                clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(invoiceId);

        // Round to 2 decimal places
        BigDecimal rounded = BigDecimal.valueOf(Math.abs(amount))
                .setScale(2, RoundingMode.HALF_UP);

        System.out.println("rounded remainingAmount: "+rounded.doubleValue());

        entity.setRemainingAmount(rounded.doubleValue());


        ClientInvoiceEntity saved = clientInvoicesRepository.save(entity);


        return invoiceMapper.toDTO(
                invoiceMapper.toDomain(saved, InvoiceType.SALE)
        );
    }

    @Override
    public InvoiceDTO getById(UUID idInvoice) {
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(idInvoice);
        Invoice invoice = invoiceMapper.toDomain(entity, InvoiceType.SALE);

        return invoiceMapper.toDTO(invoice);
    }

    @Override
    public InvoicePageItemDTO getInvoiceItemById(UUID idInvoice) {
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(idInvoice);
        Invoice invoice = invoiceMapper.toDomain(entity, InvoiceType.SALE);

        return invoiceMapper.toInvoicePageItemDTO(invoice);
    }

    @Override
    public Invoice getInvoice(UUID idInvoice) {
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(idInvoice);
        return invoiceMapper.toDomain(entity, InvoiceType.SALE);
    }

    @Override
    public InvoicesStatsResponse getClientsInvoicesStats(int year) {
        return null;
    }

    @Override
    public ConvertedInvoiceStats getClientInvoiceStats(UUID idPartner) {

        BigDecimal totalAmountTND = BigDecimal.ZERO;
        BigDecimal pendingAmountTND = BigDecimal.ZERO;
        BigDecimal totalAmountEUR = BigDecimal.ZERO;
        BigDecimal pendingAmountEUR = BigDecimal.ZERO;
        BigDecimal totalAmountUSD = BigDecimal.ZERO;
        BigDecimal pendingAmountUSD = BigDecimal.ZERO;


        PartnerInvoiceCountStatsProjection countStats =
                clientInvoicesRepository.getPartnerInvoiceCountStats(
                        idPartner,
                        InvoiceStatus.TO_COLLECT
                );

        List<PartnerInvoiceAmountStatsProjection> statsByCurrency =
                clientInvoicesRepository.getPartnerInvoiceAmountStatsGroupedByCurrency(
                        idPartner,
                        InvoiceStatus.OVERDUE
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
    public List<ClientInvoiceDashboardStatsMultiCurrencyDTO> getClientInvoicesDashboardStats(int year) {


        List<ClientInvoiceDashboardStatsMultiCurrencyDTO> clientInvoiceDashboardStatsMultiCurrencyDTOS = new ArrayList<>();
        List<ClientInvoiceDashboardStatsProjection> clientInvoiceDashboardStatsProjections =  clientInvoicesRepository.getAllClientInvoiceAmountStatsGroupedByCurrencyAndClientAndMonth(year);

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
    public ConvertedInvoiceStats getAllClientInvoiceCountStats(InvoiceStatus pendingStatus) {
        BigDecimal totalAmountTND = BigDecimal.ZERO;
        BigDecimal pendingAmountTND = BigDecimal.ZERO;
        BigDecimal totalAmountEUR = BigDecimal.ZERO;
        BigDecimal pendingAmountEUR = BigDecimal.ZERO;
        BigDecimal totalAmountUSD = BigDecimal.ZERO;
        BigDecimal pendingAmountUSD = BigDecimal.ZERO;


        PartnerInvoiceCountStatsProjection countStats =
                clientInvoicesRepository.getAllClientInvoiceCountStats(
                        InvoiceStatus.TO_COLLECT
                );

        List<PartnerInvoiceAmountStatsProjection> statsByCurrency =
                clientInvoicesRepository.getAllClientInvoiceAmountStatsGroupedByCurrency(
                        InvoiceStatus.TO_COLLECT
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

        return StatsHelper.getStats(totalAmountTND,pendingAmountTND,
                totalAmountEUR,pendingAmountEUR,
                totalAmountUSD,pendingAmountUSD,
                countStats);
    }

    @Override
    public List<InvoicePageItemDTO> getInvoicesToPay(String keyword) {
        List<ClientInvoiceEntity> entities = clientInvoicesRepository.getInvoicesToPay(keyword);

        return entities.stream()
                .map(entity-> invoiceMapper.toInvoicePageItemDTO(invoiceMapper.toDomain(entity, InvoiceType.SALE)))
                .toList();
    }

    @Override
    public void delete(UUID idInvoice) {
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(idInvoice);
        if(entity.getInvoiceStatus()!=InvoiceStatus.DRAFT){
            entity.setInvoiceStatus(InvoiceStatus.CANCELLED);
            clientInvoicesRepository.save(entity);
        }
        else {
            clientInvoicesRepository.delete(entity);
        }
    }

    @Override
    public boolean existsByInvoiceNumber(String invoiceNumber) {
        return clientInvoicesRepository.existsByReference(invoiceNumber);
    }

    @Override
    public boolean existsByInvoiceId(UUID invoiceId) {
        return clientInvoicesRepository.existsByIdInvoice(invoiceId);
    }

    @Override
    public boolean existsByPurchaseOrderId(UUID purchaseOrderId) {
        return clientInvoicesRepository.existsByPurchaseOrderIdPurchaseOrder(purchaseOrderId);
    }

    @Override
    public List<ClientRevenueStats> getClientRevenueByPeriod(UUID idPartner, String period) {

        LocalDateTime dateFin   = LocalDateTime.now();
        LocalDateTime dateDebut = dateFin.minusMonths(Long.parseLong(period));

        List<ClientInvoiceEntity> invoices = clientInvoicesRepository
                .getClientInvoicesByPeriod(idPartner, dateDebut, dateFin , InvoiceStatus.PAID);

        // Grouper les factures par mois
        Map<YearMonth, List<ClientInvoiceEntity>> facturesParMois = invoices.stream()
                .collect(Collectors.groupingBy(
                        invoice -> YearMonth.from(invoice.getCreatedAt())
                ));

        List<ClientRevenueStats> stats = new ArrayList<>();

        // Itérer sur chaque mois de la période
        YearMonth moisDebut = YearMonth.from(dateDebut);
        YearMonth moisFin   = YearMonth.from(dateFin);

        YearMonth current = moisDebut;
        while (!current.isAfter(moisFin)) {

            List<ClientInvoiceEntity> invoicesThisMonth = facturesParMois
                    .getOrDefault(current, Collections.emptyList());

            // Calculer HT et TTC pour ce mois
            double paidHT = 0.0, paidTTC = 0.0;
            double overdueHT = 0.0, overdueTTC = 0.0;

            for (ClientInvoiceEntity invoice : invoicesThisMonth) {
                List<InvoiceItemEntity> items = invoiceItemRepository
                        .findByInvoice_IdInvoice(invoice.getIdInvoice());

                for (InvoiceItemEntity item : items) {
                    double ht = item.getUnityPriceEXclTax() * item.getQuantity();
                    double ttc = item.getTotalPriceIncTax();

                    if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
                        paidHT  += ht;
                        paidTTC += ttc;
                    } else if (invoice.getInvoiceStatus() == InvoiceStatus.OVERDUE) {
                        overdueHT  += ht;
                        overdueTTC += ttc;
                    }
                }
            }

            ClientRevenueStats dto = new ClientRevenueStats();
            dto.setPeriod(current.toString());                                              // "2024-01"
            dto.setMonthLabel(current.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH))); // "janvier 2024"
            // Paid totals
            dto.setRevenueHT(paidHT);
            dto.setRevenueTTC(paidTTC);
            dto.setRevenueTVA(paidTTC - paidHT);

            // Overdue totals
            dto.setOverdueHT(overdueHT);
            dto.setOverdueTTC(overdueTTC);
            dto.setOverdueTVA(overdueTTC - overdueHT);

            dto.setNombreFactures(invoicesThisMonth.size());

            stats.add(dto);
            current = current.plusMonths(1);
        }

        return stats;
    }

    @Override
    public List<ClientRevenueStats> getAllClientRevenueByPeriod( String period) {
        LocalDateTime dateFin   = LocalDateTime.now();
        LocalDateTime dateDebut = dateFin.minusMonths(Long.parseLong(period));

        List<ClientInvoiceEntity> invoices = clientInvoicesRepository
                .getAllClientInvoicesByPeriod( dateDebut, dateFin,InvoiceStatus.PAID);

        // Grouper les factures par mois
        Map<YearMonth, List<ClientInvoiceEntity>> facturesParMois = invoices.stream()
                .collect(Collectors.groupingBy(
                        invoice -> YearMonth.from(invoice.getCreatedAt())
                ));

        List<ClientRevenueStats> stats = new ArrayList<>();

        // Itérer sur chaque mois de la période
        YearMonth moisDebut = YearMonth.from(dateDebut);
        YearMonth moisFin   = YearMonth.from(dateFin);

        YearMonth current = moisDebut;
        while (!current.isAfter(moisFin)) {

            List<ClientInvoiceEntity> facturesDuMois = facturesParMois
                    .getOrDefault(current, Collections.emptyList());

            // Calculer HT et TTC pour ce mois
            double totalHT  = 0.0;
            double totalTTC = 0.0;

            for (ClientInvoiceEntity invoice : facturesDuMois) {
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
    public Page<InvoicePageItemDTO> getClientInvoices(UUID idPartner, int page) {
        try {

            PageRequest pageRequest = PageRequest.of(page, 5, Sort.by("issueDate").descending());
            Page<InvoiceEntity> entities = clientInvoicesRepository.getClientInvoicesByPartner(idPartner, pageRequest);

            List<InvoicePageItemDTO> invoices = entities.getContent()
                    .stream()
                    .map(invoiceEntity -> invoiceMapper.toDomain(invoiceEntity, InvoiceType.SALE))
                    .map(invoiceMapper::toInvoicePageItemDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(invoices, pageRequest, entities.getTotalElements());

        } catch (DataAccessException ex) {
            throw BillingException.internalError("Erreur de fetch des factures: " + ex.getMessage());
        }
    }


    public LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.of("Africa/Tunis"))
                .toLocalDate();
    }

}
