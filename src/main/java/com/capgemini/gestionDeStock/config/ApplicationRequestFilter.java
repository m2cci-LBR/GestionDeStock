package com.capgemini.gestionDeStock.config;

import com.capgemini.gestionDeStock.services.auth.ApplicationUserDetailService;
import com.capgemini.gestionDeStock.utils.JwtUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApplicationRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ApplicationUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
      final String authHeader = request.getHeader("Authorization");
      String userEmail = null;
      String jwt = null ;
      String idEntreprise = null;

      if (StringUtils.hasLength(authHeader) && authHeader.startsWith("Bearer ")){
          jwt = authHeader.substring(7); // Car Bearer contient 7 carateres
          userEmail = jwtUtils.extractUsername(jwt);
          idEntreprise = jwtUtils.extractIdEntreprise(jwt);
      }

      if (StringUtils.hasLength(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null){
          UserDetails userDetails = this.userDetailService.loadUserByUsername(userEmail);
          if (jwtUtils.validateToken(jwt,userDetails)) { //Pour verifier que le token a ete genere pour cet utilisateur
              UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                      userDetails,null, userDetails.getAuthorities()
              );
              usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
          }
      }
      MDC.put("idEntreprise",idEntreprise);
      chain.doFilter(request,response);
    }
}