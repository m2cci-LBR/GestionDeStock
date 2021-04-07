package com.capgemini.gestionDeStock.controllers.api;

import com.capgemini.gestionDeStock.dto.CommandeClientDto;
import com.capgemini.gestionDeStock.dto.LigneCommandeClientDto;
import com.capgemini.gestionDeStock.model.EtatCommande;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.capgemini.gestionDeStock.utils.Constants.APP_ROOT;

@Api(APP_ROOT + "/commandesClients")
public interface CommandeClientApi {

    @PostMapping(APP_ROOT + "/commandesClients/create")
    ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto dto);

    @PatchMapping(APP_ROOT + "/commandesClients/update/etat/{idCommande}/{etatCommande}")
    ResponseEntity<CommandeClientDto> updateEtatCommande(@PathVariable("idCommande") Integer idCommande,@PathVariable("etatCommande") EtatCommande etatCommande);

    @PatchMapping(APP_ROOT + "/commandesClients/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    ResponseEntity<CommandeClientDto> updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
                                                             @PathVariable("idLigneCommande") Integer idLigneCommande,
                                                             @PathVariable("quantite") BigDecimal quantite);

    @PatchMapping(APP_ROOT + "/commandesClients/update/client/{idCommande}/{idClient}")
    ResponseEntity<CommandeClientDto> updateClient(@PathVariable("idCommande") Integer idCommande,@PathVariable("idClient") Integer idClient);

    @PatchMapping(APP_ROOT + "/commandesClients/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    ResponseEntity<CommandeClientDto> updateArticle(@PathVariable("idCommande") Integer idCommande,@PathVariable("idLigneCommande") Integer idLigneCommande ,@PathVariable("idArticle") Integer idArticle);

    @DeleteMapping(APP_ROOT + "/commandesClients/delete/article/{idCommande}/{idLigneCommande}")
    ResponseEntity<CommandeClientDto> deleteArticle(@PathVariable("idCommande") Integer idCommande,@PathVariable("idLigneCommande") Integer idLigneCommande);


    @GetMapping(APP_ROOT + "/commandesClients/{idCommandeClient}")
    ResponseEntity<CommandeClientDto> findById(@PathVariable Integer idCommandeClient);

    @GetMapping(APP_ROOT + "/commandesClients/{codeCommandeClient}")
    ResponseEntity<CommandeClientDto> findByCode(@PathVariable("codeCommandeClient") String code);

    @GetMapping(APP_ROOT + "/commandesClients/all")
    ResponseEntity<List<CommandeClientDto>> findAll();

    @GetMapping(APP_ROOT + "/commandesClients/lignesCommande/{idCommande}")
    ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Integer idCommande);

    @DeleteMapping(APP_ROOT + "/commandesClients/delete/{idCommandeClient}")
    ResponseEntity<Void> delete(@PathVariable("idCommandeClient") Integer id);
}
