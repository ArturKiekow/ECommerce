package com.projetoartur.ecommerce.service;

import com.projetoartur.ecommerce.dto.CreateOrderDto;
import com.projetoartur.ecommerce.dto.OrderItemDto;
import com.projetoartur.ecommerce.dto.OrderSummaryDto;
import com.projetoartur.ecommerce.entities.*;
import com.projetoartur.ecommerce.exception.CreateOrderException;
import com.projetoartur.ecommerce.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public OrderEntity createOrder(CreateOrderDto dto){
        var order = new OrderEntity();
        //1-Validar usuario
        var user = validateUser(dto);
        //2-validar itens do pedido
        var orderItems = validateOrderItems(order, dto);
        //3-Calcular preço total
        var totalPrice = calculateOrderTotal(orderItems);
        //4-Mapear para a entity
        order.setUser(user);
        order.setTotalValue(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setItems(orderItems);
        //5-Salvar entity
        return orderRepository.save(order);


    }


    public UserEntity validateUser(CreateOrderDto dto){
        return userRepository.findById(dto.userId())
                .orElseThrow(() -> new CreateOrderException("User not found"));
    }

    private List<OrderItemEntity> validateOrderItems(OrderEntity order, CreateOrderDto dto) {

        if (dto.items().isEmpty()){
            throw new CreateOrderException("Order items is empty");
        }

        return dto.items()
                .stream()
                .map(orderitemDto -> getOrderItem(order, orderitemDto))
                .toList();
    }

    private OrderItemEntity getOrderItem(OrderEntity order, OrderItemDto orderitemDto) {
        var orderItemEntity = new OrderItemEntity();
        var orderItemId = new OrderItemId();
        var product = getProduct(orderitemDto.productId());

        orderItemId.setOrder(order);
        orderItemId.setProduct(product);

        orderItemEntity.setId(orderItemId);
        orderItemEntity.setQuantity(orderitemDto.quantity());
        orderItemEntity.setSalePrice(product.getPrice());

        return orderItemEntity;
    }

    private ProductEntity getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CreateOrderException("Product not found"));
    }

    private BigDecimal calculateOrderTotal(List<OrderItemEntity> orderItems) {
        return orderItems.stream()
                .map(item -> item.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public Page<OrderSummaryDto> findAll(Integer page, Integer pageSize) {

        return orderRepository.findAll(PageRequest.of(page, pageSize))
                .map(entity -> new OrderSummaryDto(
                        entity.getOrderId(),
                        entity.getTotalValue(),
                        entity.getOrderDate(),
                        entity.getUser().getUserId()
                ));
    }

    public Optional<OrderEntity> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
