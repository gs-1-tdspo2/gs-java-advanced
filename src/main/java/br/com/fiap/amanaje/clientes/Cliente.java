package br.com.fiap.amanaje.clientes;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_AMANAJE_CLI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CLIENTE")
	private Long idCliente;

	@Column(name = "NM_CLI")
	private String nome;

	@Enumerated(EnumType.STRING)
	@Column(name = "TP_CLI")
	private TipoCliente tipoCliente;

	@Column(name = "NR_DOCUMENTO")
	private String documento;

	@Column(name = "DS_EMAIL_CONTATO")
	private String emailContato;

	@Column(name = "NR_TELEFONE")
	private String telefone;

	@Column(name = "ST_ATIVO", length = 1, columnDefinition = "CHAR(1)")
	private String stAtivo;

	@Column(name = "DT_CRIADO_EM")
	private LocalDateTime dtCriadoEm;

	@Column(name = "DT_ATUALIZADO_EM")
	private LocalDateTime dtAtualizadoEm;

	@Column(name = "DT_DEL_EM")
	private LocalDateTime dtDelEm;

	@Column(name = "ID_DEL_POR")
	private Long idDelPor;

	@Column(name = "DS_MOTIVO_EXCLUSAO")
	private String motivoExclusao;

}
