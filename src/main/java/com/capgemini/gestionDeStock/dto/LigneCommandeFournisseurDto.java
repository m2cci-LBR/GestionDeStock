package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.LigneCommandeFournisseur;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LigneCommandeFournisseurDto {

    private Integer id;
    private ArticleDto article;
    private CommandeFournisseurDto commandeFournisseur;
    private BigDecimal quantite;
    private Integer idEntreprise;

    public static LigneCommandeFournisseurDto fromEntity(LigneCommandeFournisseur ligneCommandeFournisseur){
        if (ligneCommandeFournisseur ==  null){
            return null;
        }
        return LigneCommandeFournisseurDto.builder()
                                          .id(ligneCommandeFournisseur.getId())
                                          .article(ArticleDto.fromEntity(ligneCommandeFournisseur.getArticle()))
                                          .commandeFournisseur(CommandeFournisseurDto.fromEntity(ligneCommandeFournisseur.getCommandeFournisseurs()))
                                          .quantite(ligneCommandeFournisseur.getQuantite())
                                          .idEntreprise(ligneCommandeFournisseur.getIdEntreprise())
                                          .build();
    }

    public static LigneCommandeFournisseur toEntity(LigneCommandeFournisseurDto dto){
        if (dto == null){
            return null;
        }
        LigneCommandeFournisseur ligneCommandeFournisseur = new LigneCommandeFournisseur();
        ligneCommandeFournisseur.setId(dto.getId());
        ligneCommandeFournisseur.setArticle(ArticleDto.toEntity(dto.getArticle()));
        ligneCommandeFournisseur.setCommandeFournisseurs(CommandeFournisseurDto.toEntity(dto.getCommandeFournisseur()));
        ligneCommandeFournisseur.setQuantite(dto.getQuantite());
        ligneCommandeFournisseur.setIdEntreprise(dto.getIdEntreprise());
        return ligneCommandeFournisseur;
    }
}
