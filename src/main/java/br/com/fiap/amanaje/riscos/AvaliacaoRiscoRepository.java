package br.com.fiap.amanaje.riscos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRiscoRepository extends JpaRepository<AvaliacaoRisco, Long> {

	List<AvaliacaoRisco> findByIdRegiaoOrderByDtAvaliacaoDesc(Long idRegiao);

	Optional<AvaliacaoRisco> findFirstByIdRegiaoOrderByDtAvaliacaoDesc(Long idRegiao);

}
