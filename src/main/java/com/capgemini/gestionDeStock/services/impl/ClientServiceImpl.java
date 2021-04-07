package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.ClientDto;
import com.capgemini.gestionDeStock.services.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Override
    public ClientDto save(ClientDto dto) {
        return null;
    }

    @Override
    public ClientDto findById(Integer id) {
        return null;
    }

    @Override
    public ClientDto findByCodeClient(String codeClient) {
        return null;
    }

    @Override
    public List<ClientDto> findAll() {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
