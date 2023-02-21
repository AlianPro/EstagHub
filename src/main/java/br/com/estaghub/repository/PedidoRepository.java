package br.com.estaghub.repository;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.enums.TipoPedido;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
    void criarPedido(Pedido pedido);
    Pedido getPedidoById(Long id);
    Optional<Pedido> getPedidoByDiscenteId(Discente discente, TipoPedido tipoPedido);
    Boolean getPedidoByIdWhereSupervisorNotSet(Long id);
    void addSupervisorNoPedido(Supervisor supervisor, String numPedido);
    Boolean checkIfDiscenteAlreadyHavePedido(Discente discente, TipoPedido tipoPedido);
    List<Pedido> getAllPedidos();
}
