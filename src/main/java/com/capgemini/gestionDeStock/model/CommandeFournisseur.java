package com.capgemini.gestionDeStock.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CommandeFournisseur")
public class CommandeFournisseur extends AbstractEntity{

    @Column(name = "code")
    private String code;

    @Column(name = "dateCommande")
    private Instant dateCommande;

    @Column(name = "idEntreprise")
    private Integer idEntreprise;

    @Column(name = "etatCommande")
    private EtatCommande etatCommande;

    @ManyToOne
    @JoinColumn(name = "idFournisseur")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commandeFournisseurs")
    private List<LigneCommandeFournisseur> ligneCommandeFournisseurs;


}
