package br.com.fiap.amanaje.leituras.mqtt;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MqttLeituraPayload(
		Long idEstacao,
		String codigoEstacao,
		LocalDateTime dtLeitura,
		BigDecimal distanciaAguaCm,
		BigDecimal nivelAguaPercentual,
		BigDecimal inclinacaoGraus,
		BigDecimal vibracao,
		BigDecimal pressaoHpa,
		BigDecimal pm25,
		BigDecimal pm10) {
}
