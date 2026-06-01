package br.com.fiap.amanaje.clientes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	List<Cliente> findByStAtivo(String stAtivo);

}
