package br.com.fiap.amanaje.leituras;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import br.com.fiap.amanaje.leituras.dto.LeituraIotCreateRequest;
import br.com.fiap.amanaje.leituras.dto.LeituraIotResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Leituras IoT", description = "Recebimento HTTP e histórico de telemetria das estações IoT")
public class LeituraIotController {

	private final LeituraIotService leituraService;

	public LeituraIotController(LeituraIotService leituraService) {
		this.leituraService = leituraService;
	}

	@PostMapping("/leituras")
	@Operation(summary = "Receber leitura de telemetria por HTTP")
	public ResponseEntity<LeituraIotResponse> criar(@Valid @RequestBody LeituraIotCreateRequest request) {
		LeituraIotResponse response = leituraService.criar(request);
		return ResponseEntity
				.created(linkTo(methodOn(LeituraIotController.class).listarPorRegiao(response.idRegiao())).toUri())
				.body(response);
	}

	@GetMapping("/regioes/{id}/leituras")
	@Operation(summary = "Listar histórico de leituras por região")
	public List<LeituraIotResponse> listarPorRegiao(@PathVariable Long id) {
		return leituraService.listarPorRegiao(id);
	}

}
