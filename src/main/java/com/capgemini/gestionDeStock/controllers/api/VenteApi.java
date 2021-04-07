package com.capgemini.gestionDeStock.controllers.api;

import com.capgemini.gestionDeStock.dto.VentesDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.capgemini.gestionDeStock.utils.Constants.VENTES_ENDPOINT;

@Api(VENTES_ENDPOINT)
public interface VenteApi {

    @PostMapping(VENTES_ENDPOINT + "/create")
    VentesDto save(@RequestBody VentesDto dto);

    @GetMapping(VENTES_ENDPOINT + "/{idVente}")
    VentesDto findById(@PathVariable("idVente") Integer id);

    @GetMapping(VENTES_ENDPOINT + "/{codeVente}")
    VentesDto findByCode(@PathVariable("codeVente")String code);

    @GetMapping(VENTES_ENDPOINT + "/all")
    List<VentesDto> findAll();

    @GetMapping(VENTES_ENDPOINT + "/delete/{codeVente}")
    void delete(@PathVariable("idVente") Integer id);
}
