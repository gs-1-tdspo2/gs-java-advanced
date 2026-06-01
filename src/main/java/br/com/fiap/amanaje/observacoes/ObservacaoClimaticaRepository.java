package br.com.fiap.amanaje.observacoes;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservacaoClimaticaRepository extends JpaRepository<ObservacaoClimatica, Long> {

	Optional<ObservacaoClimatica> findFirstByIdRegiaoOrderByDtObservacaoDesc(Long idRegiao);

}
