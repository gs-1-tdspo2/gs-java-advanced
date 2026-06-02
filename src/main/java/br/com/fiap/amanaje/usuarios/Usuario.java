package br.com.fiap.amanaje.usuarios;

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
@Table(name = "TB_AMANAJE_USU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_USUARIO")
	private Long idUsuario;

	@Column(name = "ID_CLIENTE")
	private Long idCliente;

	@Column(name = "NM_USU")
	private String nome;

	@Column(name = "DS_EMAIL")
	private String email;

	@Column(name = "DS_SENHA_HASH")
	private String senhaHash;

	@Enumerated(EnumType.STRING)
	@Column(name = "TP_PERFIL")
	private PerfilUsuario perfil;

	@Enumerated(EnumType.STRING)
	@Column(name = "ST_USU")
	private StatusUsuario statusUsuario;

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
