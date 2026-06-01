package br.com.fiap.amanaje.common.response;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

	@GetMapping("/health")
	public HealthResponse health() {
		return new HealthResponse(
				"Amanajé API",
				"UP",
				"API principal do Amanajé em execução",
				Instant.now());
	}

	public record HealthResponse(String application, String status, String message, Instant timestamp) {
	}

}
