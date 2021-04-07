package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.Article;
import com.capgemini.gestionDeStock.model.LigneVente;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class LigneVenteDto {

    private Integer id;
    private VentesDto ventes;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private Article article;

    public static LigneVenteDto fromEntity(LigneVente ligneVente){
        if (ligneVente == null){
            return null;
        }
        return LigneVenteDto.builder()
                            .id(ligneVente.getId())
                            .quantite(ligneVente.getQuantite())
                            .prixUnitaire(ligneVente.getPrixUnitaire())
                            .build();
    }

    public static LigneVente toEntity(LigneVenteDto dto){
        if (dto == null){
            return null;
        }

        LigneVente ligneVente = new LigneVente();
        ligneVente.setId(dto.getId());
        ligneVente.setQuantite(dto.getQuantite());
        ligneVente.setPrixUnitaire(dto.getPrixUnitaire());
        return ligneVente;
    }
}
