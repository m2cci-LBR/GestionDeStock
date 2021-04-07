package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.*;
import com.capgemini.gestionDeStock.exceptions.EntityNotFoundException;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidEntityException;
import com.capgemini.gestionDeStock.exceptions.InvalidOperationException;
import com.capgemini.gestionDeStock.model.*;
import com.capgemini.gestionDeStock.repository.ArticleRepository;
import com.capgemini.gestionDeStock.repository.ClientRepository;
import com.capgemini.gestionDeStock.repository.CommandeClientRepository;
import com.capgemini.gestionDeStock.repository.LigneCommandeClientRepository;
import com.capgemini.gestionDeStock.services.CommandeClientService;
import com.capgemini.gestionDeStock.services.MvtStckService;
import com.capgemini.gestionDeStock.validator.ArticleValidator;
import com.capgemini.gestionDeStock.validator.CommandeClientValidator;
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
public class CommandeClientServiceImpl implements CommandeClientService {

    private CommandeClientRepository commandeClientRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    private ClientRepository clientRepository;
    private ArticleRepository articleRepository;
    private MvtStckService mvtStckService;

    @Autowired
    public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository,
                                     ClientRepository clientRepository,
                                     ArticleRepository articleRepository,
                                     LigneCommandeClientRepository ligneCommandeClientRepository,
                                     MvtStckService mvtStckService) {
        this.commandeClientRepository = commandeClientRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.mvtStckService = mvtStckService;
    }

    @Override
    public CommandeClientDto save(CommandeClientDto dto) {
        List<String> errors = CommandeClientValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Commande client not valid");
            throw new InvalidEntityException("La commande client n'est pas valide", ErrorCodes.COAMMANDE_CLIENT_NOT_VALID,errors);
        }

        if (dto.getId() != null && dto.isCommandeLivree()){ // etat de modification
            throw new InvalidOperationException("Impossible de modifier la commande quand elle est livree",ErrorCodes.COAMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(dto.getClient().getId());
        if (client.isEmpty()){
            log.warn("Client with ID {} not found in the DB " , dto.getClient().getId());
            throw new EntityNotFoundException("Aucun client avec l'ID " +dto.getClient().getId()+"n'a ete trouve",ErrorCodes.CLIENT_NOT_FOUND);
        }

        List<String> articleErrors = new ArrayList<>();
        if (dto.getLigneCommandeClients() != null){
            dto.getLigneCommandeClients().forEach(ligCmdClt ->{
                if (ligCmdClt.getArticle() != null){
                    Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
                    if (article.isEmpty()){
                        articleErrors.add("L'article avec l'ID " +ligCmdClt.getArticle().getId()+ " n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
                }
            });
        }
        if (!articleErrors.isEmpty()){
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BD",ErrorCodes.ARTICLE_NOT_FOUND,articleErrors);
        }

        CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(dto));
        if (dto.getLigneCommandeClients() != null){
            dto.getLigneCommandeClients().forEach(ligCmdClt -> {
                LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligCmdClt);
                ligneCommandeClient.setCommandeClient(savedCmdClt);
                ligneCommandeClientRepository.save(ligneCommandeClient);
            });
        }
        return CommandeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeClientDto findById(Integer id) {
        if (id == null) {
            log.error("Commande Client ID is NULL");
            return null;
        }
        return commandeClientRepository.findById(id)
                                       .map(CommandeClientDto::fromEntity)
                                       .orElseThrow(() -> new EntityNotFoundException("Aucune commande client trouve avec l'ID " + id , ErrorCodes.COAMMANDE_CLIENT_NOT_FOUND));
    }

    @Override
    public CommandeClientDto findByCode(String code) {
        if (!StringUtils.hasLength(code)){
            log.error("Commande client CODE is NULL");
            return null;
        }
        return commandeClientRepository.findCommandeClientByCode(code)
                                       .map(CommandeClientDto::fromEntity)
                                       .orElseThrow(() -> new EntityNotFoundException("Aucune commande client n'a ete trouve avec le code " + code,ErrorCodes.COAMMANDE_CLIENT_NOT_FOUND));
    }

    @Override
    public List<CommandeClientDto> findAll() {
        return commandeClientRepository.findAll()
                                        .stream()
                                        .map(CommandeClientDto::fromEntity)
                                        .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
        return ligneCommandeClientRepository.findAllByCommandeClientId(idCommande).stream()
                                            .map(LigneCommandeClientDto::fromEntity)
                                            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Command client ID is NULL");
            return;
        }
        commandeClientRepository.deleteById(id);
    }

    @Override
    public CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {

        checkIdCommande(idCommande);
        if (!StringUtils.hasLength(String.valueOf(etatCommande))){
            log.error("L'etat de la commande client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null",
                    ErrorCodes.COAMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);
        commandeClient.setEtatCommande(etatCommande);

        CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient));
        if (commandeClient.isCommandeLivree()){
            updateMvtStk(idCommande);
        }
        return CommandeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {

        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0){
            log.error("La quantite is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quatite nulle ou egale a Zero",
                    ErrorCodes.COAMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);
        Optional<LigneCommandeClient> ligneCommandeClientOptional = findLigneCommandeClient(idLigneCommande);
        LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();
        ligneCommandeClient.setQuantite(quantite);
        ligneCommandeClientRepository.save(ligneCommandeClient);

        return commandeClient;
    }



    @Override
    public CommandeClientDto updateClient(Integer idCommande, Integer idClient) {

        checkIdCommande(idCommande);
        if (idClient == null){
            log.error("L'id Client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un id Client null",
                    ErrorCodes.COAMMANDE_CLIENT_NON_MODIFIABLE);
        }
        CommandeClientDto commandeClient = checkEtatCommande(idCommande);

        Optional<Client> clientOptional = clientRepository.findById(idClient);
        if (clientOptional.isEmpty()){
            throw new EntityNotFoundException("Aucun Client trouve avec l'ID " + idClient,ErrorCodes.CLIENT_NOT_FOUND);
        }

        commandeClient.setClient(ClientDto.fromEntity(clientOptional.get()));
        return CommandeClientDto.fromEntity(commandeClientRepository
                                .save(CommandeClientDto.toEntity(commandeClient)));
    }

    @Override
    public CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande,Integer idArticle) {

        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle,"nouvel");

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);

        Optional<LigneCommandeClient> ligneCommandeClient = findLigneCommandeClient(idLigneCommande);
        Optional<Article> articleOptional = articleRepository.findById(idArticle);
        if (articleOptional.isEmpty()){
            throw new EntityNotFoundException("Aucun article trouve avec l'id " + idArticle,ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
        if (errors.isEmpty()){
            throw new InvalidEntityException("Article invalid",ErrorCodes.ARTICLE_NOT_VALID,errors);
        }

        LigneCommandeClient ligneCommandeClientToSave = ligneCommandeClient.get();
        ligneCommandeClientToSave.setArticle(articleOptional.get());
        ligneCommandeClientRepository.save(ligneCommandeClientToSave);

        return commandeClient;
    }

    @Override
    public CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        CommandeClientDto commandeClient = checkEtatCommande(idCommande);

        //Just to check the LigneCommandeClient and inform the client in case it is absent
        findLigneCommandeClient(idLigneCommande);
        ligneCommandeClientRepository.deleteById(idLigneCommande);

        return commandeClient;
    }

    private Optional<LigneCommandeClient> findLigneCommandeClient(Integer idLigneCommande) {

        Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);

        if (ligneCommandeClientOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucune ligne commande client avec l'ID" + idLigneCommande, ErrorCodes.COAMMANDE_CLIENT_NOT_FOUND);
        }
        return ligneCommandeClientOptional;
    }

    private CommandeClientDto checkEtatCommande(Integer idCommande){
        CommandeClientDto commandeClient = findById(idCommande);
        if (commandeClient.isCommandeLivree()){
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande lorsqu'elle est livree",
                    ErrorCodes.COAMMANDE_CLIENT_NON_MODIFIABLE);
        }
        return commandeClient;
    }

    private void checkIdCommande(Integer idCommande){
        if (idCommande == null){
            log.error("Commande client ID is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
                    ErrorCodes.COAMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void checkIdLigneCommande(Integer idLigneCommande){
        if (idLigneCommande == null){
            log.error("L'id de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
                    ErrorCodes.COAMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Integer idArticle , String msg){
        if (idArticle == null){
            log.error("L'id de l'article " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg +" article null",
                    ErrorCodes.COAMMANDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void updateMvtStk(Integer idCommande){
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(idCommande);
        ligneCommandeClients.forEach(lig->{
            MvtStockDto mvtStockDto = MvtStockDto.builder()
                                                 .article(ArticleDto.fromEntity(lig.getArticle()))
                                                 .dateMvt(Instant.now())
                                                 .typeMvtStock(TypeMvtStock.SORTIE)
                                                 .sourceMvt(SourceMvtStk.COMMANDE_CLIENT)
                                                 .quantite(lig.getQuantite())
                                                 .idEntreprise(lig.getIdEntreprise())
                                                 .build();
            mvtStckService.sortieStock(mvtStockDto);
        });
    }
}
