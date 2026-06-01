package br.com.fiap.amanaje.clientes.dto;

import br.com.fiap.amanaje.clientes.TipoCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteCreateRequest(
		@NotBlank String nome,
		@NotNull TipoCliente tipoCliente,
		String documento,
		@Email String emailContato,
		String telefone) {
}
