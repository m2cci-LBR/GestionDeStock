package com.capgemini.gestionDeStock.dto;
import com.capgemini.gestionDeStock.model.LigneCommandeClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LigneCommandeClientDto {

    private Integer id;

    private ArticleDto article;

    @JsonIgnore
    private CommandeClientDto commandeClient;

    private BigDecimal quantite;

    private BigDecimal prixUnitaire;

    public static LigneCommandeClientDto fromEntity(LigneCommandeClient ligneCommandeClient){
        if (ligneCommandeClient == null){
            return null;
        }
        return LigneCommandeClientDto.builder()
                                     .id(ligneCommandeClient.getId())
                                     .article(ArticleDto.fromEntity(ligneCommandeClient.getArticle()))
                                     .quantite(ligneCommandeClient.getQuantite())
                                     .prixUnitaire(ligneCommandeClient.getPrixUnitaire())
                                     .build();
    }

    public static LigneCommandeClient toEntity(LigneCommandeClientDto dto){
        if (dto == null){
            return null;
        }
        LigneCommandeClient ligneCommandeClient = new LigneCommandeClient();
        ligneCommandeClient.setId(dto.getId());
        ligneCommandeClient.setArticle(ArticleDto.toEntity(dto.getArticle()));
        ligneCommandeClient.setQuantite(dto.getQuantite());
        ligneCommandeClient.setPrixUnitaire(dto.getPrixUnitaire());
        return ligneCommandeClient;
    }


}
