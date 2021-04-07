package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryDto {

    private Integer id;
    private String code;
    private String designation;
    private Integer idEntreprise;
    @JsonIgnore
    private List<ArticleDto> articles;

    public static CategoryDto fromEntity(Category category){
        if (category == null){
            return null;
            // TODO throw an exception
        }
        // Category --> CategoryDto
       return CategoryDto.builder()
                         .id(category.getId())
                         .code(category.getCode())
                         .designation(category.getDesignation())
                         .idEntreprise(category.getIdEntreprise())
                         .build();

    }

    public static Category toEntity(CategoryDto categoryDto){
        if (categoryDto ==null){
            return null;
            //TODO throw an exception
        }
        // CategoryDto --> Category
       Category category=new Category();
        category.setId(categoryDto.getId());
        category.setCode(categoryDto.getCode());
        category.setDesignation(categoryDto.getDesignation());
        category.setIdEntreprise(category.getIdEntreprise());
        return category;
    }
}
