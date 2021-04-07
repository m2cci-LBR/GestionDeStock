package com.capgemini.gestionDeStock.services.impl;

import com.capgemini.gestionDeStock.dto.ArticleDto;
import com.capgemini.gestionDeStock.exceptions.EntityNotFoundException;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidEntityException;
import com.capgemini.gestionDeStock.model.Article;
import com.capgemini.gestionDeStock.repository.ArticleRepository;
import com.capgemini.gestionDeStock.services.ArticleService;
import com.capgemini.gestionDeStock.validator.ArticleValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;

    @Autowired  //Injection du repository par constructeur
    public ArticleServiceImpl(ArticleRepository articleRepository){
         this.articleRepository=articleRepository;
    }

    @Override
    public ArticleDto save(ArticleDto dto) {
        List<String> errors= ArticleValidator.validate(dto);
        if (!errors.isEmpty()){
            log.error("Article is not valid {} " , dto);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID,errors);
        }
        return ArticleDto.fromEntity(articleRepository.save(ArticleDto.toEntity(dto)));

    }

    @Override
    public ArticleDto findById(Integer id) {
        if (id == null){
            log.error("Article ID is null");
            return null;
        }
        Optional<Article> article = articleRepository.findById(id);
        ArticleDto dto=ArticleDto.fromEntity(article.get());

        return Optional.of(dto)
                       .orElseThrow(() ->
                                       new EntityNotFoundException("Article non trouve" +id,
                                                                    ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {
        if (!StringUtils.hasLength(codeArticle)){
            log.error("Article code is null");
        }
         Optional<Article> article = articleRepository.findArticleByCodeArticle(codeArticle);
         ArticleDto dto=ArticleDto.fromEntity(article.get());

        return Optional.of(dto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Article non trouve avec le code" + codeArticle,
                                ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<ArticleDto> findAll() {
        return articleRepository.findAll()
                                 .stream()
                                 .map(ArticleDto::fromEntity)
                                 .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Article ID is null");
            return; // Pour quitter la methode
        }
        articleRepository.deleteById(id);
    }
}
