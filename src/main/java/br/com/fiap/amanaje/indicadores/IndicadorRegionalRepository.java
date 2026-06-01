package br.com.fiap.amanaje.indicadores;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicadorRegionalRepository extends JpaRepository<IndicadorRegional, Long> {

	List<IndicadorRegional> findByEstadoAndCidade(String estado, String cidade);

}
