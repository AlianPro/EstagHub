package br.com.estaghub.domain;

import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.repository.impl.DiscenteRepositoryImpl;
import br.com.estaghub.repository.impl.DocumentoRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @ManyToOne
    @JoinColumn(name="id_pedido")
    private Pedido pedido;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    @Column(name = "data_hora_ult_atualizacao")
    private LocalDateTime dataHoraUltimaAtualizacao;
    public void criarDocumento(Documento documento){
        DocumentoRepositoryImpl documentoRepository = new DocumentoRepositoryImpl();
        documentoRepository.criarDocumento(documento);
    }
}
