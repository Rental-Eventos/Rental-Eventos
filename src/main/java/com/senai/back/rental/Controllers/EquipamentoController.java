package com.senai.back.rental.Controllers;

import com.senai.back.rental.Models.Equipamento;
import com.senai.back.rental.Services.EquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/equipamentos")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    @GetMapping
    public String listar(@RequestParam(value = "termo", required = false) String termo, Model model) {
        List<Equipamento> equipamentos = equipamentoService.buscarPorTermo(termo);
        model.addAttribute("equipamentos", equipamentos);
        model.addAttribute("termo", termo);
        return "equipamentos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("equipamento", new Equipamento());
        return "equipamentos/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute("equipamento") Equipamento equipamento, RedirectAttributes redirect) {
        equipamentoService.salvar(equipamento);
        redirect.addFlashAttribute("mensagem", "Equipamento salvo com sucesso!");
        return "redirect:/equipamentos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Equipamento equipamento = equipamentoService.buscarPorId(id);
        model.addAttribute("equipamento", equipamento);
        return "equipamentos/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute("equipamento") Equipamento equipamento,
            RedirectAttributes redirect) {
        equipamento.setId(id.intValue());
        equipamentoService.salvar(equipamento);
        redirect.addFlashAttribute("mensagem", "Equipamento atualizado!");
        return "redirect:/equipamentos";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        equipamentoService.excluir(id);
        redirect.addFlashAttribute("mensagem", "Equipamento excluído.");
        return "redirect:/equipamentos";
    }
}