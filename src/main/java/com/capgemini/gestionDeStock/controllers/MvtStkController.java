package com.capgemini.gestionDeStock.controllers;

import com.capgemini.gestionDeStock.controllers.api.MvtStkApi;
import com.capgemini.gestionDeStock.dto.MvtStockDto;
import com.capgemini.gestionDeStock.services.MvtStckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class MvtStkController implements MvtStkApi {
    private MvtStckService service;

    @Autowired
    public MvtStkController(MvtStckService service) {
        this.service = service;
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        return service.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStockDto> mvtStkArticle(Integer idArticle) {
        return service.mvtStkArticle(idArticle);
    }

    @Override
    public MvtStockDto entreeStock(MvtStockDto dto) {
        return service.entreeStock(dto);
    }

    @Override
    public MvtStockDto sortieStock(MvtStockDto dto) {
        return service.sortieStock(dto);
    }

    @Override
    public MvtStockDto correctionStockPos(MvtStockDto dto) {
        return service.correctionStockPos(dto);
    }

    @Override
    public MvtStockDto correctionStockNeg(MvtStockDto dto) {
        return service.correctionStockNeg(dto);
    }
}
