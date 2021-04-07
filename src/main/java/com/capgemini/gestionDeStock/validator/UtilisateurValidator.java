package com.capgemini.gestionDeStock.validator;

import com.capgemini.gestionDeStock.dto.UtilisateurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UtilisateurValidator {

    public static List<String> validate(UtilisateurDto utilisateurDto){
        List<String> errors=new ArrayList<>();
        if (utilisateurDto == null){
            errors.add("Veuillez renseigner le nom de l'utilisateur");
            errors.add("Veuillez renseigner le prenom de l'utilisateur");
            errors.add("Veuillez renseigner le mot de passe de l'utilisateur");
            errors.add("Veuillez renseigner l'adresse de l'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getNom())){
            errors.add("Veuillez renseigner le nom de l'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getPrenom())){
            errors.add("Veuillez renseigner le prenom de l'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getEmail())){
            errors.add("Veuillez renseigner le mail de l'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getMotDePasse())){
            errors.add("Veuillez renseigner le mot de passe de l'utilisateur");
        }
        if (utilisateurDto.getDateDeNaissance() == null){
            errors.add("Veuillez renseigner la date de naissance de l'utilisateur");
        }
        if (utilisateurDto.getAdresse() == null){
            errors.add("Veuillez renseigner l'adresse de l'utilisateur");
        } else {
            if (!StringUtils.hasLength(utilisateurDto.getAdresse().getAdresse1())){
                errors.add("Le champ adresse 1 est obligatoire");
            }
            if (!StringUtils.hasLength(utilisateurDto.getAdresse().getVille())){
                errors.add("Le champ ville est obligatoire");
            }
            if (!StringUtils.hasLength(utilisateurDto.getAdresse().getPays())){
                errors.add("Le champ pays est obligatoire");
            }
        }
        return errors;
    }
}
