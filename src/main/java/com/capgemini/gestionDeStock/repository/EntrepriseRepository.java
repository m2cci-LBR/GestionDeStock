package com.capgemini.gestionDeStock.repository;

import com.capgemini.gestionDeStock.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {
}
