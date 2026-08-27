package com.senai.back.rental.Services;

import com.senai.back.rental.Models.Equipamento;
import com.senai.back.rental.Models.Movimentacao;
import com.senai.back.rental.Models.Usuario;
import com.senai.back.rental.Repositories.EquipamentoRepository;
import com.senai.back.rental.Repositories.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Transactional
    public void registrarMovimentacao(Long equipamentoId, String tipo, Integer quantidade,
            LocalDate data, Usuario usuario, StringBuilder alerta) {
        Equipamento equip = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));

        // Validações
        if (quantidade == null || quantidade <= 0) {
            throw new RuntimeException("Quantidade deve ser maior que zero");
        }

        if ("saida".equalsIgnoreCase(tipo)) {
            if (equip.getQuantidadeDisponivel() < quantidade) {
                throw new RuntimeException("Estoque insuficiente para saída");
            }
            equip.setQuantidadeDisponivel(equip.getQuantidadeDisponivel() - quantidade);
        } else if ("entrada".equalsIgnoreCase(tipo)) {
            equip.setQuantidadeDisponivel(equip.getQuantidadeDisponivel() + quantidade);
        } else {
            throw new RuntimeException("Tipo de movimentação inválido");
        }

        // Salva equipamento atualizado
        equipamentoRepository.save(equip);

        // Cria e salva a movimentação
        Movimentacao mov = new Movimentacao();
        mov.setDataMovimentacao(data);
        mov.setTipo(tipo.toLowerCase());
        mov.setQuantidade(quantidade);
        mov.setEquipamento(equip);
        mov.setUsuario(usuario);
        movimentacaoRepository.save(mov);

        // Verifica estoque mínimo após a operação (especialmente para saída)
        if ("saida".equalsIgnoreCase(tipo) && equip.getQuantidadeDisponivel() < equip.getQuantidadeMinima()) {
            if (alerta != null) {
                alerta.append("ALERTA: Estoque abaixo do mínimo para o equipamento ")
                        .append(equip.getMarca()).append(" ").append(equip.getModelo())
                        .append(". Quantidade atual: ").append(equip.getQuantidadeDisponivel());
            }
        }
    }

    public List<Movimentacao> historicoPorEquipamento(Long equipamentoId) {
        return movimentacaoRepository.findByEquipamentoIdOrderByDataMovimentacaoDesc(equipamentoId);
    }

    public List<Movimentacao> listarTodas() {
        return movimentacaoRepository.findAll();
    }
}