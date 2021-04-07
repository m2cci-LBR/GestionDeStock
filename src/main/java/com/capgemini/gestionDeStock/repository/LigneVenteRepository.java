package com.capgemini.gestionDeStock.repository;

import com.capgemini.gestionDeStock.model.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneVenteRepository extends JpaRepository<LigneVente, Integer> {

}
