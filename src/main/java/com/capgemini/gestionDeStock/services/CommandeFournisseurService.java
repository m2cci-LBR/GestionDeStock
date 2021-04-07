package com.capgemini.gestionDeStock.services;

import com.capgemini.gestionDeStock.dto.CommandeFournisseurDto;
import com.capgemini.gestionDeStock.dto.LigneCommandeFournisseurDto;
import com.capgemini.gestionDeStock.model.EtatCommande;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurDto save(CommandeFournisseurDto dto);

    CommandeFournisseurDto updateEtatCommande(Integer idCommande , EtatCommande etatCommande);

    CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

    CommandeFournisseurDto updateFournisseur(Integer idCommande,Integer idFournisseur);

    CommandeFournisseurDto updateArticle(Integer idCommande,Integer idLigneCommande,Integer idFournisseur);

    // Delete article ==> delete LigneCommandeFournisseur
    CommandeFournisseurDto deleteArticle(In idCommande,Integer idLigneCommande);

    CommandeFournisseurDto findById(Integer id);

    CommandeFournisseurDto findByCode(String code);

    List<CommandeFournisseurDto> findAll();

    List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseursByCommandeFournisseur(Integer idCommande);

    void delete(Integer id);
}
