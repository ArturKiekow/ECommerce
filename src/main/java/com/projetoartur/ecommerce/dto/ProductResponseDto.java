package com.projetoartur.ecommerce.dto;

import com.projetoartur.ecommerce.entities.ProductEntity;
import com.projetoartur.ecommerce.entities.TagEntity;

import java.util.List;

public record ProductResponseDto(Long productId,
                                 String productName,
                                 List<TagResponseDto> tags) {

    public static ProductResponseDto fromEntity(ProductEntity entity){
        return new ProductResponseDto(
                entity.getProductId(),
                entity.getProductName(),
                getTags(entity.getTags())
        );
    }

    private static List<TagResponseDto> getTags(List<TagEntity> tags) {
        return tags.stream()
                .map(TagResponseDto::fromEntity)
                .toList();
    }
}
