package br.com.estaghub.repository;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoPedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
    void criarPedido(Pedido pedido);
    Pedido getPedidoById(Long id);
    Optional<Pedido> getPedidoByDiscente(Discente discente, TipoPedido tipoPedido);
    void addSupervisorNoPedido(Supervisor supervisor, String numPedido);
    List<Pedido> getAllPedidosOfDocenteComissao(Docente docente);
    List<Pedido> getAllRenovPedidosInStep1WithoutSupervisor();
    void changeStatusPedido(String idPedido, StatusPedido statusPedido);
    void changeJustificativaDocentePedido(String idPedido, String justificativa);
    void changeJustificativaDiscentePedido(String idPedido, String justificativa);
    void changeJustificativaRecursoPedido(String idPedido, String justificativa);
    void addDocenteComissaoInPedido(String idPedido, Docente docente);
    void addDocenteOrientadorInPedido(String idPedido, Docente docente);
    void addAvaliacaoDesempenhoDiscenteInPedido(String idPedido, String avaliacao);
    List<Pedido> getAllPedidosOfDocente(Docente docente);
    void changeJustificativaDocumentacaoPedido(String idPedido, String justificativa);
    List<Pedido> getAllPedidosOfSupervisor(Supervisor supervisor);
}
