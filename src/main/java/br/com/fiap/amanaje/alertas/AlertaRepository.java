package br.com.fiap.amanaje.alertas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

	List<Alerta> findByStatusAlertaAndStAtivo(StatusAlerta statusAlerta, String stAtivo);

	List<Alerta> findByIdRegiaoAndStAtivo(Long idRegiao, String stAtivo);

	List<Alerta> findByStAtivo(String stAtivo);

	Optional<Alerta> findFirstByIdRegiaoAndTipoAlertaAndStatusAlertaInAndStAtivo(
			Long idRegiao,
			TipoAlerta tipoAlerta,
			List<StatusAlerta> statusAlerta,
			String stAtivo);

}
