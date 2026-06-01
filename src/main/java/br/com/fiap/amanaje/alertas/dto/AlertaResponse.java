package br.com.fiap.amanaje.alertas.dto;

import java.time.LocalDateTime;

import br.com.fiap.amanaje.alertas.StatusAlerta;
import br.com.fiap.amanaje.alertas.TipoAlerta;
import br.com.fiap.amanaje.riscos.NivelRisco;

public record AlertaResponse(
		Long idAlerta,
		Long idRegiao,
		Long idAvaliacao,
		TipoAlerta tipoAlerta,
		NivelRisco nivelRisco,
		String titulo,
		String descricao,
		String recomendacao,
		StatusAlerta statusAlerta,
		LocalDateTime dtAlerta,
		LocalDateTime dtResolvidoEm,
		String stAtivo,
		LocalDateTime dtCriadoEm,
		LocalDateTime dtAtualizadoEm) {
}
