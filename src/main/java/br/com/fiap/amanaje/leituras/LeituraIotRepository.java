package br.com.fiap.amanaje.leituras;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeituraIotRepository extends JpaRepository<LeituraIot, Long> {

	List<LeituraIot> findByIdRegiaoOrderByDtLeituraDesc(Long idRegiao);

	Optional<LeituraIot> findFirstByIdRegiaoAndStValidaOrderByDtLeituraDesc(Long idRegiao, String stValida);

}
