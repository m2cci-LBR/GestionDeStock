package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.CategoryDto;
import com.capgemini.gestionDeStock.exceptions.EntityNotFoundException;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidEntityException;
import com.capgemini.gestionDeStock.repository.CategoryRepository;
import com.capgemini.gestionDeStock.services.CategoryService;
import com.capgemini.gestionDeStock.validator.CategoryValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        List<String> errors = CategoryValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Category is not valid {}", dto);
            throw new InvalidEntityException("Category non valide", ErrorCodes.CATEGORY_NOT_VALID,errors);
        }
        return CategoryDto.fromEntity(categoryRepository.save(CategoryDto.toEntity(dto)));
    }

    @Override
    public CategoryDto findById(Integer id) {
        if (id == null){
            log.error("Category ID is NULL");
            return null;
        }
        return categoryRepository.findById(id)
                                 .map(CategoryDto::fromEntity)
                                 .orElseThrow(() -> new EntityNotFoundException("Aucune category avec l'ID " + id,ErrorCodes.CATEGORY_NOT_FOUND));
    }

    @Override
    public CategoryDto findByCodeCategory(String codeCategory) {
        if(!StringUtils.hasLength(codeCategory)){
            log.error("Category code is NULL");
            return null;
        }
        return categoryRepository.findCategoryByCode(codeCategory)
                                 .map(CategoryDto::fromEntity)
                                 .orElseThrow(()-> new EntityNotFoundException("Aucune category avec le code" + codeCategory,ErrorCodes.CATEGORY_NOT_FOUND));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                                 .map(CategoryDto::fromEntity)
                                 .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Category ID is NULL");
            return;
        }
        categoryRepository.deleteById(id);
    }
}
