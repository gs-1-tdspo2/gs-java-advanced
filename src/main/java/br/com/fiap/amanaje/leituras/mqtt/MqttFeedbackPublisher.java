package br.com.fiap.amanaje.leituras.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MqttFeedbackPublisher implements DisposableBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(MqttFeedbackPublisher.class);
	private static final int QOS = 1;

	private final MqttProperties properties;
	private final ObjectMapper objectMapper;
	private MqttClient client;

	public MqttFeedbackPublisher(MqttProperties properties, ObjectMapper objectMapper) {
		this.properties = properties;
		this.objectMapper = objectMapper;
	}

	public void publish(MqttFeedbackPayload payload) {
		if (!properties.isEnabled()) {
			return;
		}
		String topic = String.format(properties.getFeedbackTopicPattern(), payload.codigoEstacao());
		try {
			MqttClient mqttClient = connectedClient();
			MqttMessage message = new MqttMessage(toJson(payload).getBytes(StandardCharsets.UTF_8));
			message.setQos(QOS);
			message.setRetained(false);
			mqttClient.publish(topic, message);
			LOGGER.info("Feedback MQTT publicado em topic={} codigoEstacao={} nivelRisco={}",
					topic,
					payload.codigoEstacao(),
					payload.nivelRisco());
		}
		catch (MqttException | JsonProcessingException ex) {
			LOGGER.error("Falha ao publicar feedback MQTT em topic={} codigoEstacao={}: {}",
					topic,
					payload.codigoEstacao(),
					ex.getMessage(),
					ex);
		}
	}

	private String toJson(MqttFeedbackPayload payload) throws JsonProcessingException {
		return objectMapper.writeValueAsString(payload);
	}

	private synchronized MqttClient connectedClient() throws MqttException {
		if (client == null) {
			String clientId = properties.getClientId() + "-publisher-" + UUID.randomUUID();
			client = new MqttClient(properties.getBrokerUrl(), clientId, new MemoryPersistence());
		}
		if (!client.isConnected()) {
			client.connect(connectOptions());
		}
		return client;
	}

	private MqttConnectOptions connectOptions() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		options.setAutomaticReconnect(true);
		if (StringUtils.hasText(properties.getUsername())) {
			options.setUserName(properties.getUsername());
		}
		if (StringUtils.hasText(properties.getPassword())) {
			options.setPassword(properties.getPassword().toCharArray());
		}
		return options;
	}

	@Override
	public void destroy() throws Exception {
		if (client != null && client.isConnected()) {
			client.disconnect();
			client.close();
		}
	}

}
