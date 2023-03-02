package br.com.estaghub.domain;

import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.repository.impl.DocumentoRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

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
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "nomeEmpresa", column = @Column(name = "nome_empresa_plano_atividades")),
            @AttributeOverride(name = "responsavelEmpresa", column = @Column(name = "responsavel_empresa_plano_atividades")),
            @AttributeOverride(name = "enderecoEmpresa", column = @Column(name = "endereco_empresa_plano_atividades")),
            @AttributeOverride(name = "telefoneEmpresa", column = @Column(name = "telefone_empresa_plano_atividades")),
            @AttributeOverride(name = "emailEmpresa", column = @Column(name = "email_empresa_plano_atividades")),
            @AttributeOverride(name = "nomeSupervisor", column = @Column(name = "nome_supervisor_plano_atividades")),
            @AttributeOverride(name = "formacaoSupervisor", column = @Column(name = "formacao_supervisor_plano_atividades")),
            @AttributeOverride(name = "primeiraAtividade", column = @Column(name = "primeira_atividade_plano_atividades")),
            @AttributeOverride(name = "segundaAtividade", column = @Column(name = "segunda_atividade_plano_atividades")),
            @AttributeOverride(name = "terceiraAtividade", column = @Column(name = "terceira_atividade_plano_atividades")),
            @AttributeOverride(name = "quartaAtividade", column = @Column(name = "quarta_atividade_plano_atividades")),
            @AttributeOverride(name = "quintaAtividade", column = @Column(name = "quinta_atividade_plano_atividades"))
    })
    private PlanoAtividades planoAtividades;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "nomeEmpresa", column = @Column(name = "nome_empresa_tce")),
            @AttributeOverride(name = "cnpjEmpresa", column = @Column(name = "cnpj_empresa_tce")),
            @AttributeOverride(name = "horarioInicio", column = @Column(name = "horario_inicio_tce")),
            @AttributeOverride(name = "horarioFim", column = @Column(name = "horario_fim_tce")),
            @AttributeOverride(name = "totalHoras", column = @Column(name = "total_horas_tce")),
            @AttributeOverride(name = "intervalo", column = @Column(name = "intervalo_tce")),
            @AttributeOverride(name = "dataInicio", column = @Column(name = "data_inicio_tce")),
            @AttributeOverride(name = "dataFim", column = @Column(name = "data_fim_tce")),
            @AttributeOverride(name = "bolsa", column = @Column(name = "bolsa_tce")),
            @AttributeOverride(name = "auxTransporte", column = @Column(name = "aux_transporte_tce")),
            @AttributeOverride(name = "codApolice", column = @Column(name = "cod_apolice_tce")),
            @AttributeOverride(name = "nomeSeguradora", column = @Column(name = "nome_seguradora_tce"))
    })
    private TCE tce;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dataAntiga", column = @Column(name = "data_antiga_termo_aditivo")),
            @AttributeOverride(name = "dataNova", column = @Column(name = "data_nova_termo_aditivo")),
            @AttributeOverride(name = "nomeSeguradora", column = @Column(name = "nome_seguradora_termo_aditivo")),
            @AttributeOverride(name = "codApolice", column = @Column(name = "codigo_apolice_termo_aditivo"))
    })
    private TermoAditivo termoAditivo;
    public void criarDocumento(Documento documento){
        DocumentoRepositoryImpl documentoRepository = new DocumentoRepositoryImpl();
        documentoRepository.criarDocumento(documento);
    }
    public Optional<Documento> getDocumentoByIdPedidoAndTipoDocumento(Long idPedido, TipoDocumento tipoDocumento){
        DocumentoRepositoryImpl documentoRepository = new DocumentoRepositoryImpl();
        return documentoRepository.getDocumentoByIdPedidoAndTipoDocumento(idPedido, tipoDocumento);
    }
    public void addPlanoAtividadesInDocumento(String idDocumento, PlanoAtividades planoAtividades){
        DocumentoRepositoryImpl documentoRepository = new DocumentoRepositoryImpl();
        documentoRepository.addPlanoAtividadesInDocumento(idDocumento, planoAtividades);
    }
    public void addTCEInDocumento(String idDocumento, TCE tce){
        DocumentoRepositoryImpl documentoRepository = new DocumentoRepositoryImpl();
        documentoRepository.addTCEInDocumento(idDocumento, tce);
    }
    public void addTermoAditivoInDocumento(String idDocumento, TermoAditivo termoAditivo){
        DocumentoRepositoryImpl documentoRepository = new DocumentoRepositoryImpl();
        documentoRepository.addTermoAditivoInDocumento(idDocumento, termoAditivo);
    }
}
