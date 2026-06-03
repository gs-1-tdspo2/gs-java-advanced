package br.com.fiap.amanaje.leituras.mqtt;

import br.com.fiap.amanaje.riscos.enums.NivelRisco;

public final class LedStatusMapper {

	private LedStatusMapper() {
	}

	public static String toLed(NivelRisco nivelRisco) {
		return switch (nivelRisco) {
			case BAIXO -> "GREEN";
			case MODERADO -> "YELLOW";
			case ALTO -> "ORANGE";
			case CRITICO -> "RED";
		};
	}

	public static boolean isAlerta(NivelRisco nivelRisco) {
		return nivelRisco == NivelRisco.ALTO || nivelRisco == NivelRisco.CRITICO;
	}

	public static String mensagem(NivelRisco nivelRisco) {
		return switch (nivelRisco) {
			case BAIXO -> "Condição estável. Monitoramento preventivo ativo.";
			case MODERADO -> "Atenção: risco moderado detectado. Manter monitoramento.";
			case ALTO -> "Risco alto detectado. Acionar protocolo preventivo.";
			case CRITICO -> "Risco crítico detectado. Acionar alerta preventivo imediatamente.";
		};
	}

}
