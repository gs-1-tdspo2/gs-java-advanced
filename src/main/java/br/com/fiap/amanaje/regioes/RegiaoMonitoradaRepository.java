package br.com.fiap.amanaje.regioes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegiaoMonitoradaRepository extends JpaRepository<RegiaoMonitorada, Long> {

	List<RegiaoMonitorada> findByIdClienteAndStAtivo(Long idCliente, String stAtivo);

	List<RegiaoMonitorada> findByStAtivo(String stAtivo);

}
