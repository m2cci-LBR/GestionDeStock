package com.capgemini.gestionDeStock.controllers.api;

import com.capgemini.gestionDeStock.dto.ArticleDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.capgemini.gestionDeStock.utils.Constants.APP_ROOT;

@Api(APP_ROOT + "/articles")
public interface ArticleApi {

    @PostMapping(value = APP_ROOT + "/articles/create",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Enregistrer un article (Ajouter/Modifier)" , notes = "cette methode permet d'ajouter ou modifier un article",response = ArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200 , message = "L'objet Article cree / modifie"),
            @ApiResponse(code = 400 , message = "L'objet article n'est pas valide")
    })
    ArticleDto save(@RequestBody ArticleDto dto);

    @GetMapping(value = APP_ROOT + "/articles/{idArticle}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Rechercher un article par ID" , notes = "Cette methode permet de chercher un article par son ID" , response = ArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200 , message = "L'article a ete trouve dans la BDD"),
            @ApiResponse(code = 404 , message = "Aucun article avec l'ID fourni n'existe dans la BDD")
    })
    ArticleDto findById(@PathVariable("idArticle") Integer id);

    @GetMapping(value = APP_ROOT + "/articles/{codeArticle}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Rechercher un article par CODE" , notes = "Cette methode permet de chercher un article par son ID" , response = ArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200 , message = "L'article a ete trouve dans la BDD"),
            @ApiResponse(code = 404 , message = "Aucun article avec le CODE fourni n'existe dans la BDD")
    })
    ArticleDto findByCodeArticle(@PathVariable("codeArticle") String codeArticle);

    @GetMapping(value = APP_ROOT + "/articles/all",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Renvoi la liste des articles" , notes = "Cette methode permet de chercher et renvoyer la liste des articles dans la BDD" , responseContainer = "List<ArticleDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200 , message = "La liste des articles / Une liste vide")
    })
    List<ArticleDto> findAll();

    @DeleteMapping(value = APP_ROOT + "/articles/delete/{idArticle}")
    @ApiOperation(value = "Supprimer un article" , notes = "Cette methode permet de supprimer un article par son ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200 , message = "L'article a ete supprime")
    })
    void delete(@PathVariable("idArticle") Integer id);
}
