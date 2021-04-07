package com.capgemini.gestionDeStock.controllers;

import com.capgemini.gestionDeStock.dto.auth.AuthenticationRequest;
import com.capgemini.gestionDeStock.dto.auth.AuthenticationResponse;
import com.capgemini.gestionDeStock.model.auth.ExtendedUser;
import com.capgemini.gestionDeStock.services.auth.ApplicationUserDetailService;
import com.capgemini.gestionDeStock.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.capgemini.gestionDeStock.utils.Constants.AUTHENTICATION_ENDPOINT;

@RestController
@RequestMapping(AUTHENTICATION_ENDPOINT)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationUserDetailService userDetailService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticatee(@RequestBody AuthenticationRequest request){
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getLogin(),
            request.getPassword()
    ));
      final UserDetails userDetails = userDetailService.loadUserByUsername(request.getLogin());

      final String jwt = jwtUtils.generateToken((ExtendedUser) userDetails);

      return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(jwt).build());
    }
}
