package br.com.etechoracio.academia.dto;

import br.com.etechoracio.academia.enums.NivelDificuldadeEnum;
import jakarta.persistence.Column;

public record ExercicioFisicoResponse(
        Long id,

        String nome,

        String grupoMuscular,

        String imagem,

        String descricao,

        Integer series,

        int repeticoes,

        double cargaSugerida,

        NivelDificuldadeEnum nivelDificuldade

) {
}
