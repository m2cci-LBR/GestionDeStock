package com.capgemini.gestionDeStock.services.strategy;

import com.capgemini.gestionDeStock.dto.ClientDto;
import com.capgemini.gestionDeStock.exceptions.ErrorCodes;
import com.capgemini.gestionDeStock.exceptions.InvalidOperationException;
import com.capgemini.gestionDeStock.services.ClientService;
import com.capgemini.gestionDeStock.services.FlickrService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("clientStrategy")
@Slf4j
public class SaveClientPhoto implements Strategy<ClientDto>{

    private FlickrService flickrService;
    private ClientService clientService;

    @Autowired
    public SaveClientPhoto(FlickrService flickrService, ClientService clientService) {
        this.flickrService = flickrService;
        this.clientService = clientService;
    }

    @Override
    public ClientDto savePhoto(Integer id ,InputStream photo, String titre) throws FlickrException {
        ClientDto client = clientService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo,titre);
        if (StringUtils.hasLength(urlPhoto)){
            throw new InvalidOperationException("Erreur lors du save de la photo du client", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }

        client.setPhoto(urlPhoto);
        return clientService.save(client);
    }
}
