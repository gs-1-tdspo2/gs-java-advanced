package br.com.fiap.amanaje.riscos.service;

import br.com.fiap.amanaje.alertas.service.AlertaService;

import br.com.fiap.amanaje.riscos.enums.TipoRisco;

import br.com.fiap.amanaje.riscos.enums.NivelRisco;

import br.com.fiap.amanaje.riscos.repository.AvaliacaoRiscoRepository;

import br.com.fiap.amanaje.riscos.service.RiscoService;

import br.com.fiap.amanaje.riscos.model.AvaliacaoRisco;

import br.com.fiap.amanaje.observacoes.repository.ObservacaoClimaticaRepository;

import br.com.fiap.amanaje.leituras.repository.LeituraIotRepository;

import br.com.fiap.amanaje.regioes.service.RegiaoMonitoradaService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import br.com.fiap.amanaje.alertas.service.AlertaService;
import br.com.fiap.amanaje.common.exception.BusinessRuleException;
import br.com.fiap.amanaje.leituras.model.LeituraIot;
import br.com.fiap.amanaje.leituras.repository.LeituraIotRepository;
import br.com.fiap.amanaje.observacoes.model.ObservacaoClimatica;
import br.com.fiap.amanaje.observacoes.repository.ObservacaoClimaticaRepository;
import br.com.fiap.amanaje.regioes.model.RegiaoMonitorada;
import br.com.fiap.amanaje.regioes.service.RegiaoMonitoradaService;
import br.com.fiap.amanaje.riscos.dto.AvaliarRiscoResponse;
import br.com.fiap.amanaje.riscos.dto.RiscoAtualResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RiscoServiceTest {

	@Mock
	private AvaliacaoRiscoRepository avaliacaoRepository;

	@Mock
	private LeituraIotRepository leituraRepository;

	@Mock
	private ObservacaoClimaticaRepository observacaoRepository;

	@Mock
	private RegiaoMonitoradaService regiaoService;

	@Mock
	private AlertaService alertaService;

	@InjectMocks
	private RiscoService riscoService;

	@Test
	void shouldRejectEvaluationWithoutAnySourceData() {
		when(regiaoService.buscarAtiva(20L)).thenReturn(RegiaoMonitorada.builder().idRegiao(20L).build());
		when(leituraRepository.findFirstByIdRegiaoAndStValidaOrderByDtLeituraDesc(20L, "S"))
				.thenReturn(Optional.empty());
		when(observacaoRepository.findFirstByIdRegiaoOrderByDtObservacaoDesc(20L))
				.thenReturn(Optional.empty());

		assertThatThrownBy(() -> riscoService.avaliar(20L))
				.isInstanceOf(BusinessRuleException.class)
				.hasMessage("Não há leitura IoT válida nem observação climática para avaliar a região: 20");
	}

	@Test
	void shouldCreateFourEvaluationsFromLatestSources() {
		LeituraIot leitura = LeituraIot.builder()
				.idLeitura(30L)
				.nivelAguaPercentual(BigDecimal.valueOf(100))
				.distanciaAguaCm(BigDecimal.ZERO)
				.inclinacaoGraus(BigDecimal.valueOf(45))
				.vibracao(BigDecimal.valueOf(10))
				.pressaoHpa(BigDecimal.valueOf(800))
				.pm25(BigDecimal.valueOf(75))
				.pm10(BigDecimal.valueOf(150))
				.build();
		ObservacaoClimatica observacao = ObservacaoClimatica.builder()
				.idObservacao(40L)
				.precipitacaoMm(BigDecimal.valueOf(100))
				.ventoKmh(BigDecimal.valueOf(100))
				.build();
		when(regiaoService.buscarAtiva(20L)).thenReturn(RegiaoMonitorada.builder().idRegiao(20L).build());
		when(leituraRepository.findFirstByIdRegiaoAndStValidaOrderByDtLeituraDesc(20L, "S"))
				.thenReturn(Optional.of(leitura));
		when(observacaoRepository.findFirstByIdRegiaoOrderByDtObservacaoDesc(20L))
				.thenReturn(Optional.of(observacao));
		when(avaliacaoRepository.save(any(AvaliacaoRisco.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));
		when(alertaService.gerarSeNecessario(any(AvaliacaoRisco.class)))
				.thenReturn(Optional.empty());

		AvaliarRiscoResponse response = riscoService.avaliar(20L);

		assertThat(response.avaliacoes()).hasSize(4);
		assertThat(response.avaliacoes())
				.allSatisfy(avaliacao -> {
					assertThat(avaliacao.idLeitura()).isEqualTo(30L);
					assertThat(avaliacao.idObservacao()).isEqualTo(40L);
					assertThat(avaliacao.scoreRisco()).isEqualByComparingTo(BigDecimal.valueOf(100));
					assertThat(avaliacao.nivelRisco()).isEqualTo(NivelRisco.CRITICO);
				});
		verify(avaliacaoRepository, times(4)).save(any(AvaliacaoRisco.class));
		verify(alertaService, times(4)).gerarSeNecessario(any(AvaliacaoRisco.class));
	}

	@Test
	void shouldResolveExactRiskBoundaries() {
		assertThat(riscoService.resolveNivelRisco(BigDecimal.valueOf(24))).isEqualTo(NivelRisco.BAIXO);
		assertThat(riscoService.resolveNivelRisco(BigDecimal.valueOf(25))).isEqualTo(NivelRisco.MODERADO);
		assertThat(riscoService.resolveNivelRisco(BigDecimal.valueOf(50))).isEqualTo(NivelRisco.ALTO);
		assertThat(riscoService.resolveNivelRisco(BigDecimal.valueOf(75))).isEqualTo(NivelRisco.CRITICO);
		assertThat(riscoService.clampScore(120)).isEqualByComparingTo(BigDecimal.valueOf(100));
		assertThat(riscoService.clampScore(-10)).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@Test
	void shouldReturnHighestLatestRiskAsConsolidatedCurrentRisk() {
		when(regiaoService.buscarAtiva(20L)).thenReturn(RegiaoMonitorada.builder().idRegiao(20L).build());
		when(avaliacaoRepository.findFirstByIdRegiaoAndTipoRiscoOrderByDtAvaliacaoDesc(20L, TipoRisco.ENCHENTE))
				.thenReturn(Optional.of(avaliacao(TipoRisco.ENCHENTE, 60)));
		when(avaliacaoRepository.findFirstByIdRegiaoAndTipoRiscoOrderByDtAvaliacaoDesc(20L, TipoRisco.DESLIZAMENTO))
				.thenReturn(Optional.of(avaliacao(TipoRisco.DESLIZAMENTO, 30)));
		when(avaliacaoRepository.findFirstByIdRegiaoAndTipoRiscoOrderByDtAvaliacaoDesc(20L, TipoRisco.TEMPESTADE))
				.thenReturn(Optional.of(avaliacao(TipoRisco.TEMPESTADE, 80)));
		when(avaliacaoRepository.findFirstByIdRegiaoAndTipoRiscoOrderByDtAvaliacaoDesc(20L, TipoRisco.QUALIDADE_AR))
				.thenReturn(Optional.of(avaliacao(TipoRisco.QUALIDADE_AR, 10)));

		RiscoAtualResponse response = riscoService.buscarAtual(20L);

		assertThat(response.avaliacoes()).hasSize(4);
		assertThat(response.scoreConsolidado()).isEqualByComparingTo(BigDecimal.valueOf(80));
		assertThat(response.nivelConsolidado()).isEqualTo(NivelRisco.CRITICO);
	}

	private AvaliacaoRisco avaliacao(TipoRisco tipoRisco, int score) {
		BigDecimal scoreRisco = BigDecimal.valueOf(score);
		return AvaliacaoRisco.builder()
				.idRegiao(20L)
				.tipoRisco(tipoRisco)
				.scoreRisco(scoreRisco)
				.nivelRisco(riscoService.resolveNivelRisco(scoreRisco))
				.build();
	}

}
