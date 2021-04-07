package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.MvtStock;
import com.capgemini.gestionDeStock.model.SourceMvtStk;
import com.capgemini.gestionDeStock.model.TypeMvtStock;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class MvtStockDto {

    private Integer id;

    private Instant dateMvt;

    private BigDecimal quantite;

    private ArticleDto article;

    private TypeMvtStock typeMvtStock;

    private SourceMvtStk sourceMvt;

    private Integer idEntreprise;

    public static MvtStockDto fromEntity(MvtStock mvtStock){
        if (mvtStock == null){
            return null;
        }
        return MvtStockDto.builder()
                          .id(mvtStock.getId())
                          .dateMvt(mvtStock.getDateMvt())
                          .quantite(mvtStock.getQuantite())
                          .article(ArticleDto.fromEntity(mvtStock.getArticle()))
                          .typeMvtStock(mvtStock.getTypeMvtStock())
                          .sourceMvt(mvtStock.getSourceMvt())
                          .idEntreprise(mvtStock.getIdEntreprise())
                          .build();
    }


    public static MvtStock toEntity(MvtStockDto dto){
        if (dto == null){
            return null;
        }
        MvtStock mvtStock = new MvtStock();
        mvtStock.setId(dto.getId());
        mvtStock.setDateMvt(dto.getDateMvt());
        mvtStock.setQuantite(dto.getQuantite());
        mvtStock.setArticle(ArticleDto.toEntity(dto.getArticle()));
        mvtStock.setSourceMvt(dto.getSourceMvt());
        mvtStock.setTypeMvtStock(dto.getTypeMvtStock());

        return mvtStock;
    }
}
