package br.com.etechoracio.academia.repository;

import br.com.etechoracio.academia.entity.ExercicioFisico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExercicioFisicoRepository extends JpaRepository<ExercicioFisico, Long> {

    @Query("SELECT e FROM ExercicioFisico e WHERE e.aprovado = true")
    List<ExercicioFisico> buscarExerciciosAprovados();

    Optional<ExercicioFisico> findByIdAndAprovadoTrue(Long id);
}
