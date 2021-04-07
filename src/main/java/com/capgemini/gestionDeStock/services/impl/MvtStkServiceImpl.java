package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.MvtStockDto;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidEntityException;
import com.capgemini.gestionDeStock.model.TypeMvtStock;
import com.capgemini.gestionDeStock.repository.MvtStkRepository;
import com.capgemini.gestionDeStock.services.ArticleService;
import com.capgemini.gestionDeStock.services.MvtStckService;
import com.capgemini.gestionDeStock.validator.MvtStkValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MvtStkServiceImpl implements MvtStckService {

    private MvtStkRepository repository;
    private ArticleService articleService;

    @Autowired
    public MvtStkServiceImpl(MvtStkRepository repository, ArticleService articleService) {
        this.repository = repository;
        this.articleService = articleService;
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        if (idArticle == null){
            log.warn("ID article is NULL");
            return BigDecimal.valueOf(-1);
        }
        articleService.findById(idArticle);
        return repository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStockDto> mvtStkArticle(Integer idArticle) {
        return repository.findAllByArticleId(idArticle).stream()
                          .map(MvtStockDto::fromEntity)
                          .collect(Collectors.toList());
    }

    @Override
    public MvtStockDto entreeStock(MvtStockDto dto) {
        return entreePositive(dto,TypeMvtStock.ENTREE);
    }

    @Override
    public MvtStockDto sortieStock(MvtStockDto dto) {
       return sortieNegative(dto,TypeMvtStock.SORTIE);
    }

    @Override
    public MvtStockDto correctionStockPos(MvtStockDto dto) {
        return entreePositive(dto,TypeMvtStock.CORRECTION_POS);
    }

    @Override
    public MvtStockDto correctionStockNeg(MvtStockDto dto) {
        return sortieNegative(dto,TypeMvtStock.CORRECTION_NEG);
    }

    private MvtStockDto entreePositive(MvtStockDto dto,TypeMvtStock typeMvtStock){
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Le mvt de stock is not valid {}" ,dto);
            throw new InvalidEntityException("Le mvt de stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID,errors);
        }
        dto.setQuantite(BigDecimal.valueOf(Math.abs(dto.getQuantite().doubleValue())));
        dto.setTypeMvtStock(typeMvtStock);

        return MvtStockDto.fromEntity(repository.save(MvtStockDto.toEntity(dto)));
    }

    private MvtStockDto sortieNegative(MvtStockDto dto,TypeMvtStock typeMvtStock){
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Le mvt de stock is not valid {}" ,dto);
            throw new InvalidEntityException("Le mvt de stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID,errors);
        }
        dto.setQuantite(BigDecimal.valueOf(Math.abs(dto.getQuantite().doubleValue()) * -1));
        dto.setTypeMvtStock(typeMvtStock);
        return MvtStockDto.fromEntity(repository.save(MvtStockDto.toEntity(dto)));
    }
}
