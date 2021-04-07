package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.UtilisateurDto;
import com.capgemini.gestionDeStock.exceptions.EntityNotFoundException;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidEntityException;
import com.capgemini.gestionDeStock.repository.UtilisateurRepository;
import com.capgemini.gestionDeStock.services.UtilisateurService;
import com.capgemini.gestionDeStock.validator.UtilisateurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {
        List<String> errors = UtilisateurValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Utilisateur is not valid {}" , dto);
            throw new InvalidEntityException("Utilisateur non valide" , ErrorCodes.UTILISATEUR_NOT_VALID,errors);
        }
        return UtilisateurDto.fromEntity(utilisateurRepository.save(UtilisateurDto.toEntity(dto)));
    }

    @Override
    public UtilisateurDto findById(Integer id) {
        return null;
    }

    @Override
    public UtilisateurDto findByEmail(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email)
                                    .map(UtilisateurDto::fromEntity)
                                     .orElseThrow(() -> new EntityNotFoundException("Aucun utilisateur avec le mail" + email, ErrorCodes.UTILISATEUR_NOT_FOUND));
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurRepository.findAll().stream()
                                    .map(UtilisateurDto::fromEntity)
                                    .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Utilisateur ID is NULL");
            return;
        }
        utilisateurRepository.deleteById(id);
    }
}
