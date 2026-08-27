package com.senai.back.rental.Repositories;

import com.senai.back.rental.Models.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    // Lista movimentações de um equipamento, ordenadas pela data decrescente
    List<Movimentacao> findByEquipamentoIdOrderByDataMovimentacaoDesc(Long equipamentoId);

    // Lista movimentações de um usuário
    List<Movimentacao> findByUsuarioId(Long usuarioId);

    // Lista todas as movimentações de um tipo específico (entrada ou saída)
    List<Movimentacao> findByTipo(String tipo);

    // Lista movimentações de um equipamento entre datas (se necessário)
    List<Movimentacao> findByEquipamentoIdAndDataMovimentacaoBetween(Long equipamentoId, java.time.LocalDate inicio,
            java.time.LocalDate fim);
}