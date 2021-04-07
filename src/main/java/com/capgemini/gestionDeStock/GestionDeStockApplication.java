package com.capgemini.gestionDeStock;

import com.capgemini.gestionDeStock.dto.UtilisateurDto;
import com.capgemini.gestionDeStock.model.Utilisateur;
import com.capgemini.gestionDeStock.repository.UtilisateurRepository;
import com.capgemini.gestionDeStock.services.UtilisateurService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GestionDeStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionDeStockApplication.class, args);
	}

	@Bean
	public CommandLineRunner sampleData(UtilisateurRepository repository){
        return (args -> {
			repository.save(new Utilisateur("Larbi","Ben","test.gmail.com","password"));
		});
	}

}
