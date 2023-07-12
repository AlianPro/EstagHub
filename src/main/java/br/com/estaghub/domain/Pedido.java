package br.com.estaghub.domain;

import br.com.estaghub.domain.embeddable.RenovacaoEstagio;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.repository.impl.PedidoRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_supervisor")
    private Supervisor supervisor;
    @ManyToOne
    @JoinColumn(name = "id_docente_comissao_responsavel")
    private Docente docenteComissaoResponsavel;
    @ManyToOne
    @JoinColumn(name = "id_docente_orientador")
    private Docente docenteOrientador;
    @ManyToOne
    @JoinColumn(name = "id_discente")
    private Discente discente;
    @OneToMany(mappedBy = "pedido")
    private List<Documento> documentos;
    @Column(name = "justificativa_discente")
    @NotBlank
    private String justificativaDiscente;
    @Column(name = "justificativa_docente")
    @NotBlank
    private String justificativaDocente;
    @Column(name = "justificativa_recurso")
    @NotBlank
    private String justificativaRecurso;
    @Column(name = "justificativa_documentacao")
    @NotBlank
    private String justificativaDocumentacao;
    @Enumerated(EnumType.STRING)
    private TipoPedido tipo;
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    @Column(name = "data_hora_ult_atualizacao")
    private LocalDateTime dataHoraUltimaAtualizacao;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "nomeEmpresa", column = @Column(name = "nome_empresa_renovacao")),
            @AttributeOverride(name = "enderecoEmpresa", column = @Column(name = "endereco_empresa_renovacao")),
            @AttributeOverride(name = "resumoAtividades", column = @Column(name = "resumo_atividades_renovacao")),
            @AttributeOverride(name = "tempoEstagio", column = @Column(name = "tempo_estagio_renovacao")),
            @AttributeOverride(name = "totalHoras", column = @Column(name = "contribuicao_estagio_renovacao")),
            @AttributeOverride(name = "avaliacaoSupervisor", column = @Column(name = "avaliacao_supervisor_renovacao"))
    })
    private RenovacaoEstagio renovacaoEstagio;
    public void criarPedido(Pedido pedido){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.criarPedido(pedido);
    }
    public Pedido getPedidoById(Long id){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getPedidoById(id);
    }
    public void addSupervisorNoPedido(Supervisor supervisor, String numPedido){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.addSupervisorNoPedido(supervisor, numPedido);
    }
    public Optional<Pedido> getPedidoByDiscente(Discente discente, TipoPedido tipoPedido){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getPedidoByDiscente(discente, tipoPedido);
    }

    public List<Pedido> getAllPedidosOfDocenteComissao(Docente docente){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getAllPedidosOfDocenteComissao(docente);
    }
    public List<Pedido> getAllRenovPedidosInStep1WithoutSupervisor(){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getAllRenovPedidosInStep1WithoutSupervisor();
    }
    public void changeStatusPedido(String idPedido, StatusPedido statusPedido){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.changeStatusPedido(idPedido,statusPedido);
    }
    public void changeJustificativaDocentePedido(String idPedido, String justificativa){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.changeJustificativaDocentePedido(idPedido,justificativa);
    }
    public void changeJustificativaDiscentePedido(String idPedido, String justificativa){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.changeJustificativaDiscentePedido(idPedido,justificativa);
    }
    public void changeJustificativaRecursoPedido(String idPedido, String justificativa){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.changeJustificativaRecursoPedido(idPedido,justificativa);
    }
    public void addDocenteComissaoInPedido(String idPedido, Docente docente){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.addDocenteComissaoInPedido(idPedido,docente);
    }
    public void addDocenteOrientadorInPedido(String idPedido, Docente docente){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.addDocenteOrientadorInPedido(idPedido,docente);
    }
    public List<Pedido> getAllPedidosOfDocente(Docente docente){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getAllPedidosOfDocente(docente);
    }
    public List<Pedido> getAllPedidosOfSupervisor(Supervisor supervisor){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getAllPedidosOfSupervisor(supervisor);
    }
    public void changeJustificativaDocumentacaoPedido(String idPedido, String justificativa){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.changeJustificativaDocumentacaoPedido(idPedido,justificativa);
    }
    public void addAvaliacaoDesempenhoDiscenteInPedido(String idPedido, String justificativa){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.addAvaliacaoDesempenhoDiscenteInPedido(idPedido,justificativa);
    }
}
