package com.senai.back.rental.Repositories;

import com.senai.back.rental.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca usuário pelo email (para autenticação)
    Optional<Usuario> findByEmail(String email);

    // Verifica se existe usuário com determinado CPF
    boolean existsByCpf(String cpf);

    // Verifica se existe usuário com determinado email
    boolean existsByEmail(String email);
}