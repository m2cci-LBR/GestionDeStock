package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.Client;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientDto {

    private Integer id;
    private String nom;
    private String  prenom;
    private AdresseDto adresse;
    private String photo;
    private String  mail;
    private String numTel;
    private List<CommandeClientDto> commandeClients;

    public static ClientDto fromEntity(Client client){
        if (client == null){
            return null;
        }
        return ClientDto.builder()
                        .id(client.getId())
                        .nom(client.getNom())
                        .prenom(client.getPrenom())
                        .photo(client.getPhoto())
                        .mail(client.getMail())
                        .numTel(client.getNumTel())
                       // .commandeClients(CommandeClientDto.fromEntity(client.getCommandeClients()))
                        .build();
    }

    public static Client toEntity(ClientDto dto){
        if (dto == null){
            return null;
        }
        Client client = new Client();
        client.setId(dto.getId());
        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setPhoto(dto.getPhoto());
        client.setMail(dto.getMail());
        client.setNumTel(dto.getNumTel());
        return client;
    }

}
