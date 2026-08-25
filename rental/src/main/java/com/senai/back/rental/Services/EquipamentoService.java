package com.senai.back.rental.Services;

import com.senai.back.rental.Models.Equipamento;
import com.senai.back.rental.Repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    public List<Equipamento> listarTodos() {
        return equipamentoRepository.findAll();
    }

    public List<Equipamento> buscarPorTermo(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarTodos();
        }
        return equipamentoRepository.buscarPorTermo(termo);
    }

    public Equipamento salvar(Equipamento equipamento) {
        // Validações adicionais podem ser feitas aqui
        return equipamentoRepository.save(equipamento);
    }

    public Equipamento buscarPorId(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));
    }

    public void excluir(Long id) {
        equipamentoRepository.deleteById(id);
    }

    // Método para listar equipamentos ordenados alfabeticamente
    // utilizando algoritmo de ordenação (Selection Sort)
    public List<Equipamento> listarOrdenadoAlfabeticamente() {
        List<Equipamento> equipamentos = new ArrayList<>(equipamentoRepository.findAll());
        // Selection Sort por marca (ou modelo)
        for (int i = 0; i < equipamentos.size() - 1; i++) {
            int indiceMenor = i;
            for (int j = i + 1; j < equipamentos.size(); j++) {
                String marcaAtual = equipamentos.get(j).getMarca() != null ? equipamentos.get(j).getMarca() : "";
                String marcaMenor = equipamentos.get(indiceMenor).getMarca() != null
                        ? equipamentos.get(indiceMenor).getMarca()
                        : "";
                if (marcaAtual.compareToIgnoreCase(marcaMenor) < 0) {
                    indiceMenor = j;
                }
            }
            if (indiceMenor != i) {
                Collections.swap(equipamentos, i, indiceMenor);
            }
        }
        return equipamentos;
    }
}