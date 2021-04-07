package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.*;
import com.capgemini.gestionDeStock.exceptions.EntityNotFoundException;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidEntityException;
import com.capgemini.gestionDeStock.exceptions.InvalidOperationException;
import com.capgemini.gestionDeStock.model.*;
import com.capgemini.gestionDeStock.repository.ArticleRepository;
import com.capgemini.gestionDeStock.repository.CommandeFournisseurRepository;
import com.capgemini.gestionDeStock.repository.FournisseurRepository;
import com.capgemini.gestionDeStock.repository.LigneCommandeFournisseurRepository;
import com.capgemini.gestionDeStock.services.CommandeFournisseurService;
import com.capgemini.gestionDeStock.services.MvtStckService;
import com.capgemini.gestionDeStock.validator.ArticleValidator;
import com.capgemini.gestionDeStock.validator.CommandeFournisseurValidator;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private CommandeFournisseurRepository commandeFournisseurRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private FournisseurRepository fournisseurRepository;
    private ArticleRepository articleRepository;
    private MvtStckService mvtStckService;

    @Autowired
    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository,
                                          LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository,
                                          FournisseurRepository fournisseurRepository,
                                          ArticleRepository articleRepository,
                                          MvtStckService mvtStckService) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.articleRepository = articleRepository;
        this.mvtStckService = mvtStckService;
    }

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto dto) {
        List<String> errors = CommandeFournisseurValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Commande Fournisseur is not valid");
            throw new InvalidEntityException("La commande Fournisseur n'est pas valide", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID,errors);
        }

        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(dto.getFournisseur().getId());
        if (fournisseur.isEmpty()){
            log.warn("Fournisseur with ID {} was not found in the DB" , dto.getFournisseur().getId());
            throw new EntityNotFoundException("Aucun Fournisseur avec l'ID"  + dto.getFournisseur().getId() + " n'a ete trouve dans la BD",ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();
        if (dto.getLigneCommandeFournisseurs() != null){
            dto.getLigneCommandeFournisseurs().forEach(ligCmdFr ->{
                if (ligCmdFr.getArticle() != null){
                    Optional<Article> article = articleRepository.findById(ligCmdFr.getArticle().getId());
                    if (article.isEmpty()){
                        articleErrors.add("L'article avec l'ID" +ligCmdFr.getArticle().getId() + " n'existe pas");
                    } else {
                        articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
                    }
                }
            });
        }

        if (!articleErrors.isEmpty()){
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD",ErrorCodes.ARTICLE_NOT_FOUND,articleErrors);
        }

        CommandeFournisseur savedCmdFrs =  commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(dto));

        if (dto.getLigneCommandeFournisseurs() != null){
            dto.getLigneCommandeFournisseurs().forEach(ligCmdFrs ->{
                LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligCmdFrs);
                ligneCommandeFournisseur.setCommandeFournisseurs(savedCmdFrs);
                ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
            });
        }
        return CommandeFournisseurDto.fromEntity(savedCmdFrs);
    }

    @Override
    public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);
        if (!StringUtils.hasLength(String.valueOf(etatCommande))){
            log.error("L'etat de la commande fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat NULL",ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        CommandeFournisseurDto commandeFournisseur= checkEtatCommande(idCommande);
        commandeFournisseur.setEtatCommande(etatCommande);

        CommandeFournisseur savedCmdFr = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur));

        if (commandeFournisseur.isCommandeLivree()){
            updateMvtStk(idCommande);
        }
        return CommandeFournisseurDto.fromEntity(savedCmdFr);
    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0){
            log.error("L'ID de la commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite Null ou egale Zero",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = findLigneCommandeFournisseur(idLigneCommande);
        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurOptional.get();
        ligneCommandeFournisseur.setQuantite(quantite);
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

        return commandeFournisseur;
    }

    private Optional<LigneCommandeFournisseur> findLigneCommandeFournisseur(Integer idLigneCommande) {
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);
        if (ligneCommandeFournisseurOptional.isEmpty()){
            throw new EntityNotFoundException("Aucune ligne fournisseur trouvee avec l'ID " + idLigneCommande,
                      ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND);
        }
        return ligneCommandeFournisseurOptional;
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {

        checkIdCommande(idCommande);
        if (idFournisseur == null){
            log.error("L'ID fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID forunisseur NULL",
                      ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
        if (fournisseurOptional.isEmpty()){
           throw new EntityNotFoundException("Aucun Fournisseur avec l'ID" + idFournisseur ,ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND);
        }

        commandeFournisseur.setFournisseur(FournisseurDto.fromEntity(fournisseurOptional.get()));
        return CommandeFournisseurDto.fromEntity(
                commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur))
        );
    }

    @Override
    public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle,"nouvel");

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);

        Optional<LigneCommandeFournisseur> ligneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(idArticle);
        if (articleOptional.isEmpty()){
            throw new EntityNotFoundException("Aucun article trouve avec l'ID " +idArticle ,ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
        if (!errors.isEmpty()){
            throw new InvalidEntityException("Article non valide",ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        LigneCommandeFournisseur ligneCommandeFournisseurToSave = ligneCommandeFournisseur.get();
        ligneCommandeFournisseurToSave.setArticle(articleOptional.get());
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseurToSave);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto deleteArticle(In idCommande, Integer idLigneCommande) {
        checkIdCommande(idLigneCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idLigneCommande);
        // Just to check the LigneCommandeFournisseur and inform fournisseur in case it is absent
        findLigneCommandeFournisseur(idLigneCommande);
        ligneCommandeFournisseurRepository.deleteById(idLigneCommande);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto findById(Integer id) {
        if (id == null){
            log.error("Commande forunisseur ID is null");
            return null;
        }

        return commandeFournisseurRepository.findById(id)
                                            .map(CommandeFournisseurDto::fromEntity)
                                            .orElseThrow(() -> new EntityNotFoundException("Aucune commande fournisseur avec l ID " + id,ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND));

    }

    @Override
    public CommandeFournisseurDto findByCode(String code) {
        if (!StringUtils.hasLength(code)){
            log.error("Commande Fournisseur Code is NULL");
            return null;
        }
        return commandeFournisseurRepository.findCommandeFournisseurByCode(code)
                                            .map(CommandeFournisseurDto::fromEntity)
                                             .orElseThrow(() -> new EntityNotFoundException("Aucune Commande Fournisseur n'a ete trouve avec le code" + code,ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND));
    }

    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurRepository.findAll().stream()
                                            .map(CommandeFournisseurDto::fromEntity)
                                            .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseursByCommandeFournisseur(Integer idCommande) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Commande Fournisseur ID is NULL");
            return;
        }
        commandeFournisseurRepository.deleteById(id);
    }

    private CommandeFournisseurDto checkEtatCommande(Integer idCommande){
        CommandeFournisseurDto commandeFournisseur = findById(idCommande);
        if (commandeFournisseur.isCommandeLivree()){
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande lorsqu'elle est livree",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        return commandeFournisseur;
    }

    private void checkIdCommande(Integer idCommande){
        if (idCommande == null){
            log.error("Commande client ID is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdLigneCommande(Integer idLigneCommande){
        if (idLigneCommande == null){
            log.error("L'id de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Integer idArticle , String msg){
        if (idArticle == null){
            log.error("L'id de l'article " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg +" article null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void updateMvtStk(Integer idCommande){
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande);
        ligneCommandeFournisseurs.forEach(lig->{
            MvtStockDto mvtStockDto = MvtStockDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvtStock(TypeMvtStock.ENTREE)
                    .sourceMvt(SourceMvtStk.COMMANDE_FOURNISSEUR)
                    .quantite(lig.getQuantite())
                    .idEntreprise(lig.getIdEntreprise())
                    .build();
            mvtStckService.entreeStock(mvtStockDto);
        });
    }
}
