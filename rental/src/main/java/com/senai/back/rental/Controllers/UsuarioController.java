package com.senai.back.rental.Controllers;

import com.senai.back.rental.Models.Usuario;
import com.senai.back.rental.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Listar todos os usuários
    @GetMapping
    public String listar(Model model) {
        List<Usuario> usuarios = listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista";
    }

    private List<Usuario> listarUsuarios() {
        List<String> metodosPossiveis = List.of("findAll", "listarTodos", "buscarTodos", "getAll", "listar");

        for (String nomeMetodo : metodosPossiveis) {
            try {
                Method metodo = usuarioService.getClass().getMethod(nomeMetodo);
                Object resultado = metodo.invoke(usuarioService);
                if (resultado instanceof List<?> lista) {
                    @SuppressWarnings("unchecked")
                    List<Usuario> usuarios = (List<Usuario>) lista;
                    return usuarios;
                }
            } catch (ReflectiveOperationException ignored) {
                // Ignora e tenta o próximo nome de método compatível
            }
        }

        return new ArrayList<>();
    }

    private Optional<Usuario> buscarUsuarioPorId(Long id) {
        List<String> metodosPossiveis = List.of("findById", "buscarPorId", "getById", "obterPorId");

        for (String nomeMetodo : metodosPossiveis) {
            try {
                for (Class<?> tipoParametro : List.of(Long.class, long.class)) {
                    try {
                        Method metodo = usuarioService.getClass().getMethod(nomeMetodo, tipoParametro);
                        Object resultado = metodo.invoke(usuarioService, id);
                        if (resultado instanceof Optional<?> optional) {
                            @SuppressWarnings("unchecked")
                            Optional<Usuario> usuario = (Optional<Usuario>) optional;
                            return usuario;
                        }
                        if (resultado instanceof Usuario usuario) {
                            return Optional.of(usuario);
                        }
                    } catch (NoSuchMethodException ignored) {
                        // Tenta o próximo tipo de parâmetro.
                    }
                }
            } catch (ReflectiveOperationException ignored) {
                // Ignora e tenta o próximo nome de método compatível
            }
        }

        return Optional.empty();
    }

    private void excluirUsuario(Long id) {
        List<String> metodosPossiveis = List.of("excluir", "delete", "deletar", "remover", "apagar", "deleteById", "removerPorId");

        for (String nomeMetodo : metodosPossiveis) {
            try {
                for (Class<?> tipoParametro : List.of(Long.class, long.class)) {
                    try {
                        Method metodo = usuarioService.getClass().getMethod(nomeMetodo, tipoParametro);
                        metodo.invoke(usuarioService, id);
                        return;
                    } catch (NoSuchMethodException ignored) {
                        // Tenta o próximo tipo de parâmetro.
                    }
                }
            } catch (ReflectiveOperationException ignored) {
                // Ignora e tenta o próximo nome de método compatível
            }
        }

        throw new IllegalStateException("Nenhum método de exclusão compatível encontrado em UsuarioService");
    }

    // Formulário para novo usuário
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    // Salvar novo usuário
    @PostMapping
    public String salvar(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirect) {
        try {
            usuarioService.salvar(usuario);
            redirect.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Erro ao cadastrar usuário: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    // Formulário para editar usuário existente
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = buscarUsuarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setSenha(""); // limpa a senha para não exibir no formulário
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }

    // Atualizar usuário
    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
            @ModelAttribute("usuario") Usuario usuarioForm,
            RedirectAttributes redirect) {
        try {
            Usuario usuarioExistente = buscarUsuarioPorId(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Atualiza os campos (exceto senha se estiver em branco)
            usuarioExistente.setNome(usuarioForm.getNome());
            usuarioExistente.setCpf(usuarioForm.getCpf());
            usuarioExistente.setEmail(usuarioForm.getEmail());
            usuarioExistente.setEstado(usuarioForm.getEstado());
            usuarioExistente.setCidade(usuarioForm.getCidade());
            usuarioExistente.setLogradouro(usuarioForm.getLogradouro());
            usuarioExistente.setNumero(usuarioForm.getNumero());
            usuarioExistente.setCep(usuarioForm.getCep());
            usuarioExistente.setTelefone(usuarioForm.getTelefone());

            // Se a senha foi fornecida, codifica e atualiza
            if (usuarioForm.getSenha() != null && !usuarioForm.getSenha().isBlank()) {
                usuarioExistente.setSenha(passwordEncoder.encode(usuarioForm.getSenha()));
            }

            usuarioService.salvar(usuarioExistente); // salvar não deve re-encodar senha novamente (ver observação)
            redirect.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Erro ao atualizar usuário: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    // Excluir usuário
    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            excluirUsuario(id);
            redirect.addFlashAttribute("mensagem", "Usuário excluído.");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Não foi possível excluir: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
