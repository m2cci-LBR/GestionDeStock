package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.CategoryDto;
import com.capgemini.gestionDeStock.exceptions.EntityNotFoundException;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidEntityException;
import com.capgemini.gestionDeStock.services.CategoryService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService service;

    @Test
    public void shouldSaveCategoryWithSuccess(){
        CategoryDto expectedCategory = CategoryDto.builder()
                   .code("Cat test")
                   .designation("Designation test")
                   .idEntreprise(1)
                   .build();

       CategoryDto savedCategory = service.save(expectedCategory);

        Assertions.assertNotNull(savedCategory);
        Assertions.assertNotNull(savedCategory.getId());
        Assertions.assertEquals(expectedCategory.getCode(),savedCategory.getCode());
        Assertions.assertEquals(expectedCategory.getDesignation(),savedCategory.getDesignation());
        //Assertions.assertEquals(expectedCategory.getIdEntreprise(),savedCategory.getIdEntreprise());
    }

    @Test
    public void shouldUpdateCategoryWithSuccess(){
        CategoryDto expectedCategory = CategoryDto.builder()
                .code("Cat test")
                .designation("Designation test")
                .idEntreprise(1)
                .build();

        CategoryDto savedCategory = service.save(expectedCategory);

        CategoryDto categoryToUpdate = savedCategory;
        categoryToUpdate.setCode("Cat Update");
        savedCategory = service.save(categoryToUpdate);

        Assertions.assertNotNull(savedCategory);
        Assertions.assertNotNull(categoryToUpdate.getId());
        Assertions.assertEquals(categoryToUpdate.getCode(),savedCategory.getCode());
        Assertions.assertEquals(categoryToUpdate.getDesignation(),savedCategory.getDesignation());
        //Assertions.assertEquals(expectedCategory.getIdEntreprise(),savedCategory.getIdEntreprise());
    }

    @Test
    public void shouldThrowInvalidEntityException(){
        CategoryDto expectedCategory = CategoryDto.builder().build();

        InvalidEntityException expectedException = assertThrows(InvalidEntityException.class,() -> service.save(expectedCategory));

        assertEquals(ErrorCodes.CATEGORY_NOT_VALID,expectedException.getErrorCode());
        assertEquals(1,expectedException.getErrors().size());
        assertEquals("Veuillez renseigner le code de la categorie",expectedException.getErrors().get(0));
    }

    @Test
    public void shouldThrowEntityNotFoundException(){

        EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class,() -> service.findById(0));

        assertEquals(ErrorCodes.CATEGORY_NOT_FOUND,expectedException.getErrorCode());
        assertEquals("Aucune category avec l'ID 0",expectedException.getMessage());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundException2(){
      service.findById(0);
    }



}