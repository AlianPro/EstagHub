package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.repository.PedidoRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PedidoRepositoryImpl implements PedidoRepository {
    EntityManagerFactory emf;
    EntityManager em;
    public PedidoRepositoryImpl() {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
    }

    @Override
    public void criarPedido(Pedido pedido) {
        pedido.setDataHoraCriacao(LocalDateTime.now());
        em.getTransaction().begin();
        em.persist(pedido);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    @Override
    public Pedido getPedidoById(Long id) {
        Pedido pedido = em.find(Pedido.class,id);
        em.close();
        emf.close();
        return pedido;
    }

    @Override
    public Boolean getPedidoByIdWhereSupervisorNotSet(Long numPedido) {
        Query queryCheckIfHasSupervisorPresent = em.createNativeQuery("Select * from Pedido where id= :id and id_supervisor is null");
        queryCheckIfHasSupervisorPresent.setParameter("id", numPedido);
        if (!queryCheckIfHasSupervisorPresent.getResultList().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public Optional<Pedido> getPedidoByDiscenteId(Discente discente, TipoPedido tipoPedido) {
        TypedQuery<Pedido> queryGetPedidoByDiscenteName = em.createQuery("Select p from Pedido p where p.discente = :discente and p.tipo = :tipo", Pedido.class);
        queryGetPedidoByDiscenteName.setParameter("discente", discente);
        queryGetPedidoByDiscenteName.setParameter("tipo", tipoPedido);
        return Optional.ofNullable(queryGetPedidoByDiscenteName.getSingleResult());
    }

    @Override
    public void addSupervisorNoPedido(Supervisor supervisor, String numPedido) {
        if (getPedidoByIdWhereSupervisorNotSet(Long.parseLong(numPedido))){
            Query query = em.createQuery("UPDATE Pedido set id_supervisor = :idSupervisor , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
            query.setParameter("idSupervisor", supervisor.getId());
            query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
            query.setParameter("idPedido", Long.parseLong(numPedido));
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
        }
        em.close();
        emf.close();
    }
    @Override
    public Boolean checkIfDiscenteAlreadyHavePedido(Discente discente, TipoPedido tipoPedido) {
        Query query = em.createNativeQuery("SELECT * FROM Pedido WHERE id_discente = :id_discente and tipo = :tipo");
        query.setParameter("id_discente", discente.getId());
        query.setParameter("tipo", tipoPedido.name());
        return query.getResultList().isEmpty();
    }
    @Override
    public List<Pedido> getAllPedidos() {
        return em.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList();
    }

}
