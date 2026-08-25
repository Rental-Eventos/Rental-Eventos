package com.senai.back.rental.Repository;

import com.senai.back.rental.Models.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    // Busca equipamentos cujo modelo, marca ou categoria contenham o termo
    // (ignorando maiúsculas)
    @Query("SELECT e FROM Equipamento e WHERE " +
            "LOWER(e.marca) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(e.modelo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(e.categoria) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Equipamento> buscarPorTermo(@Param("termo") String termo);

    // Lista todos os equipamentos com status ativo
    List<Equipamento> findByStatus(String status);

    // Verifica se existe equipamento com determinada marca e modelo
    boolean existsByMarcaAndModelo(String marca, String modelo);
}