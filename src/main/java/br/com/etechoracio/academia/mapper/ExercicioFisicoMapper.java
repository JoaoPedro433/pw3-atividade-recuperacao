package br.com.etechoracio.academia.mapper;

import br.com.etechoracio.academia.dto.ExercicioFisicoRequest;
import br.com.etechoracio.academia.dto.ExercicioFisicoResponse;
import br.com.etechoracio.academia.entity.ExercicioFisico;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExercicioFisicoMapper {

    ExercicioFisico dtoToEntity(ExercicioFisicoRequest request);

    ExercicioFisicoResponse entityToDto(ExercicioFisico exercicioFisico);

    List<ExercicioFisicoResponse> entityToDto(List<ExercicioFisico> exercicios);

}
