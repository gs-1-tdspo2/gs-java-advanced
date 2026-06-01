package br.com.fiap.amanaje.indicadores;

import java.util.List;

import br.com.fiap.amanaje.indicadores.dto.IndicadorRegionalResponse;
import br.com.fiap.amanaje.riscos.NivelRisco;
import br.com.fiap.amanaje.riscos.TipoRisco;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IndicadorRegionalService {

	private final IndicadorRegionalRepository indicadorRepository;

	public IndicadorRegionalService(IndicadorRegionalRepository indicadorRepository) {
		this.indicadorRepository = indicadorRepository;
	}

	@Transactional(readOnly = true)
	public List<IndicadorRegionalResponse> listar(
			String estado,
			String cidade,
			TipoRisco tipoRisco,
			NivelRisco nivelRiscoMedio) {
		return indicadorRepository.findAll().stream()
				.filter(indicador -> matchesIgnoreCase(indicador.getEstado(), estado))
				.filter(indicador -> matchesIgnoreCase(indicador.getCidade(), cidade))
				.filter(indicador -> tipoRisco == null || tipoRisco == indicador.getTipoRisco())
				.filter(indicador -> nivelRiscoMedio == null || nivelRiscoMedio == indicador.getNivelRiscoMedio())
				.map(this::toResponse)
				.toList();
	}

	private boolean matchesIgnoreCase(String valor, String filtro) {
		return filtro == null || valor != null && valor.equalsIgnoreCase(filtro);
	}

	private IndicadorRegionalResponse toResponse(IndicadorRegional indicador) {
		return new IndicadorRegionalResponse(
				indicador.getIdIndicador(),
				indicador.getIdRegiao(),
				indicador.getEstado(),
				indicador.getCidade(),
				indicador.getNomeRegiao(),
				indicador.getTipoRisco(),
				indicador.getScoreMedio(),
				indicador.getNivelRiscoMedio(),
				indicador.getQuantidadeEstacoes(),
				indicador.getQuantidadeAlertasAtivos(),
				indicador.getFonteCalculo(),
				indicador.getDtCalculo());
	}

}
