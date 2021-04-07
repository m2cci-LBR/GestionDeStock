package com.capgemini.gestionDeStock.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "MvtStock")
public class MvtStock extends AbstractEntity {

    @Column(name = "dateMvt")
    private Instant dateMvt;

    @Column(name = "quantite")
    private BigDecimal quantite;

    @ManyToOne
    @JoinColumn(name = "idArticle")
    private Article article;

    @Column(name = "typeMvtStock")
    private TypeMvtStock typeMvtStock;

    @Column(name = "sourcemvt")
    private SourceMvtStk sourceMvt;

    @Column(name = "idEntreprise")
    private Integer idEntreprise;
}
