package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.enums.StatusPedido;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.repository.PedidoRepository;
import br.com.estaghub.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PedidoRepositoryImpl implements PedidoRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();

    @Override
    public void criarPedido(Pedido pedido) {
        pedido.setDataHoraCriacao(LocalDateTime.now());
        em.getTransaction().begin();
        em.persist(pedido);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Pedido getPedidoById(Long id) {
        try{
            return em.find(Pedido.class,id);
        }finally {
            em.close();
        }
    }

    @Override
    public Optional<Pedido> getPedidoByDiscente(Discente discente, TipoPedido tipoPedido) {
        try{
            TypedQuery<Pedido> queryGetPedidoByDiscenteName = em.createQuery("Select p from Pedido p where p.discente = :discente and p.tipo = :tipo and p.status <> 'PEDIDO_ENCERRADO'", Pedido.class);
            queryGetPedidoByDiscenteName.setParameter("discente", discente);
            queryGetPedidoByDiscenteName.setParameter("tipo", tipoPedido);
            return Optional.ofNullable(queryGetPedidoByDiscenteName.getResultList().isEmpty()? null : queryGetPedidoByDiscenteName.getSingleResult());
        }finally {
            em.close();
        }
    }

    @Override
    public void addSupervisorNoPedido(Supervisor supervisor, String numPedido) {
        Query query = em.createQuery("UPDATE Pedido set id_supervisor = :idSupervisor , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido and tipo = 'RENOVACAO'");
        query.setParameter("idSupervisor", supervisor.getId());
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idPedido", Long.parseLong(numPedido));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public List<Pedido> getAllPedidosOfDocenteComissao(Docente docente) {
        try{
            TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p where (p.docenteComissaoResponsavel = :idDocente or p.docenteComissaoResponsavel is null) and p.status <> 'PEDIDO_ENCERRADO'", Pedido.class);
            query.setParameter("idDocente", docente);
            return query.getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public List<Pedido> getAllRenovPedidosInStep1WithoutSupervisor() {
        try{
            TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p where p.supervisor is null and p.tipo = 'RENOVACAO' and p.status = 'RENOVACAO_STEP1'", Pedido.class);
            return query.getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public List<Pedido> getAllPedidosOfDocente(Docente docente) {
        try{
            TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.docenteOrientador = :id_docente_orientador and p.status <> 'PEDIDO_ENCERRADO'", Pedido.class);
            query.setParameter("id_docente_orientador", docente);
            return query.getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public List<Pedido> getAllPedidosOfSupervisor(Supervisor supervisor) {
        try{
            TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.supervisor = :supervisor and p.status <> 'PEDIDO_ENCERRADO'", Pedido.class);
            query.setParameter("supervisor", supervisor);
            return query.getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public void changeStatusPedido(String idPedido, StatusPedido statusPedido) {
        Query query = em.createQuery("UPDATE Pedido set status = :status , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
        query.setParameter("status", statusPedido);
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idPedido", Long.parseLong(idPedido));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void changeJustificativaDocentePedido(String idPedido, String justificativa) {
        Query query = em.createQuery("UPDATE Pedido set justificativa_docente = :justificativa_docente , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
        query.setParameter("justificativa_docente", justificativa);
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idPedido", Long.parseLong(idPedido));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void changeJustificativaDiscentePedido(String idPedido, String justificativa) {
        Query query = em.createQuery("UPDATE Pedido set justificativa_discente = :justificativa_discente , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
        query.setParameter("justificativa_discente", justificativa);
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idPedido", Long.parseLong(idPedido));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void changeJustificativaRecursoPedido(String idPedido, String justificativa) {
        Query query = em.createQuery("UPDATE Pedido set justificativa_recurso = :justificativa_recurso , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
        query.setParameter("justificativa_recurso", justificativa);
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idPedido", Long.parseLong(idPedido));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void changeJustificativaDocumentacaoPedido(String idPedido, String justificativa) {
        Query query = em.createQuery("UPDATE Pedido set justificativa_documentacao = :justificativa_documentacao , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
        query.setParameter("justificativa_documentacao", justificativa);
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idPedido", Long.parseLong(idPedido));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void addDocenteComissaoInPedido(String idPedido, Docente docente) {
        Query query = em.createQuery("UPDATE Pedido set id_docente_comissao_responsavel = :docente WHERE id = :idPedido");
        query.setParameter("docente", docente);
        query.setParameter("idPedido", Long.parseLong(idPedido));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void addDocenteOrientadorInPedido(String idPedido, Docente docente) {
        Query query = em.createQuery("UPDATE Pedido set id_docente_orientador = :id_docente_orientador WHERE id = :idPedido");
        query.setParameter("id_docente_orientador", docente);
        query.setParameter("idPedido", Long.parseLong(idPedido));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void addAvaliacaoDesempenhoDiscenteInPedido(String idPedido, String avaliacao) {
        Query query = em.createQuery("UPDATE Pedido set avaliacao_supervisor_renovacao = :avaliacao WHERE id = :idPedido");
        query.setParameter("avaliacao", avaliacao);
        query.setParameter("idPedido", Long.parseLong(idPedido));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

}
