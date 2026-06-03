package br.com.fiap.amanaje;

import br.com.fiap.amanaje.leituras.mqtt.MqttProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MqttProperties.class)
public class AmanajeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmanajeApiApplication.class, args);
	}

}
