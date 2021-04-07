package com.capgemini.gestionDeStock.dto;

import com.capgemini.gestionDeStock.model.Roles;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {

    private Integer id;
    private String roleName;
    private UtilisateurDto utilisateur;

    public static RoleDto fromEntity(Roles roles){
        if (roles == null){
            return null;
        }

        return RoleDto.builder()
                      .id(roles.getId())
                      .roleName(roles.getRoleName())
                      .build();
    }

    public static Roles toEntity(RoleDto dto){
        if (dto == null){
            return null;
        }

        Roles roles = new Roles();
        roles.setId(dto.getId());
        roles.setRoleName(roles.getRoleName());
        return roles;
    }
}
