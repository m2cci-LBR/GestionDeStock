package com.capgemini.gestionDeStock.repository;

import com.capgemini.gestionDeStock.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}
