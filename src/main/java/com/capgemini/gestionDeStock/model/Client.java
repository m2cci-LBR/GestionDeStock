package com.capgemini.gestionDeStock.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Client")
public class Client extends AbstractEntity{
    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String  prenom;

    @Embedded
    private Adresse adresse;

    @Column(name = "photo")
    private String photo;

    @Column(name = "mail")
    private String  mail;

    @Column(name = "numTel")
    private String numTel;

    @OneToMany(mappedBy = "client")
    private List<CommandeClient> commandeClients;

    @Column(name = "idEntreprise")
    private Integer idEntreprise;

}
