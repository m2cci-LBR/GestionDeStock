package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.Utilisateur;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class UtilisateurDto {

    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private Instant dateDeNaissance;
    private String motDePasse;
    private AdresseDto adresse;
    private String photo;
    private EntrepriseDto entreprise;
    private List<RoleDto> roles;


    public static UtilisateurDto fromEntity(Utilisateur utilisateur){
        if (utilisateur == null){
            return null;
        }
        return UtilisateurDto.builder()
                             .id(utilisateur.getId())
                             .nom(utilisateur.getNom())
                             .prenom(utilisateur.getPrenom())
                             .email(utilisateur.getEmail())
                             .build();
    }


    public static Utilisateur toEntity(UtilisateurDto dto){
        if (dto == null){
            return null;
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(dto.getId());
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        return utilisateur;
    }
}
