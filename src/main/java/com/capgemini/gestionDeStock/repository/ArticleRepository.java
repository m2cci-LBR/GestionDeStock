package com.capgemini.gestionDeStock.repository;

import com.capgemini.gestionDeStock.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findArticleByCodeArticle(String codeArticle);
}
