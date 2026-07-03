package com.alces.deportes.config;

import com.alces.deportes.model.Usuario;
import com.alces.deportes.model.enums.RolUsuario;
import com.alces.deportes.repository.UsuarioRepository;
import com.alces.deportes.util.Sha256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        crearUsuario("admin", "admin123", RolUsuario.ADMINISTRADOR, "Administrador Los Alces");
        crearUsuario("tesorero", "tesorero123", RolUsuario.TESORERO, "Tesorero Club");
        crearUsuario("entrenador", "entrenador123", RolUsuario.ENTRENADOR, "Entrenador Principal");
        crearUsuario("socio1", "socio123", RolUsuario.SOCIO, "Socio Demo");
    }

    private void crearUsuario(String username, String password, RolUsuario rol, String nombre) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPasswordHash(Sha256Util.hash(password));
        usuario.setRol(rol);
        usuario.setNombre(nombre);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }
}
