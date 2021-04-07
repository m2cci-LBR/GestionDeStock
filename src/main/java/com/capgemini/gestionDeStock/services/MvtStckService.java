package com.capgemini.gestionDeStock.services;

import com.capgemini.gestionDeStock.dto.MvtStockDto;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStckService {

    BigDecimal stockReelArticle(Integer idArticle);

    List<MvtStockDto> mvtStkArticle(Integer idArticle);

    MvtStockDto entreeStock(MvtStockDto dto);

    MvtStockDto sortieStock(MvtStockDto dto);

    MvtStockDto correctionStockPos(MvtStockDto dto);

    MvtStockDto correctionStockNeg(MvtStockDto dto);
}
