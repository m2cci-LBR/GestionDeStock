package com.capgemini.gestionDeStock.validator;

import com.capgemini.gestionDeStock.dto.ClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {

    public static List<String> validate(ClientDto dto){
        List<String> errors = new ArrayList<>();
        if (!StringUtils.hasLength(dto.getNom())){
            errors.add("Veullez renseigner le nom du client");
        }

        return errors;
    }
}
