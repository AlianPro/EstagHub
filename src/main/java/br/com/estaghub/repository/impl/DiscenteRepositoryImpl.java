package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.repository.DiscenteRepository;
import br.com.estaghub.util.CryptUtil;
import br.com.estaghub.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class DiscenteRepositoryImpl implements DiscenteRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();

    @Override
    public void criarDiscente(Discente discente) {
        TypedQuery<Discente> query = em.createQuery("SELECT d FROM Discente d WHERE d.email = :email", Discente.class);
        query.setParameter("email", discente.getEmail());
        if (query.getResultList().isEmpty()){
            discente.setSenha(CryptUtil.encryptPassword(discente.getSenha()));
            em.getTransaction().begin();
            em.persist(discente);
            em.getTransaction().commit();
        }
        em.close();
    }
    @Override
    public Discente getDiscenteById(Long id) {
        try {
            return em.find(Discente.class,id);
        }finally {
            em.close();
        }
    }

    @Override
    public Boolean loginDiscente(String email, String senha) {
        try {
            TypedQuery<Discente> query = em.createQuery("SELECT d FROM Discente d WHERE d.email = :email", Discente.class);
            query.setParameter("email", email);
            if (!query.getResultList().isEmpty()){
                return CryptUtil.checkPassword(senha, query.getSingleResult().getSenha());
            }
            return false;
        }finally {
            em.close();
        }
    }

    @Override
    public Optional<Discente> getDiscenteByEmail(String email) {
        try {
            TypedQuery<Discente> query = em.createQuery("SELECT d FROM Discente d WHERE d.email = :email", Discente.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        }finally {
            em.close();
        }
    }
    @Override
    public Boolean checkIfDiscenteAlreadyHavePedido(Discente discente, TipoPedido tipoPedido) {
        try {
            TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.id_discente = :id and p.tipo = :tipo", Pedido.class);
            query.setParameter("id", discente.getId());
            query.setParameter("tipo", tipoPedido);
            return query.getResultList().size() > 0;
        }finally {
            em.close();
        }
    }
    @Override
    public void addInfoNovoPedidoInDiscente(Discente discente) {
        Query query = em.createQuery("UPDATE Discente set cpf = :cpf , rg = :rg , orgao_expedidor_rg = :orgao_expedidor_rg, ira = :ira , periodo = :periodo , carga_horaria_cumprida = :carga_horaria_cumprida , curso_id = :curso_id , endereco = :endereco WHERE id = :id");
        query.setParameter("id", discente.getId());
        query.setParameter("cpf", discente.getCpf());
        query.setParameter("rg", discente.getRg());
        query.setParameter("orgao_expedidor_rg", discente.getOrgaoExpedidorRg());
        query.setParameter("ira", discente.getIra());
        query.setParameter("periodo", discente.getPeriodo());
        query.setParameter("carga_horaria_cumprida", discente.getCargaHorariaCumprida());
        query.setParameter("curso_id", discente.getCurso().getId());
        query.setParameter("endereco", discente.getEndereco());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
