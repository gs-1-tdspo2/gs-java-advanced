package br.com.fiap.amanaje.leituras.mqtt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

import br.com.fiap.amanaje.riscos.dto.AvaliacaoRiscoResponse;
import br.com.fiap.amanaje.riscos.dto.AvaliarRiscoResponse;
import br.com.fiap.amanaje.riscos.enums.NivelRisco;
import br.com.fiap.amanaje.riscos.enums.TipoRisco;
import org.springframework.stereotype.Component;

@Component
public class MqttFeedbackPayloadFactory {

	public MqttFeedbackPayload fromRisk(String codigoEstacao, Long idRegiao, AvaliarRiscoResponse risco) {
		AvaliacaoRiscoResponse principal = risco.avaliacoes().stream()
				.max(Comparator
						.comparing(AvaliacaoRiscoResponse::scoreRisco)
						.thenComparing(avaliacao -> avaliacao.tipoRisco().name()))
				.orElseThrow();
		return build(
				codigoEstacao,
				idRegiao,
				principal.nivelRisco(),
				principal.tipoRisco(),
				principal.scoreRisco());
	}

	public MqttFeedbackPayload monitoringOnly(String codigoEstacao, Long idRegiao) {
		return build(codigoEstacao, idRegiao, NivelRisco.BAIXO, null, BigDecimal.ZERO);
	}

	private MqttFeedbackPayload build(
			String codigoEstacao,
			Long idRegiao,
			NivelRisco nivelRisco,
			TipoRisco tipoRiscoPrincipal,
			BigDecimal score) {
		return new MqttFeedbackPayload(
				codigoEstacao,
				idRegiao,
				nivelRisco,
				tipoRiscoPrincipal,
				score,
				LedStatusMapper.isAlerta(nivelRisco),
				LedStatusMapper.toLed(nivelRisco),
				LedStatusMapper.mensagem(nivelRisco),
				LocalDateTime.now());
	}

}
