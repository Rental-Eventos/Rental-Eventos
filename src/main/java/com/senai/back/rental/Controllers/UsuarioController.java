package com.senai.back.rental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senai.back.rental.models.Usuario;
import com.senai.back.rental.repositories.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Listar todos os usuários (GET /api/usuarios)
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    // 2. Buscar por ID (GET /api/usuarios/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cadastrar novo usuário (POST /api/usuarios)
    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        // Criptografa a senha antes de salvar no banco
        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        Usuario novoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    // 4. Atualizar usuário (PUT /api/usuarios/{id})
    // 4. Atualizar usuário (PUT /api/usuarios/{id})
@PutMapping("/{id}")
public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuarioForm) {
    return usuarioRepository.findById(id).map(usuario -> {
        if (usuarioForm.getNome() != null) usuario.setNome(usuarioForm.getNome());
        if (usuarioForm.getCpf() != null) usuario.setCpf(usuarioForm.getCpf());
        if (usuarioForm.getEmail() != null) usuario.setEmail(usuarioForm.getEmail());
        if (usuarioForm.getEstado() != null) usuario.setEstado(usuarioForm.getEstado());
        if (usuarioForm.getCidade() != null) usuario.setCidade(usuarioForm.getCidade());
        if (usuarioForm.getLogradouro() != null) usuario.setLogradouro(usuarioForm.getLogradouro());
        if (usuarioForm.getNumero() != null) usuario.setNumero(usuarioForm.getNumero());
        if (usuarioForm.getCep() != null) usuario.setCep(usuarioForm.getCep());
        if (usuarioForm.getTelefone() != null) usuario.setTelefone(usuarioForm.getTelefone());

        // Se enviou uma nova senha válida, criptografa
        if (usuarioForm.getSenha() != null && !usuarioForm.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuarioForm.getSenha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }).orElse(ResponseEntity.notFound().build());
}

    // 5. Deletar usuário (DELETE /api/usuarios/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}