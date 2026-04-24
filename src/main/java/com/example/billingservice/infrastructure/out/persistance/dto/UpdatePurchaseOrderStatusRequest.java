package com.example.billingservice.infrastructure.out.persistance.dto;

import com.example.billingservice.domain.enums.InvoiceStatus;
import com.example.billingservice.domain.enums.PurchaseOrderStatus;
import com.example.billingservice.infrastructure.out.persistance.validators.ValidEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePurchaseOrderStatusRequest {

    @NotNull(message = "Purchase order status is required")
    @ValidEnum(enumClass = PurchaseOrderStatus.class, message = "Status invalid")
    private String status;
}
