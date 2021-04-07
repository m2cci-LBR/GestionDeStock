package com.capgemini.gestionDeStock.services.strategy;

import com.capgemini.gestionDeStock.dto.ArticleDto;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidOperationException;
import com.capgemini.gestionDeStock.model.Article;
import com.capgemini.gestionDeStock.services.ArticleService;
import com.capgemini.gestionDeStock.services.FlickrService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("articleStrategy")
@Slf4j
public class SaveArticlePhoto implements Strategy<ArticleDto>{

    private FlickrService flickrService;
    private ArticleService articleService;

    @Autowired
    public SaveArticlePhoto(FlickrService flickrService, ArticleService articleService) {
        this.flickrService = flickrService;
        this.articleService = articleService;
    }

    @Override
    public ArticleDto savePhoto(Integer id ,InputStream photo, String titre) throws FlickrException {
        ArticleDto article = articleService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo,titre);
        if (StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException("Erreur lors du save de la photo de l'article", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }
        article.setPhoto(urlPhoto);

        return articleService.save(article);
    }
}
