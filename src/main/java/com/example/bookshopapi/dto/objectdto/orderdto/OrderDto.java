package com.example.bookshopapi.dto.objectdto.orderdto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDto {
    private int order_id;
    private String merchandise_subtotal;
    private String total_quantity;
    private String created_on;
    private String shipped_on;
    private int customer_id;
    private String address;
    private String receiver_name;
    private String receiver_phone;
    private int shipping_id;
    private String shipping_type;
    private String shipping_cost;
    private String order_status;
    private String order_total;
    private int isRating;
}
