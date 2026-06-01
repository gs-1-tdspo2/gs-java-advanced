package br.com.fiap.amanaje.alertas;

import java.time.LocalDateTime;

import br.com.fiap.amanaje.riscos.NivelRisco;
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
@Table(name = "TB_AMANAJE_ALERTA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ALERTA")
	private Long idAlerta;

	@Column(name = "ID_REGIAO")
	private Long idRegiao;

	@Column(name = "ID_AVALIACAO")
	private Long idAvaliacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "TP_ALERTA")
	private TipoAlerta tipoAlerta;

	@Enumerated(EnumType.STRING)
	@Column(name = "TP_NIVEL")
	private NivelRisco nivelRisco;

	@Column(name = "DS_TITULO")
	private String titulo;

	@Column(name = "DS_ALERTA")
	private String descricao;

	@Column(name = "DS_RECOM")
	private String recomendacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "ST_ALERTA")
	private StatusAlerta statusAlerta;

	@Column(name = "DT_ALERTA")
	private LocalDateTime dtAlerta;

	@Column(name = "DT_RESOLVIDO_EM")
	private LocalDateTime dtResolvidoEm;

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
