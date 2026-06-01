package br.com.fiap.amanaje.indicadores.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.fiap.amanaje.riscos.NivelRisco;
import br.com.fiap.amanaje.riscos.TipoRisco;

public record IndicadorRegionalResponse(
		Long idIndicador,
		Long idRegiao,
		String estado,
		String cidade,
		String nomeRegiao,
		TipoRisco tipoRisco,
		BigDecimal scoreMedio,
		NivelRisco nivelRiscoMedio,
		Integer quantidadeEstacoes,
		Integer quantidadeAlertasAtivos,
		String fonteCalculo,
		LocalDateTime dtCalculo) {
}
