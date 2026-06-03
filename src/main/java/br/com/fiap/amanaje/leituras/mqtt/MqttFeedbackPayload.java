package br.com.fiap.amanaje.leituras.mqtt;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.fiap.amanaje.riscos.enums.NivelRisco;
import br.com.fiap.amanaje.riscos.enums.TipoRisco;

public record MqttFeedbackPayload(
		String codigoEstacao,
		Long idRegiao,
		NivelRisco nivelRisco,
		TipoRisco tipoRiscoPrincipal,
		BigDecimal score,
		boolean alerta,
		String led,
		String mensagem,
		LocalDateTime timestamp) {
}
