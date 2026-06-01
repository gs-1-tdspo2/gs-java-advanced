package br.com.fiap.amanaje.regioes;

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
@Table(name = "TB_AMANAJE_REGIAO_MONIT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegiaoMonitorada {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_REGIAO")
	private Long idRegiao;

	@Column(name = "ID_CLIENTE")
	private Long idCliente;

	@Column(name = "NM_REGIAO")
	private String nome;

	@Column(name = "NM_CIDADE")
	private String cidade;

	@Column(name = "SG_ESTADO")
	private String estado;

	@Column(name = "NR_LATITUDE")
	private BigDecimal latitude;

	@Column(name = "NR_LONGITUDE")
	private BigDecimal longitude;

	@Enumerated(EnumType.STRING)
	@Column(name = "TP_AREA")
	private TipoArea tipoArea;

	@Column(name = "NR_NIVEL_VULN")
	private Integer nivelVulnerabilidade;

	@Enumerated(EnumType.STRING)
	@Column(name = "TP_VISIB")
	private TipoVisibilidade tipoVisibilidade;

	@Column(name = "ST_ATIVO")
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
