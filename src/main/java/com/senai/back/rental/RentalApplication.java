package com.senai.back.rental;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.senai.back.rental.models.Usuario;
import com.senai.back.rental.repositories.UsuarioRepository;

@SpringBootApplication
public class RentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalApplication.class, args);
    }

    @Bean
    public CommandLineRunner initTestUser(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Criptografa a senha do João se ele existir no banco
            usuarioRepository.findByEmail("joao@email.com").ifPresent(joao -> {
                joao.setSenha(passwordEncoder.encode("senha123"));
                usuarioRepository.save(joao);
                System.out.println(">>> SENHA DO JOÃO ATUALIZADA COM SUCESSO! <<<");
            });

            // 2. Garante o usuário Admin com todos os campos obrigatórios
            String emailAdmin = "admin@email.com";
            Usuario admin = usuarioRepository.findByEmail(emailAdmin).orElseGet(Usuario::new);
            admin.setEmail(emailAdmin);
            admin.setSenha(passwordEncoder.encode("123456"));
            admin.setNome("Usuario Teste");
            admin.setCpf("00000000000");
            admin.setEstado("RJ");
            admin.setCidade("Petropolis");
            admin.setLogradouro("Rua de Teste");
            admin.setNumero("123");
            admin.setCep("25600000");
            admin.setTelefone("24999999999");

            usuarioRepository.save(admin);
            System.out.println("=========================================");
            System.out.println("SISTEMA PRONTO PARA LOGIN");
            System.out.println("=========================================");
        };
    }
}