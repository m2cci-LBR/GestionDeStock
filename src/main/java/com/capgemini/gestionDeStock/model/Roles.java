package com.capgemini.gestionDeStock.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Roles")
public class Roles extends AbstractEntity {

    @Column(name = "roleName")
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "idUtilisateur")
    private Utilisateur utilisateur;

    @Column(name = "idEntreprise")
    private Integer idEntreprise;
}
