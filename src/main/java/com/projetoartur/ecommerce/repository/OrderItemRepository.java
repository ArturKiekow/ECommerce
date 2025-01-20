package com.projetoartur.ecommerce.repository;

import com.projetoartur.ecommerce.entities.OrderItemEntity;
import com.projetoartur.ecommerce.entities.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemId> {
}
