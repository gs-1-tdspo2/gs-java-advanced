package br.com.fiap.amanaje.riscos.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.fiap.amanaje.riscos.NivelRisco;

public record RiscoAtualResponse(
		Long idRegiao,
		BigDecimal scoreConsolidado,
		NivelRisco nivelConsolidado,
		List<AvaliacaoRiscoResponse> avaliacoes) {
}
