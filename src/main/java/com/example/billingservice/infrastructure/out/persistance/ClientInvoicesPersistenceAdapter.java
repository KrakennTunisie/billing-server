package com.example.billingservice.infrastructure.out.persistance;

import com.example.billingservice.application.ports.in.CurrencyConversionUseCase;
import com.example.billingservice.application.ports.out.ClientInvoicesRepositoryPort;
import com.example.billingservice.domain.enums.InvoiceCurrency;
import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.InvoiceType;
import com.example.billingservice.domain.exceptions.BillingException;
import com.example.billingservice.domain.model.Invoice;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        }    }

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
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(invoiceId);
        entity.setInvoiceStatus(newStatus);
        Invoice invoice1 = invoiceMapper.toDomain(clientInvoicesRepository.save(entity), InvoiceType.SALE);

        return  invoiceMapper.toDTO(invoice1);
    }

    @Override
    public InvoiceDTO getById(UUID idInvoice) {
        ClientInvoiceEntity entity = clientInvoicesRepository.getClientInvoiceEntityByIdInvoice(idInvoice);
        Invoice invoice = invoiceMapper.toDomain(entity, InvoiceType.SALE);

        return invoiceMapper.toDTO(invoice);    }

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




    public LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.of("Europe/Paris"))
                .toLocalDate();
    }

}
