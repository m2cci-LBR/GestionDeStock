package com.capgemini.gestionDeStock.controllers.api;

import com.capgemini.gestionDeStock.dto.ClientDto;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.capgemini.gestionDeStock.utils.Constants.APP_ROOT;

@Api(APP_ROOT + "/clients")
public interface ClientApi {

    @PostMapping(value = APP_ROOT + "/clients/create",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    ClientDto save(@RequestBody ClientDto dto);

    @GetMapping(value = APP_ROOT + "/clients/{idClient}",produces = MediaType.APPLICATION_JSON_VALUE)
    ClientDto findById(@PathVariable("idClient") Integer idClient);

    @GetMapping(value = APP_ROOT + "/clients/all",produces = MediaType.APPLICATION_JSON_VALUE)
    List<ClientDto> findAll();

    @DeleteMapping(value = APP_ROOT + "/clients/delete/{idClient}")
    void delete(@PathVariable("idClient") Integer id);
}
