package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.CommandeFournisseur;
import com.capgemini.gestionDeStock.model.EtatCommande;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class CommandeFournisseurDto {

    private Integer id;

    private String code;

    private Instant dateCommande;

    private EtatCommande etatCommande;

    private Integer idEntreprise;

    private FournisseurDto fournisseur;

    @JsonIgnore
    private List<LigneCommandeFournisseurDto> ligneCommandeFournisseurs;

    public static CommandeFournisseurDto fromEntity(CommandeFournisseur commandeFournisseur) {
        if (commandeFournisseur == null){
            return null;
        }
        return CommandeFournisseurDto.builder()
                                     .id(commandeFournisseur.getId())
                                     .code(commandeFournisseur.getCode())
                                     .dateCommande(commandeFournisseur.getDateCommande())
                                     .fournisseur(FournisseurDto.fromEntity(commandeFournisseur.getFournisseur()))
                                     .etatCommande(commandeFournisseur.getEtatCommande())
                                     .idEntreprise(commandeFournisseur.getIdEntreprise())
                                     .build();
    }

    public static CommandeFournisseur toEntity(CommandeFournisseurDto dto){
        if (dto == null){
            return null;
        }
        CommandeFournisseur commandeFournisseur = new CommandeFournisseur();
        commandeFournisseur.setId(dto.getId());
        commandeFournisseur.setCode(dto.getCode());
        commandeFournisseur.setDateCommande(dto.getDateCommande());
        commandeFournisseur.setFournisseur(FournisseurDto.toEntity(dto.getFournisseur()));
        commandeFournisseur.setEtatCommande(dto.getEtatCommande());
        commandeFournisseur.setIdEntreprise(dto.getIdEntreprise());
        return commandeFournisseur;
    }

    public boolean isCommandeLivree(){
        return EtatCommande.LIVREE.equals(this.etatCommande);
    }

}
