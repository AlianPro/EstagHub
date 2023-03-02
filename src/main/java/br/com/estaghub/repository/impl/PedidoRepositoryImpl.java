package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.repository.PedidoRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
//        em.close();
//        emf.close();
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
    public Optional<Pedido> getPedidoByDiscente(Discente discente, TipoPedido tipoPedido) {
        TypedQuery<Pedido> queryGetPedidoByDiscenteName = em.createQuery("Select p from Pedido p where p.discente = :discente and p.tipo = :tipo and p.status <> 'PEDIDO_ENCERRADO'", Pedido.class);
        queryGetPedidoByDiscenteName.setParameter("discente", discente);
        queryGetPedidoByDiscenteName.setParameter("tipo", tipoPedido);
        return Optional.ofNullable(queryGetPedidoByDiscenteName.getResultList().isEmpty()? null : queryGetPedidoByDiscenteName.getSingleResult());
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
            em.close();
            emf.close();
        }
    }
    @Override
    public Boolean checkIfDiscenteAlreadyHavePedido(Discente discente, TipoPedido tipoPedido) {
        TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.discente = :discente and p.tipo = :tipo", Pedido.class);
        query.setParameter("discente", discente);
        query.setParameter("tipo", tipoPedido);
        return query.getResultList().isEmpty();
    }
    @Override
    public List<Pedido> getAllPedidos() {
        return em.createQuery("SELECT p FROM Pedido p where p.status <> 'NOVO_PEDIDO_FIM' and p.status <> 'PEDIDO_ENCERRADO'", Pedido.class).getResultList();
    }
    @Override
    public List<Pedido> getAllPedidosOfDocente(Docente docente) {
        TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.docenteOrientador = :id_docente_orientador and p.status <> 'NOVO_PEDIDO_FIM' and p.status <> 'PEDIDO_ENCERRADO'", Pedido.class);
        query.setParameter("id_docente_orientador", docente);
        return query.getResultList();
    }
    @Override
    public List<Pedido> getAllPedidosOfSupervisor(Supervisor supervisor) {
        TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.supervisor = :supervisor and p.status <> 'NOVO_PEDIDO_FIM' and p.status <> 'PEDIDO_ENCERRADO'", Pedido.class);
        query.setParameter("supervisor", supervisor);
        return query.getResultList();
    }
    @Override
    public void changeStatusPedido(String idPedido, StatusPedido statusPedido) {
        if (Objects.nonNull(getPedidoById(Long.parseLong(idPedido)))){
            Query query = em.createQuery("UPDATE Pedido set status = :status , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
            query.setParameter("status", statusPedido);
            query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
            query.setParameter("idPedido", Long.parseLong(idPedido));
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }
    @Override
    public void changeJustificativaDocentePedido(String idPedido, String justificativa) {
        if (Objects.nonNull(getPedidoById(Long.parseLong(idPedido)))){
            Query query = em.createQuery("UPDATE Pedido set justificativa_docente = :justificativa_docente , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
            query.setParameter("justificativa_docente", justificativa);
            query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
            query.setParameter("idPedido", Long.parseLong(idPedido));
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }
    @Override
    public void changeJustificativaDiscentePedido(String idPedido, String justificativa) {
        if (Objects.nonNull(getPedidoById(Long.parseLong(idPedido)))){
            Query query = em.createQuery("UPDATE Pedido set justificativa_discente = :justificativa_discente , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
            query.setParameter("justificativa_discente", justificativa);
            query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
            query.setParameter("idPedido", Long.parseLong(idPedido));
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }
    @Override
    public void changeJustificativaRecursoPedido(String idPedido, String justificativa) {
        if (Objects.nonNull(getPedidoById(Long.parseLong(idPedido)))){
            Query query = em.createQuery("UPDATE Pedido set justificativa_recurso = :justificativa_recurso , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
            query.setParameter("justificativa_recurso", justificativa);
            query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
            query.setParameter("idPedido", Long.parseLong(idPedido));
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }
    @Override
    public void changeJustificativaDocumentacaoPedido(String idPedido, String justificativa) {
        if (Objects.nonNull(getPedidoById(Long.parseLong(idPedido)))){
            Query query = em.createQuery("UPDATE Pedido set justificativa_documentacao = :justificativa_documentacao , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
            query.setParameter("justificativa_documentacao", justificativa);
            query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
            query.setParameter("idPedido", Long.parseLong(idPedido));
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }
    @Override
    public void addDocenteComissaoInPedido(String idPedido, Docente docente) {
        if (Objects.nonNull(getPedidoById(Long.parseLong(idPedido)))){
            Query query = em.createQuery("UPDATE Pedido set id_docente_comissao_responsavel = :docente WHERE id = :idPedido");
            query.setParameter("docente", docente);
            query.setParameter("idPedido", Long.parseLong(idPedido));
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }
    @Override
    public void addDocenteOrientadorInPedido(String idPedido, Docente docente) {
        if (Objects.nonNull(getPedidoById(Long.parseLong(idPedido)))){
            Query query = em.createQuery("UPDATE Pedido set id_docente_orientador = :id_docente_orientador WHERE id = :idPedido");
            query.setParameter("id_docente_orientador", docente);
            query.setParameter("idPedido", Long.parseLong(idPedido));
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }
    @Override
    public void addAvaliacaoDesempenhoDiscenteInPedido(String idPedido, String avaliacao) {
        if (Objects.nonNull(getPedidoById(Long.parseLong(idPedido)))){
            Query query = em.createQuery("UPDATE Pedido set avaliacao_supervisor_renovacao = :avaliacao WHERE id = :idPedido");
            query.setParameter("avaliacao", avaliacao);
            query.setParameter("idPedido", Long.parseLong(idPedido));
            em.getTransaction().begin();
            query.executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        }
    }

}
