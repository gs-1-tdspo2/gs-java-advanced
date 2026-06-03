package br.com.fiap.amanaje.leituras.mqtt;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MqttLeituraPayloadTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void shouldParseSampleTelemetryJson() throws Exception {
		String json = """
				{
				  "codigoEstacao": "AMANAJE-SP-RP-001",
				  "distanciaAguaCm": 80,
				  "nivelAguaPercentual": 73,
				  "inclinacaoGraus": 18.5,
				  "vibracao": 0.72,
				  "pressaoHpa": 998.4,
				  "pm25": 118,
				  "pm10": 180
				}
				""";

		MqttLeituraPayload payload = objectMapper.readValue(json, MqttLeituraPayload.class);

		assertThat(payload.codigoEstacao()).isEqualTo("AMANAJE-SP-RP-001");
		assertThat(payload.distanciaAguaCm()).isEqualByComparingTo(BigDecimal.valueOf(80));
		assertThat(payload.nivelAguaPercentual()).isEqualByComparingTo(BigDecimal.valueOf(73));
		assertThat(payload.inclinacaoGraus()).isEqualByComparingTo("18.5");
		assertThat(payload.vibracao()).isEqualByComparingTo("0.72");
		assertThat(payload.pressaoHpa()).isEqualByComparingTo("998.4");
		assertThat(payload.pm25()).isEqualByComparingTo(BigDecimal.valueOf(118));
		assertThat(payload.pm10()).isEqualByComparingTo(BigDecimal.valueOf(180));
	}

}
