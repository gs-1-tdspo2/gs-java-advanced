package br.com.fiap.amanaje.estacoes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstacaoIotRepository extends JpaRepository<EstacaoIot, Long> {

	List<EstacaoIot> findByIdRegiaoAndStAtivo(Long idRegiao, String stAtivo);

	List<EstacaoIot> findByStAtivo(String stAtivo);

	Optional<EstacaoIot> findByCodigoEstacao(String codigoEstacao);

}
