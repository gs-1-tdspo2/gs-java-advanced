package br.com.fiap.amanaje.leituras.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "amanaje.mqtt")
public class MqttProperties {

	private boolean enabled = false;
	private String brokerUrl = "tcp://mqtt-dashboard.com:1883";
	private String clientId = "amanaje-java-api";
	private String username = "";
	private String password = "";
	private String telemetryTopic = "app/estacoes/+/telemetria";
	private String statusTopic = "app/estacoes/+/status";
	private String commandTopicPattern = "app/estacoes/%s/alertas";
	private boolean evaluateRiskOnMessage = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelemetryTopic() {
		return telemetryTopic;
	}

	public void setTelemetryTopic(String telemetryTopic) {
		this.telemetryTopic = telemetryTopic;
	}

	public String getStatusTopic() {
		return statusTopic;
	}

	public void setStatusTopic(String statusTopic) {
		this.statusTopic = statusTopic;
	}

	public String getCommandTopicPattern() {
		return commandTopicPattern;
	}

	public void setCommandTopicPattern(String commandTopicPattern) {
		this.commandTopicPattern = commandTopicPattern;
	}

	public boolean isEvaluateRiskOnMessage() {
		return evaluateRiskOnMessage;
	}

	public void setEvaluateRiskOnMessage(boolean evaluateRiskOnMessage) {
		this.evaluateRiskOnMessage = evaluateRiskOnMessage;
	}

}
