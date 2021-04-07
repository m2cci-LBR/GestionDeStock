package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.ArticleDto;
import com.capgemini.gestionDeStock.dto.LigneVenteDto;
import com.capgemini.gestionDeStock.dto.MvtStockDto;
import com.capgemini.gestionDeStock.dto.VentesDto;
import com.capgemini.gestionDeStock.exceptions.EntityNotFoundException;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidEntityException;
import com.capgemini.gestionDeStock.model.*;
import com.capgemini.gestionDeStock.repository.ArticleRepository;
import com.capgemini.gestionDeStock.repository.LigneVenteRepository;
import com.capgemini.gestionDeStock.repository.VentesRepository;
import com.capgemini.gestionDeStock.services.MvtStckService;
import com.capgemini.gestionDeStock.services.VentesService;
import com.capgemini.gestionDeStock.validator.VentesValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VentesServiceImpl implements VentesService {

    private ArticleRepository articleRepository;
    private VentesRepository ventesRepository;
    private LigneVenteRepository ligneVenteRepository;
    private MvtStckService mvtStckService;

    @Autowired
    public VentesServiceImpl(ArticleRepository articleRepository,
                             VentesRepository ventesRepository,
                             LigneVenteRepository ligneVenteRepository,
                             MvtStckService mvtStckService) {
        this.articleRepository = articleRepository;
        this.ventesRepository = ventesRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.mvtStckService = mvtStckService;
    }

    @Override
    public VentesDto save(VentesDto dto) {
        List<String> errors = VentesValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("");
            throw new InvalidEntityException("L'objet vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID,errors);
        }

        List<String> articleErrors = new ArrayList<>();
        dto.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()){
                articleErrors.add("Aucun article trouve avec l'ID " + ligneVenteDto.getArticle().getId());
            }
        });
        if (!articleErrors.isEmpty()){
            log.error("One or more articles were not found in the DB , {}", errors);
            throw new InvalidEntityException("Un ou plusieurs articles non trouve au niveau de la BD",ErrorCodes.VENTE_NOT_VALID);
        }
        Ventes savedVentes = ventesRepository.save(VentesDto.toEntity(dto));
        dto.getLigneVentes().forEach(ligneVenteDto -> {
            LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
            ligneVente.setVente(savedVentes);
            ligneVenteRepository.save(ligneVente);
            updateMvtStk(ligneVente);
        });

        return VentesDto.fromEntity(savedVentes);
    }

    @Override
    public VentesDto findById(Integer id) {
        if (id == null){
            log.error("Ventes ID is NULL");
            return null;
        }
        return ventesRepository.findById(id)
                                .map(VentesDto::fromEntity)
                                .orElseThrow(()-> new EntityNotFoundException("Aucune vente trouve dans la BD",ErrorCodes.VENTE_NOT_FOUND));
    }

    @Override
    public VentesDto findByCode(String code) {
        if (!StringUtils.hasLength(code)){
            log.error("Ventes ID is NULL");
            return null;
        }
        return ventesRepository.findVentesByCode(code)
                               .map(VentesDto::fromEntity)
                               .orElseThrow(() -> new EntityNotFoundException("Aucune ventes trouve avec le code" +code,ErrorCodes.VENTE_NOT_VALID));
    }

    @Override
    public List<VentesDto> findAll() {
        return ventesRepository.findAll().stream()
                               .map(VentesDto::fromEntity)
                               .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
            if (id == null){
                log.error("Ventes ID is NULL");
                return;
            }
    ventesRepository.deleteById(id);
    }

    private void updateMvtStk(LigneVente lig){
            MvtStockDto mvtStockDto = MvtStockDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvtStock(TypeMvtStock.SORTIE)
                    .sourceMvt(SourceMvtStk.VENTE)
                    .quantite(lig.getQuantite())
                    .idEntreprise(lig.getIdEntreprise())
                    .build();
            mvtStckService.entreeStock(mvtStockDto);
    }
}
