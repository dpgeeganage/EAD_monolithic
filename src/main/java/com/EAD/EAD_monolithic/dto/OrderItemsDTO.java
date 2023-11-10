package com.EAD.EAD_monolithic.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemsDTO {

    private Integer itemId;
    private Integer orderId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalUnitPrice;
}
