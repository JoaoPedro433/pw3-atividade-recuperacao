package br.com.etechoracio.academia.service;

import br.com.etechoracio.academia.dto.ExercicioFisicoResponse;
import br.com.etechoracio.academia.entity.ExercicioFisico;
import br.com.etechoracio.academia.mapper.ExercicioFisicoMapper;
import br.com.etechoracio.academia.repository.ExercicioFisicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ExercicioFisicoService {

    @Autowired
    private ExercicioFisicoRepository exercicioFisicoRepository;

    @Autowired
    private ExercicioFisicoMapper exercicioFisicoMapper;

    public List<ExercicioFisicoResponse> buscarPorExerciciosAprovador() {

       List <ExercicioFisico> exerciciosAprovados = exercicioFisicoRepository.buscarExerciciosAprovados();

        return exercicioFisicoMapper.entityToDto(exerciciosAprovados);
    }

    public ExercicioFisicoResponse buscarExerciciosAprovadoPorId(Long id) {

        ExercicioFisico exercicioAprovadoIndividual = exercicioFisicoRepository.findByIdAndAprovadoTrue(id)
                                                                                .orElseThrow(() -> new ResponseStatusException(
                                                                                        HttpStatus.NOT_FOUND,
                                                                                        ("Exercício aprovado não encontrado")));

        return exercicioFisicoMapper.entityToDto(exercicioAprovadoIndividual);
    }
}
