package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.EntrepriseDto;
import com.capgemini.gestionDeStock.dto.RoleDto;
import com.capgemini.gestionDeStock.dto.UtilisateurDto;
import com.capgemini.gestionDeStock.exceptions.EntityNotFoundException;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidEntityException;
import com.capgemini.gestionDeStock.repository.EntrepriseRepository;
import com.capgemini.gestionDeStock.repository.RolesRepository;
import com.capgemini.gestionDeStock.services.EntrepriseService;
import com.capgemini.gestionDeStock.services.UtilisateurService;
import com.capgemini.gestionDeStock.validator.EntrepriseValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Transactional
@Service
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService {

    private EntrepriseRepository entrepriseRepository;
    private UtilisateurService utilisateurService;
    private RolesRepository rolesRepository;

    @Autowired
    public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository,
                                 UtilisateurService utilisateurService,
                                 RolesRepository rolesRepository) {
        this.entrepriseRepository = entrepriseRepository;
        this.utilisateurService = utilisateurService;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public EntrepriseDto save(EntrepriseDto dto) {
        List<String> errors = EntrepriseValidator.validate(dto);
        if (!errors.isEmpty()){
          log.error("Entreprise is not valid {}", dto);
          throw new InvalidEntityException("Entreprise non valide", ErrorCodes.ENTREPRISE_NOT_FOUND);
        }
        EntrepriseDto savedEntreprise = EntrepriseDto.fromEntity(
                      entrepriseRepository.save(EntrepriseDto.toEntity(dto)));

        UtilisateurDto utilisateur = fromEntreprise(savedEntreprise);
        UtilisateurDto savedUser = utilisateurService.save(utilisateur);

        RoleDto roleDto = RoleDto.builder()
                                 .roleName("ADMIN")
                                 .utilisateur(savedUser)
                                 .build();

        rolesRepository.save(RoleDto.toEntity(roleDto));

        return savedEntreprise;
    }

    private UtilisateurDto fromEntreprise(EntrepriseDto dto){
        return UtilisateurDto.builder()
                             .adresse(dto.getAdresse())
                             .nom(dto.getNom())
                             .prenom(dto.getCodeFiscal())
                             .email(dto.getEmail())
                             .motDePasse(generateRandomPassword())
                             .entreprise(dto)
                             .dateDeNaissance(Instant.now())
                             .photo(dto.getPhoto())
                             .build();
    }

    private String generateRandomPassword(){ return "password" ;}

    @Override
    public EntrepriseDto findById(Integer id) {
        if (id == null){
            log.error("Entreprise ID is NULL");
            return null;
        }

        return entrepriseRepository.findById(id)
                                    .map(EntrepriseDto::fromEntity)
                                    .orElseThrow(() -> new EntityNotFoundException("Entreprise non trouvee",ErrorCodes.ENTREPRISE_NOT_FOUND));
    }

    @Override
    public List<EntrepriseDto> findAll() {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
