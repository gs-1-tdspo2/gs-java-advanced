package br.com.fiap.amanaje.leituras.mqtt;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.amanaje.riscos.enums.NivelRisco;
import org.junit.jupiter.api.Test;

class LedStatusMapperTest {

	@Test
	void shouldMapLedByRiskLevel() {
		assertThat(LedStatusMapper.toLed(NivelRisco.BAIXO)).isEqualTo("GREEN");
		assertThat(LedStatusMapper.toLed(NivelRisco.MODERADO)).isEqualTo("YELLOW");
		assertThat(LedStatusMapper.toLed(NivelRisco.ALTO)).isEqualTo("ORANGE");
		assertThat(LedStatusMapper.toLed(NivelRisco.CRITICO)).isEqualTo("RED");
	}

	@Test
	void shouldFlagAlertOnlyForHighAndCriticalRisk() {
		assertThat(LedStatusMapper.isAlerta(NivelRisco.BAIXO)).isFalse();
		assertThat(LedStatusMapper.isAlerta(NivelRisco.MODERADO)).isFalse();
		assertThat(LedStatusMapper.isAlerta(NivelRisco.ALTO)).isTrue();
		assertThat(LedStatusMapper.isAlerta(NivelRisco.CRITICO)).isTrue();
	}

}
