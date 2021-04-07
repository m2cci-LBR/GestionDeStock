package com.capgemini.gestionDeStock.services.strategy;

import com.capgemini.gestionDeStock.dto.UtilisateurDto;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidOperationException;
import com.capgemini.gestionDeStock.model.Utilisateur;
import com.capgemini.gestionDeStock.services.FlickrService;
import com.capgemini.gestionDeStock.services.UtilisateurService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("utilisateurStrategy")
@Slf4j
public class SaveUtilisateurPhoto implements Strategy<UtilisateurDto>{

    private FlickrService flickrService;
    private UtilisateurService utilisateurService;

    @Autowired
    public SaveUtilisateurPhoto(FlickrService flickrService, UtilisateurService utilisateurService) {
        this.flickrService = flickrService;
        this.utilisateurService = utilisateurService;
    }

    @Override
    public UtilisateurDto savePhoto(Integer id,InputStream photo, String titre) throws FlickrException {
        UtilisateurDto utilisateur = utilisateurService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo,titre);
        if (StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException("Erreur lors du save de la photo de l'utilisateur", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }
        utilisateur.setPhoto(urlPhoto);
        return utilisateurService.save(utilisateur);
    }
}
