package br.com.fiap.amanaje.leituras.mqtt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MqttPropertiesTest {

	@Test
	void shouldExposeDefaultValues() {
		MqttProperties properties = new MqttProperties();

		assertThat(properties.isEnabled()).isFalse();
		assertThat(properties.getBrokerUrl()).isEqualTo("tcp://broker.hivemq.com:1883");
		assertThat(properties.getClientId()).isEqualTo("amanaje-java-api");
		assertThat(properties.getUsername()).isEmpty();
		assertThat(properties.getPassword()).isEmpty();
		assertThat(properties.getTelemetryTopic()).isEqualTo("amanaje/estacoes/+/telemetria");
		assertThat(properties.getFeedbackTopicPattern()).isEqualTo("amanaje/estacoes/%s/feedback");
		assertThat(properties.isEvaluateRiskOnMessage()).isTrue();
	}

}
