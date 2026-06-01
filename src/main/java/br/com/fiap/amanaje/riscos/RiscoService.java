package br.com.fiap.amanaje.riscos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import br.com.fiap.amanaje.alertas.AlertaService;
import br.com.fiap.amanaje.alertas.dto.AlertaResponse;
import br.com.fiap.amanaje.common.exception.BusinessRuleException;
import br.com.fiap.amanaje.common.exception.ResourceNotFoundException;
import br.com.fiap.amanaje.leituras.LeituraIot;
import br.com.fiap.amanaje.leituras.LeituraIotRepository;
import br.com.fiap.amanaje.observacoes.ObservacaoClimatica;
import br.com.fiap.amanaje.observacoes.ObservacaoClimaticaRepository;
import br.com.fiap.amanaje.regioes.RegiaoMonitoradaService;
import br.com.fiap.amanaje.riscos.dto.AvaliacaoRiscoResponse;
import br.com.fiap.amanaje.riscos.dto.AvaliarRiscoResponse;
import br.com.fiap.amanaje.riscos.dto.RiscoAtualResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RiscoService {

	private static final String VALIDA = "S";

	private final AvaliacaoRiscoRepository avaliacaoRepository;
	private final LeituraIotRepository leituraRepository;
	private final ObservacaoClimaticaRepository observacaoRepository;
	private final RegiaoMonitoradaService regiaoService;
	private final AlertaService alertaService;

	public RiscoService(
			AvaliacaoRiscoRepository avaliacaoRepository,
			LeituraIotRepository leituraRepository,
			ObservacaoClimaticaRepository observacaoRepository,
			RegiaoMonitoradaService regiaoService,
			AlertaService alertaService) {
		this.avaliacaoRepository = avaliacaoRepository;
		this.leituraRepository = leituraRepository;
		this.observacaoRepository = observacaoRepository;
		this.regiaoService = regiaoService;
		this.alertaService = alertaService;
	}

	@Transactional
	public AvaliarRiscoResponse avaliar(Long idRegiao) {
		regiaoService.buscarAtiva(idRegiao);
		Optional<LeituraIot> leitura = leituraRepository
				.findFirstByIdRegiaoAndStValidaOrderByDtLeituraDesc(idRegiao, VALIDA);
		Optional<ObservacaoClimatica> observacao = observacaoRepository
				.findFirstByIdRegiaoOrderByDtObservacaoDesc(idRegiao);
		if (leitura.isEmpty() && observacao.isEmpty()) {
			throw new BusinessRuleException("Não há leitura IoT válida nem observação climática para avaliar a região: "
					+ idRegiao);
		}

		LocalDateTime agora = LocalDateTime.now();
		List<AvaliacaoRisco> avaliacoes = List.of(
				criarAvaliacao(idRegiao, TipoRisco.ENCHENTE, leitura.orElse(null), observacao.orElse(null), agora),
				criarAvaliacao(idRegiao, TipoRisco.DESLIZAMENTO, leitura.orElse(null), observacao.orElse(null), agora),
				criarAvaliacao(idRegiao, TipoRisco.TEMPESTADE, leitura.orElse(null), observacao.orElse(null), agora),
				criarAvaliacao(idRegiao, TipoRisco.QUALIDADE_AR, leitura.orElse(null), observacao.orElse(null), agora));

		List<AlertaResponse> alertas = avaliacoes.stream()
				.map(alertaService::gerarSeNecessario)
				.flatMap(Optional::stream)
				.toList();

		return new AvaliarRiscoResponse(
				idRegiao,
				avaliacoes.stream().map(this::toResponse).toList(),
				alertas);
	}

	@Transactional(readOnly = true)
	public RiscoAtualResponse buscarAtual(Long idRegiao) {
		regiaoService.buscarAtiva(idRegiao);
		List<AvaliacaoRiscoResponse> avaliacoes = List.of(TipoRisco.values()).stream()
				.map(tipoRisco -> avaliacaoRepository
						.findFirstByIdRegiaoAndTipoRiscoOrderByDtAvaliacaoDesc(idRegiao, tipoRisco))
				.flatMap(Optional::stream)
				.map(this::toResponse)
				.toList();
		if (avaliacoes.isEmpty()) {
			throw new ResourceNotFoundException("Nenhuma avaliação de risco encontrada para a região: " + idRegiao);
		}

		AvaliacaoRiscoResponse consolidada = avaliacoes.stream()
				.max(Comparator.comparing(AvaliacaoRiscoResponse::scoreRisco))
				.orElseThrow();
		return new RiscoAtualResponse(
				idRegiao,
				consolidada.scoreRisco(),
				consolidada.nivelRisco(),
				avaliacoes);
	}

	BigDecimal calculateEnchenteScore(LeituraIot leitura, ObservacaoClimatica observacao) {
		double score = contribution(leitura == null ? null : leitura.getNivelAguaPercentual(), 100, 60)
				+ inverseContribution(leitura == null ? null : leitura.getDistanciaAguaCm(), 100, 25)
				+ contribution(observacao == null ? null : observacao.getPrecipitacaoMm(), 100, 15);
		return clampScore(score);
	}

	BigDecimal calculateDeslizamentoScore(LeituraIot leitura, ObservacaoClimatica observacao) {
		double score = contribution(leitura == null ? null : leitura.getInclinacaoGraus(), 45, 45)
				+ contribution(leitura == null ? null : leitura.getVibracao(), 10, 30)
				+ contribution(observacao == null ? null : observacao.getPrecipitacaoMm(), 100, 25);
		return clampScore(score);
	}

	BigDecimal calculateTempestadeScore(LeituraIot leitura, ObservacaoClimatica observacao) {
		BigDecimal pressao = leitura != null && leitura.getPressaoHpa() != null
				? leitura.getPressaoHpa()
				: observacao == null ? null : observacao.getPressaoHpa();
		double score = pressureContribution(pressao)
				+ contribution(observacao == null ? null : observacao.getVentoKmh(), 100, 35)
				+ contribution(observacao == null ? null : observacao.getPrecipitacaoMm(), 100, 25);
		return clampScore(score);
	}

	BigDecimal calculateQualidadeArScore(LeituraIot leitura) {
		double score = contribution(leitura == null ? null : leitura.getPm25(), 75, 60)
				+ contribution(leitura == null ? null : leitura.getPm10(), 150, 40);
		return clampScore(score);
	}

	BigDecimal clampScore(double score) {
		return BigDecimal.valueOf(Math.max(0, Math.min(100, score)))
				.setScale(2, RoundingMode.HALF_UP);
	}

	NivelRisco resolveNivelRisco(BigDecimal score) {
		if (score.compareTo(BigDecimal.valueOf(75)) >= 0) {
			return NivelRisco.CRITICO;
		}
		if (score.compareTo(BigDecimal.valueOf(50)) >= 0) {
			return NivelRisco.ALTO;
		}
		if (score.compareTo(BigDecimal.valueOf(25)) >= 0) {
			return NivelRisco.MODERADO;
		}
		return NivelRisco.BAIXO;
	}

	private AvaliacaoRisco criarAvaliacao(
			Long idRegiao,
			TipoRisco tipoRisco,
			LeituraIot leitura,
			ObservacaoClimatica observacao,
			LocalDateTime agora) {
		BigDecimal score = switch (tipoRisco) {
			case ENCHENTE -> calculateEnchenteScore(leitura, observacao);
			case DESLIZAMENTO -> calculateDeslizamentoScore(leitura, observacao);
			case TEMPESTADE -> calculateTempestadeScore(leitura, observacao);
			case QUALIDADE_AR -> calculateQualidadeArScore(leitura);
		};
		AvaliacaoRisco avaliacao = AvaliacaoRisco.builder()
				.idRegiao(idRegiao)
				.idLeitura(leitura == null ? null : leitura.getIdLeitura())
				.idObservacao(observacao == null ? null : observacao.getIdObservacao())
				.tipoRisco(tipoRisco)
				.scoreRisco(score)
				.nivelRisco(resolveNivelRisco(score))
				.motivo(criarMotivo(tipoRisco))
				.dtAvaliacao(agora)
				.build();
		return avaliacaoRepository.save(avaliacao);
	}

	private double contribution(BigDecimal value, double referenceValue, double maxContribution) {
		if (value == null) {
			return 0;
		}
		return Math.min(Math.abs(value.doubleValue()) / referenceValue * maxContribution, maxContribution);
	}

	private double inverseContribution(BigDecimal value, double referenceValue, double maxContribution) {
		if (value == null) {
			return 0;
		}
		double normalized = (referenceValue - value.doubleValue()) / referenceValue * maxContribution;
		return Math.max(0, Math.min(normalized, maxContribution));
	}

	private double pressureContribution(BigDecimal pressaoHpa) {
		if (pressaoHpa == null) {
			return 0;
		}
		double normalized = (1013 - pressaoHpa.doubleValue()) / 213 * 40;
		return Math.max(0, Math.min(normalized, 40));
	}

	private String criarMotivo(TipoRisco tipoRisco) {
		return switch (tipoRisco) {
			case ENCHENTE -> "Cálculo baseado em nível da água, distância da água e precipitação disponíveis.";
			case DESLIZAMENTO -> "Cálculo baseado em inclinação, vibração e precipitação disponíveis.";
			case TEMPESTADE -> "Cálculo baseado em pressão atmosférica, vento e precipitação disponíveis.";
			case QUALIDADE_AR -> "Cálculo baseado em partículas PM2.5 e PM10 disponíveis.";
		};
	}

	private AvaliacaoRiscoResponse toResponse(AvaliacaoRisco avaliacao) {
		return new AvaliacaoRiscoResponse(
				avaliacao.getIdAvaliacao(),
				avaliacao.getIdRegiao(),
				avaliacao.getIdLeitura(),
				avaliacao.getIdObservacao(),
				avaliacao.getTipoRisco(),
				avaliacao.getScoreRisco(),
				avaliacao.getNivelRisco(),
				avaliacao.getMotivo(),
				avaliacao.getDtAvaliacao());
	}

}
