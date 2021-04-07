package com.capgemini.gestionDeStock.services;

import com.capgemini.gestionDeStock.dto.ClientDto;

import java.util.List;

public interface ClientService {
    ClientDto save(ClientDto dto);

    ClientDto findById(Integer id);

    ClientDto findByCodeClient(String codeClient);

    List<ClientDto> findAll();

    void delete(Integer id);
}
