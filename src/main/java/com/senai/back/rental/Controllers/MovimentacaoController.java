package com.senai.back.rental.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senai.back.rental.models.Movimentacao;
import com.senai.back.rental.models.Usuario;
import com.senai.back.rental.services.EquipamentoService;
import com.senai.back.rental.services.MovimentacaoService;
import com.senai.back.rental.services.UsuarioService;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    private final EquipamentoService equipamentoService;
    private final MovimentacaoService movimentacaoService;
    private final UsuarioService usuarioService;

    public MovimentacaoController(EquipamentoService equipamentoService,
                                  MovimentacaoService movimentacaoService,
                                  UsuarioService usuarioService) {
        this.equipamentoService = equipamentoService;
        this.movimentacaoService = movimentacaoService;
        this.usuarioService = usuarioService;
    }

    // Estrutura para receber o corpo da requisição JSON no POST
    public record MovimentacaoRequest(Long equipamentoId, String tipo, Integer quantidade, String data) {}

    // 1. Registrar Movimentação (POST /api/movimentacoes)
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody MovimentacaoRequest request, Authentication authentication) {
        try {
            // O Spring Security injeta o 'Authentication' automaticamente com base no Bearer Token
            Usuario usuario = usuarioService.buscarPorEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            LocalDate dataMov = LocalDate.parse(request.data());
            StringBuilder alerta = new StringBuilder();

            movimentacaoService.registrarMovimentacao(
                    request.equipamentoId(),
                    request.tipo(),
                    request.quantidade(),
                    dataMov,
                    usuario,
                    alerta
            );

            if (alerta.length() > 0) {
                return ResponseEntity.ok(Map.of("alerta", alerta.toString()));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensagem", "Movimentação registrada com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // 2. Histórico por Equipamento (GET /api/movimentacoes/historico/{equipamentoId})
    @GetMapping("/historico/{equipamentoId}")
    public ResponseEntity<List<Movimentacao>> historico(@PathVariable Long equipamentoId) {
        List<Movimentacao> movimentacoes = movimentacaoService.historicoPorEquipamento(equipamentoId);
        return ResponseEntity.ok(movimentacoes);
    }
}
