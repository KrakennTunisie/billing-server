package com.example.billingservice.application.ports.in;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.model.Invoice;
import com.example.billingservice.infrastructure.out.persistance.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface InvoiceUseCase {
    Page<InvoicePageItemDTO> getClientsInvoices(String keyword , String status , int page);

    Page<InvoicePageItemDTO> getSuppliersInvoices(String keyword , String status , int page);

    InvoiceDTO createInvoice(InvoiceCreateDTO invoice) throws IOException;

    InvoiceDTO createClientInvoice(InvoiceCreateDTO invoice) throws IOException;

    @Transactional
    InvoiceDTO updateInvoice(InvoiceUpdateDTO invoiceUpdateDTO) throws IOException;

    @Transactional
    InvoiceDTO updateClientInvoice(InvoiceUpdateDTO invoiceUpdateDTO) throws IOException;

    InvoiceDTO updateInvoiceStatus(UUID invoiceId, InvoiceStatus invoiceStatus);

    InvoiceDTO updateClientInvoiceStatus(UUID invoiceId, InvoiceStatus invoiceStatus);

    InvoiceDTO updateClientInvoiceRemainingAmount(UUID invoiceId, double paidAmount);

    InvoiceDTO getInvoiceById(UUID invoiceId);

    InvoiceDTO getInvoiceByInvoiceNumber(String invoiceNumber);


    InvoiceDTO getClientInvoiceById(UUID invoiceId);

    Invoice getInvoiceDomainById(UUID invoiceId);

    Invoice getClientInvoiceDomainById(UUID invoiceId);

    void deleteInvoice(UUID invoiceId);

    void deleteClientInvoice(UUID invoiceId);

    boolean existsByInvoiceNumber(String invoiceNumber);

    boolean clientInvoiceExistsByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceId(UUID invoiceId);

    boolean existsByClientPurchaseOrderId(UUID purchaseOrderID);

    boolean clientInvoiceExistsByInvoiceId(UUID invoiceId);

    List<InvoicePageItemDTO> getClientTopInvoices(UUID clientId);

    List<InvoicePageItemDTO> getInvoicesToPay(String keyword);

    List<InvoicePageItemDTO> getSupplierTopInvoices(UUID supplierId);
    List<InvoiceSummaryDTO>  getClientInvoices (UUID clientId);


}
