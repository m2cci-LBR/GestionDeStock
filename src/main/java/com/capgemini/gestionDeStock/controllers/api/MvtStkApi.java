package com.capgemini.gestionDeStock.controllers.api;

import com.capgemini.gestionDeStock.dto.MvtStockDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

import static com.capgemini.gestionDeStock.utils.Constants.APP_ROOT;

@Api(APP_ROOT+"/mvtstk")
public interface MvtStkApi {

    @GetMapping(APP_ROOT+"/mvtstk/stockreel/{idArticle}")
    BigDecimal stockReelArticle(@PathVariable("idArticle") Integer idArticle);

    @GetMapping(APP_ROOT+"/mvtstk/filter/article/{idArticle}")
    List<MvtStockDto> mvtStkArticle(@PathVariable("idArticle") Integer idArticle);

    @PostMapping(APP_ROOT+"/mvtstk/entree")
    MvtStockDto entreeStock(@RequestBody MvtStockDto dto);

    @PostMapping(APP_ROOT+"/mvtstk/sortie")
    MvtStockDto sortieStock(@RequestBody MvtStockDto dto);

    @PostMapping(APP_ROOT+"/mvtstk/correctionpos")
    MvtStockDto correctionStockPos(@RequestBody MvtStockDto dto);

    @PostMapping(APP_ROOT+"/mvtstk/correctionneg")
    MvtStockDto correctionStockNeg(@RequestBody MvtStockDto dto);
}
