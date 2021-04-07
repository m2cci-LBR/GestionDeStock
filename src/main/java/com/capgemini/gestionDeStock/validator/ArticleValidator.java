package com.capgemini.gestionDeStock.validator;

import com.capgemini.gestionDeStock.dto.ArticleDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticleValidator {

    public static List<String> validate(ArticleDto dto){
        List<String> errors = new ArrayList<>();
        if (dto == null){
            errors.add("Veuillez renseigner le code de l'article");
            errors.add("Veuillez renseigner le mail de l'utilisateur");
            errors.add("Veuillez renseigner le prix unitaire HT de l'article");
            errors.add("Veuillez renseigner le taux TVA de l'article");
            errors.add("Veuillez renseigner le prix unitaire TTC de l'article");
            errors.add("Veuillez renseigner la categorie de l'article");
        }

        if (!StringUtils.hasLength(dto.getCodeArticle())){
            errors.add("Veuillez renseigner le code de l'article");
        }
        if (!StringUtils.hasLength(dto.getDesignation())){
            errors.add("Veuillez renseigner le mail de l'utilisateur");
        }
        if (dto.getPrixUnitaireHt() == null){
            errors.add("Veuillez renseigner le prix unitaire HT de l'article");
        }
        if (dto.getTauxTva() ==null){
            errors.add("Veuillez renseigner le taux TVA de l'article");
        }

        if (dto.getPrixUnitaireTtc() == null){
            errors.add("Veuillez renseigner le prix unitaire TTC de l'article");
        }
        if (dto.getCategory() == null){
            errors.add("Veuillez renseigner la categorie de l'article");
        }
        return errors;
    }
}
