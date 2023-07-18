package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.enums.TipoPedido;
import br.com.estaghub.repository.DiscenteRepository;
import br.com.estaghub.util.CryptUtil;
import br.com.estaghub.util.HibernateUtil;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class DiscenteRepositoryImpl implements DiscenteRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();

    @Override
    public void criarDiscente(Discente discente) {
        discente.setSenha(CryptUtil.encryptPassword(discente.getSenha()));
        discente.setIsActive(true);
        em.getTransaction().begin();
        em.persist(discente);
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public Boolean checkIfPossibleToCreateDiscente(Discente discente) {
        try{
            TypedQuery<Discente> query = em.createQuery("SELECT d FROM Discente d WHERE (d.email = :email or d.matricula = :matricula) and d.isActive = 1", Discente.class);
            query.setParameter("email", discente.getEmail());
            query.setParameter("matricula", discente.getMatricula());
            return query.getResultList().isEmpty();
        }finally {
            em.close();
        }
    }
    @Override
    public Optional<Discente> getDiscenteById(Long id) {
        try {
            TypedQuery<Discente> query = em.createQuery("SELECT d FROM Discente d WHERE d.id = :id", Discente.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        }finally {
            em.close();
        }
    }

    @Override
    public Boolean loginDiscente(String email, String senha) {
        try {
            TypedQuery<Discente> query = em.createQuery("SELECT d FROM Discente d WHERE d.email = :email and d.isActive = 1", Discente.class);
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
            TypedQuery<Discente> query = em.createQuery("SELECT d FROM Discente d WHERE d.email = :email and d.isActive = 1", Discente.class);
            query.setParameter("email", email);
            return query.getResultList().isEmpty()? Optional.empty(): Optional.ofNullable(query.getSingleResult());
        }finally {
            em.close();
        }
    }
    @Override
    public Optional<Discente> getDiscenteByMatricula(String matricula) {
        try {
            TypedQuery<Discente> query = em.createQuery("SELECT d FROM Discente d WHERE d.matricula = :matricula and d.isActive = 1", Discente.class);
            query.setParameter("matricula", matricula);
            return query.getResultList().isEmpty()? Optional.empty(): Optional.ofNullable(query.getSingleResult());
        }finally {
            em.close();
        }
    }
    @Override
    public void editProfileDiscente(Discente discente) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE Discente set ");
        if (Strings.isNotBlank(discente.getNome())){
            sql.append("nome = :nome ,");
        }
        if (Strings.isNotBlank(discente.getMatricula())){
            sql.append("matricula = :matricula ,");
        }
        if (Strings.isNotBlank(discente.getTelefone())){
            sql.append("telefone = :telefone ,");
        }
        sql.append("data_hora_ult_atualizacao = :data_hora_ult_atualizacao ");
        sql.append("WHERE id = :idDiscente");
        Query query = em.createQuery(sql.toString());
        if (Strings.isNotBlank(discente.getNome())){
            query.setParameter("nome", discente.getNome());
        }
        if (Strings.isNotBlank(discente.getMatricula())){
            query.setParameter("matricula", discente.getMatricula());
        }
        if (Strings.isNotBlank(discente.getTelefone())){
            query.setParameter("telefone", discente.getTelefone());
        }
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idDiscente", discente.getId());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void deleteDiscente(Discente discente) {
        Query query = em.createQuery("UPDATE Discente set status = :status , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :id");
        query.setParameter("id", discente.getId());
        query.setParameter("status", discente.getIsActive());
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
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
    @Override
    public void changePasswordDiscente(String email, String novaSenha) {
        Query query = em.createQuery("UPDATE Discente set senha = :senha WHERE email = :email and status = 1");
        query.setParameter("email", email);
        query.setParameter("senha", CryptUtil.encryptPassword(novaSenha));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
