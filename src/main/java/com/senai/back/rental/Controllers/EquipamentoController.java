package com.senai.back.rental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.senai.back.rental.models.Equipamento;
import com.senai.back.rental.services.EquipamentoService;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    // 1. Listar ou buscar por termo (GET /api/equipamentos ou /api/equipamentos?termo=furadeira)
    @GetMapping
    public ResponseEntity<List<Equipamento>> listar(@RequestParam(value = "termo", required = false) String termo) {
        List<Equipamento> equipamentos = equipamentoService.buscarPorTermo(termo);
        return ResponseEntity.ok(equipamentos);
    }

    // 2. Buscar por ID (GET /api/equipamentos/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Equipamento> buscarPorId(@PathVariable Long id) {
        Equipamento equipamento = equipamentoService.buscarPorId(id);
        if (equipamento == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(equipamento);
    }

    // 3. Cadastrar equipamento (POST /api/equipamentos)
    @PostMapping
    public ResponseEntity<Equipamento> salvar(@RequestBody Equipamento equipamento) {
        Equipamento novoEquipamento = equipamentoService.salvar(equipamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEquipamento);
    }

    // 4. Atualizar equipamento (PUT /api/equipamentos/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Equipamento> atualizar(@PathVariable Long id, @RequestBody Equipamento equipamento) {
        equipamento.setId(id.intValue());
        Equipamento atualizado = equipamentoService.salvar(equipamento);
        return ResponseEntity.ok(atualizado);
    }

    // 5. Excluir equipamento (DELETE /api/equipamentos/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        equipamentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}