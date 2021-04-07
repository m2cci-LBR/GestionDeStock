package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.Entreprise;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class EntrepriseDto {

    private Integer id;
    private String nom;
    private String prenom;
    private String description;
    private AdresseDto adresse;
    private String codeFiscal;
    private String photo;
    private String email;
    private String numTel;
    private String siteWeb;
    private List<UtilisateurDto> utilisateurs;

    public  static EntrepriseDto fromEntity(Entreprise entreprise){
        if (entreprise == null){
            return null;
        }

        return EntrepriseDto.builder()
                            .id(entreprise.getId())
                            .build();
    }


    public static Entreprise toEntity(EntrepriseDto dto){
        if (dto == null){
            return null;
        }

        Entreprise entreprise = new Entreprise();
        entreprise.setId(dto.getId());
        return entreprise;
    }
}
