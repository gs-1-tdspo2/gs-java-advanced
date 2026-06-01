package br.com.fiap.amanaje.alertas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

	List<Alerta> findByStatusAlertaAndStAtivo(StatusAlerta statusAlerta, String stAtivo);

	List<Alerta> findByIdRegiaoAndStAtivo(Long idRegiao, String stAtivo);

}
