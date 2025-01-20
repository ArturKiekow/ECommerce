package com.projetoartur.ecommerce.controller;


import com.projetoartur.ecommerce.dto.*;
import com.projetoartur.ecommerce.entities.OrderEntity;
import com.projetoartur.ecommerce.service.OrderService;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderDto dto){
        var order = orderService.createOrder(dto);
        return ResponseEntity.created(URI.create("/orders/" + order.getOrderId())).build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<OrderSummaryDto>> listOrders(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        var response = orderService.findAll(page, pageSize);
        return ResponseEntity.ok(new ApiResponse<>(
                response.getContent(),
                new PaginationResponse(
                        response.getNumber(),
                        response.getSize(),
                        response.getTotalElements(),
                        response.getTotalPages()
                )
        ));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> findOrderById(@PathVariable("orderId") Long orderId){
        var orderOp = orderService.findOrderById(orderId);
        return orderOp.isPresent() ?
                ResponseEntity.ok(OrderResponseDto.fromEntity(orderOp.get())) :
                ResponseEntity.notFound().build();
    }

}
