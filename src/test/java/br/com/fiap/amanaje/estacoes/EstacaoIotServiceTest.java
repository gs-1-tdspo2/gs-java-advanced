package br.com.fiap.amanaje.estacoes;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import br.com.fiap.amanaje.common.exception.BusinessRuleException;
import br.com.fiap.amanaje.estacoes.dto.EstacaoCreateRequest;
import br.com.fiap.amanaje.regioes.RegiaoMonitorada;
import br.com.fiap.amanaje.regioes.RegiaoMonitoradaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EstacaoIotServiceTest {

	@Mock
	private EstacaoIotRepository estacaoRepository;

	@Mock
	private RegiaoMonitoradaService regiaoService;

	@InjectMocks
	private EstacaoIotService estacaoService;

	@Test
	void shouldRejectDuplicatedStationCode() {
		EstacaoCreateRequest request = new EstacaoCreateRequest(
				10L,
				"EST-001",
				"Estação Encosta Norte",
				TipoEstacao.REAL,
				StatusEstacao.ATIVA,
				null,
				null);
		when(regiaoService.buscarAtiva(10L)).thenReturn(RegiaoMonitorada.builder().idRegiao(10L).build());
		when(estacaoRepository.findByCodigoEstacao("EST-001"))
				.thenReturn(Optional.of(EstacaoIot.builder().idEstacao(20L).build()));

		assertThatThrownBy(() -> estacaoService.criar(request))
				.isInstanceOf(BusinessRuleException.class)
				.hasMessage("Código de estação já cadastrado: EST-001");
	}

}
