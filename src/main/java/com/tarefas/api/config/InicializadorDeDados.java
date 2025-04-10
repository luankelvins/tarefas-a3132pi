package com.tarefas.api.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tarefas.api.constants.StatusUsuario;
import com.tarefas.api.model.Usuario;
import com.tarefas.api.repository.UsuarioRepository;

@Configuration
public class InicializadorDeDados {

    @Bean
    CommandLineRunner initUsuarios(UsuarioRepository usuarioRepository) {
        return args -> {
            if (!usuarioRepository.existsByEmail("admin@sgp.com")) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@sgp.com");
                admin.setSenha(new BCryptPasswordEncoder().encode("admin123")); // senha criptografada
                admin.setCpf("000.000.000-00");
                admin.setStatus(StatusUsuario.ATIVO);
                admin.setDataNascimento(LocalDate.of(1990, 1, 1));

                usuarioRepository.save(admin);
                System.out.println("✅ Usuário admin criado com sucesso!");
            } else {
                System.out.println("ℹ️ Usuário admin já existe.");
            }
        };
    }
}