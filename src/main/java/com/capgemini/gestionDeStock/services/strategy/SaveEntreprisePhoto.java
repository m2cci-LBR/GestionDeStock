package com.capgemini.gestionDeStock.services.strategy;

import com.capgemini.gestionDeStock.dto.EntrepriseDto;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidOperationException;
import com.capgemini.gestionDeStock.services.EntrepriseService;
import com.capgemini.gestionDeStock.services.FlickrService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("entrepriseStrategy")
@Slf4j
public class SaveEntreprisePhoto implements Strategy<EntrepriseDto>{

    private FlickrService flickrService;
    private EntrepriseService entrepriseService;

    @Autowired
    public SaveEntreprisePhoto(FlickrService flickrService, EntrepriseService entrepriseService) {
        this.flickrService = flickrService;
        this.entrepriseService = entrepriseService;
    }

    @Override
    public EntrepriseDto savePhoto(Integer id,InputStream photo, String titre) throws FlickrException {
        EntrepriseDto entreprise = entrepriseService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo,titre);
        if (StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException("Erreur lors du save de la photo de l'entreprise", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }
        entreprise.setPhoto(urlPhoto);
        return entrepriseService.save(entreprise);
    }
}
