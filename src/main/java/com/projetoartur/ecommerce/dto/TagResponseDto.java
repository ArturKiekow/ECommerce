package com.projetoartur.ecommerce.dto;


import com.projetoartur.ecommerce.entities.TagEntity;

public record TagResponseDto(Long tagId,
                             String name) {
    public static TagResponseDto fromEntity(TagEntity entity){
        return new TagResponseDto(
                entity.getTagId(),
                entity.getName()
        );
    }
}
