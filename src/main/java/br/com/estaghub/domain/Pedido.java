package br.com.estaghub.domain;

import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.repository.impl.PedidoRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String justificativa;
    @Enumerated(EnumType.STRING)
    private TipoPedido tipo;
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    @Column(name = "data_hora_criacao")
    private LocalDateTime dataHoraCriacao;
    @Column(name = "data_hora_ult_atualizacao")
    private LocalDateTime dataHoraUltimaAtualizacao;

    public void criarPedido(Pedido pedido){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.criarPedido(pedido);
    }
    public Pedido getPedidoById(Long id){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getPedidoById(id);
    }
    public Boolean getPedidoByIdWhereSupervisorNotSet(String numPedido){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getPedidoByIdWhereSupervisorNotSet(Long.parseLong(numPedido));
    }
    public void addSupervisorNoPedido(Supervisor supervisor, String numPedido){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        pedidoRepository.addSupervisorNoPedido(supervisor, numPedido);
    }
    public Optional<Pedido> getPedidoByDiscenteId(Discente discente, TipoPedido tipoPedido){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getPedidoByDiscenteId(discente, tipoPedido);
    }
    public Boolean checkIfDiscenteAlreadyHavePedido(Discente discente, TipoPedido tipoPedido){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.checkIfDiscenteAlreadyHavePedido(discente, tipoPedido);
    }
    public List<Pedido> getAllPedidos(){
        PedidoRepositoryImpl pedidoRepository = new PedidoRepositoryImpl();
        return pedidoRepository.getAllPedidos();
    }
}
