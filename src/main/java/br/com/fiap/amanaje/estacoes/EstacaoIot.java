package br.com.fiap.amanaje.estacoes;

import java.math.BigDecimal;
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
@Table(name = "TB_AMANAJE_EST_IOT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstacaoIot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ESTACAO")
	private Long idEstacao;

	@Column(name = "ID_REGIAO")
	private Long idRegiao;

	@Column(name = "CD_EST")
	private String codigoEstacao;

	@Column(name = "NM_EST")
	private String nome;

	@Enumerated(EnumType.STRING)
	@Column(name = "TP_EST")
	private TipoEstacao tipoEstacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "ST_EST")
	private StatusEstacao statusEstacao;

	@Column(name = "NR_LATITUDE")
	private BigDecimal latitude;

	@Column(name = "NR_LONGITUDE")
	private BigDecimal longitude;

	@Column(name = "DT_ULTIMA_COM")
	private LocalDateTime dtUltimaComunicacao;

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
