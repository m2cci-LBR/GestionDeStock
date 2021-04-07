package com.capgemini.gestionDeStock.dto;
import com.capgemini.gestionDeStock.model.Fournisseur;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FournisseurDto {

    private Integer id;
    private String nom;
    private String prenom;
    private AdresseDto adresse;
    private String photo;
    private String mail;
    private  String numTel;
    private List<CommandeFournisseurDto> commandeFournisseurs;

    public static FournisseurDto fromEntity(Fournisseur fournisseur){
        if (fournisseur == null){
            return null;
        }
        return FournisseurDto.builder()
                             .id(fournisseur.getId())
                             .nom(fournisseur.getNom())
                             .prenom(fournisseur.getPrenom())
                             .photo(fournisseur.getPhoto())
                             .mail(fournisseur.getMail())
                             .numTel(fournisseur.getNumTel())
                             .build();
    }

    public static Fournisseur toEntity(FournisseurDto dto){
        if (dto == null){
            return null;
        }
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(dto.getId());
        fournisseur.setNom(dto.getNom());
        fournisseur.setPrenom(dto.getPrenom());
        fournisseur.setPhoto(dto.getPhoto());
        fournisseur.setMail(dto.getMail());
        fournisseur.setNumTel(dto.getNumTel());
        return fournisseur;
    }

}
