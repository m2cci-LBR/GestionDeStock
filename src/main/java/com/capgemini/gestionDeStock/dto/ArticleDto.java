package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.Article;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class ArticleDto {

    private Integer id;

    private String codeArticle;

    private String designation;

    private BigDecimal prixUnitaireHt;

    private BigDecimal tauxTva;

    private BigDecimal prixUnitaireTtc;

    private String photo;

    private Integer idEntreprise;

    private CategoryDto category;

    public static ArticleDto fromEntity(Article article){
        if (article == null){
            return null;
            // TODO throw an exception
        }
        // Article --> ArticleDto
        return ArticleDto.builder()
                         .id(article.getId())
                         .codeArticle(article.getCodeArticle())
                         .designation(article.getDesignation())
                         .photo(article.getPhoto())
                         .prixUnitaireHt(article.getPrixUnitaireHt())
                         .prixUnitaireTtc(article.getPrixUnitaireTtc())
                         .tauxTva(article.getTauxTva())
                         .idEntreprise(article.getIdEntreprise())
                         .category(CategoryDto.fromEntity(article.getCategory()))
                         .build();

    }

    public static Article toEntity(ArticleDto articleDto){
        if (articleDto ==null){
            return null;
            //TODO throw an exception
        }
        // ArticleDto --> Article
        Article article=new Article();
        article.setId(articleDto.getId());
        article.setCodeArticle(articleDto.getCodeArticle());
        article.setDesignation(articleDto.getDesignation());
        article.setPhoto(articleDto.getPhoto());
        article.setPrixUnitaireHt(articleDto.getPrixUnitaireHt());
        article.setPrixUnitaireTtc(articleDto.getPrixUnitaireTtc());
        article.setIdEntreprise(articleDto.getIdEntreprise());
        article.setCategory(CategoryDto.toEntity(articleDto.getCategory()));
        return article;
    }

}
