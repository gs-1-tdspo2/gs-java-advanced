package br.com.fiap.amanaje.leituras.mqtt;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.fiap.amanaje.leituras.service.LeituraIotService;
import br.com.fiap.amanaje.riscos.service.RiscoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;

@ExtendWith(MockitoExtension.class)
class MqttLeituraSubscriberTest {

	@Mock
	private LeituraIotService leituraService;

	@Mock
	private RiscoService riscoService;

	@Mock
	private MqttFeedbackPublisher feedbackPublisher;

	@Mock
	private MqttFeedbackPayloadFactory feedbackPayloadFactory;

	@Test
	void shouldNotConnectOrProcessWhenDisabled() {
		MqttProperties properties = new MqttProperties();
		properties.setEnabled(false);
		MqttLeituraSubscriber subscriber = new MqttLeituraSubscriber(
				properties,
				new ObjectMapper(),
				leituraService,
				riscoService,
				feedbackPublisher,
				feedbackPayloadFactory);

		subscriber.run(new DefaultApplicationArguments());

		verify(leituraService, never()).criar(org.mockito.ArgumentMatchers.any());
		verify(riscoService, never()).avaliar(org.mockito.ArgumentMatchers.any());
		verify(feedbackPublisher, never()).publish(org.mockito.ArgumentMatchers.any());
	}

}
