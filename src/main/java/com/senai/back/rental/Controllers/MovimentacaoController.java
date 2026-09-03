package com.senai.back.rental.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.senai.back.rental.models.Equipamento;
import com.senai.back.rental.models.Movimentacao;
import com.senai.back.rental.models.Usuario;
import com.senai.back.rental.services.EquipamentoService;
import com.senai.back.rental.services.MovimentacaoService;
import com.senai.back.rental.services.UsuarioService;

@Controller
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    private EquipamentoService equipamentoService;
    private MovimentacaoService movimentacaoService;
    private UsuarioService usuarioService;

    public MovimentacaoController(EquipamentoService equipamentoService, MovimentacaoService movimentacaoService,
            UsuarioService usuarioService) {
        this.equipamentoService = equipamentoService;
        this.movimentacaoService = movimentacaoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarParaMovimentar(Model model) {
        List<Equipamento> equipamentos = equipamentoService.listarOrdenadoAlfabeticamente();
        model.addAttribute("equipamentos", equipamentos);
        model.addAttribute("hoje", LocalDate.now());
        return "movimentacoes/gestao";
    }

    @PostMapping
    public String registrar(@RequestParam("equipamentoId") Long equipamentoId,
            @RequestParam("tipo") String tipo,
            @RequestParam("quantidade") Integer quantidade,
            @RequestParam("data") String data,
            Authentication authentication,
            RedirectAttributes redirect) {
        try {
            Usuario usuario = usuarioService.buscarPorEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            LocalDate dataMov = LocalDate.parse(data);
            StringBuilder alerta = new StringBuilder();
            movimentacaoService.registrarMovimentacao(equipamentoId, tipo, quantidade, dataMov, usuario, alerta);

            if (alerta.length() > 0) {
                redirect.addFlashAttribute("alerta", alerta.toString());
            } else {
                redirect.addFlashAttribute("mensagem", "Movimentação registrada com sucesso!");
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/movimentacoes";
    }

    @GetMapping("/historico/{equipamentoId}")
    public String historico(@PathVariable Long equipamentoId, Model model) {
        Equipamento equipamento = equipamentoService.buscarPorId(equipamentoId);
        List<Movimentacao> movimentacoes = movimentacaoService.historicoPorEquipamento(equipamentoId);
        model.addAttribute("equipamento", equipamento);
        model.addAttribute("movimentacoes", movimentacoes);
        return "movimentacoes/historico";
    }
}