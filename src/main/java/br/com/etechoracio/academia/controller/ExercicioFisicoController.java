package br.com.etechoracio.academia.controller;

import br.com.etechoracio.academia.dto.ExercicioFisicoResponse;
import br.com.etechoracio.academia.entity.ExercicioFisico;
import br.com.etechoracio.academia.service.ExercicioFisicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exercicios-fisicos")
public class ExercicioFisicoController {

    @Autowired
    private ExercicioFisicoService exercicioFisicoService;

    @GetMapping("/buscar-por-aprovados")
    public ResponseEntity<List <ExercicioFisicoResponse>> buscarPorExerciciosAprovados() {

        var exercicioFisico = exercicioFisicoService.buscarPorExerciciosAprovador();

        return ResponseEntity.ok(exercicioFisico);
    }

    @GetMapping("/buscar-por-aprovado/{id}")
    public ResponseEntity<ExercicioFisicoResponse> buscarPorExerciciosAprovadoPorId(@PathVariable Long id) {

        var exercicioFisico = exercicioFisicoService.buscarExerciciosAprovadoPorId(id);

        return ResponseEntity.ok(exercicioFisico);
    }


}
