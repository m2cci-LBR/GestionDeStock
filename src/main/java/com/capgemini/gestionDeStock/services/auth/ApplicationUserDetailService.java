package com.capgemini.gestionDeStock.services.auth;

import com.capgemini.gestionDeStock.dto.UtilisateurDto;
import com.capgemini.gestionDeStock.model.auth.ExtendedUser;
import com.capgemini.gestionDeStock.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationUserDetailService implements UserDetailsService {

    @Autowired
    private UtilisateurService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UtilisateurDto utilisateur = service.findByEmail(email);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        utilisateur.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));

        return new ExtendedUser(utilisateur.getEmail(), utilisateur.getMotDePasse(), utilisateur.getEntreprise().getId(),authorities);
    }
}
