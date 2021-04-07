package com.capgemini.gestionDeStock.repository;

import com.capgemini.gestionDeStock.model.MvtStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStkRepository extends JpaRepository<MvtStock,Integer> {

    @Query("select sum(m.quantite) from MvtStk m where m.article.id= :idArticle")
    BigDecimal stockReelArticle(@Param("idArticle") Integer idArticle);

    List<MvtStock> findAllByArticleId(Integer idArticle);
}
