package br.com.etechoracio.academia.dto;

import br.com.etechoracio.academia.enums.NivelDificuldadeEnum;

public record ExercicioFisicoRequest(

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
