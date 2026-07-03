package com.alces.deportes.dto.auth;

import com.alces.deportes.model.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String username;
    private String nombre;
    private RolUsuario rol;
    private String mensaje;
}
