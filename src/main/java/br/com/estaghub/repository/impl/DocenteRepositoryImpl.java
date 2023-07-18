package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Departamento;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.repository.DocenteRepository;
import br.com.estaghub.util.CryptUtil;
import br.com.estaghub.util.HibernateUtil;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DocenteRepositoryImpl implements DocenteRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();

    @Override
    public void criarDocente(Docente docente) {
        docente.setSenha(CryptUtil.encryptPassword(docente.getSenha()));
        docente.setIsActive(true);
        em.getTransaction().begin();
        em.persist(docente);
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void updateDocente(Docente docente) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE Docente set ");
        if (Strings.isNotBlank(docente.getNome())){
            sql.append("nome = :nome ,");
        }
        if (Strings.isNotBlank(docente.getEmail())){
            sql.append("email = :email ,");
        }
        if (Strings.isNotBlank(docente.getSiape())){
            sql.append("siape = :siape ,");
        }
        if (!Objects.isNull(docente.getIsActive())){
            sql.append("status = :status ,");
        }
        if (!Objects.isNull(docente.getIsDocenteComissao())){
            sql.append("docente_comissao = :docente_comissao ,");
        }
        if (!Objects.isNull(docente.getDepartamento())){
            sql.append("id_departamento = :id_departamento ,");
        }
        sql.append("data_hora_ult_atualizacao = :data_hora_ult_atualizacao ");
        sql.append("WHERE id = :idDocente");
        Query query = em.createQuery(sql.toString());
        if (Strings.isNotBlank(docente.getNome())){
            query.setParameter("nome", docente.getNome());
        }
        if (Strings.isNotBlank(docente.getEmail())){
            query.setParameter("email", docente.getEmail());
        }
        if (Strings.isNotBlank(docente.getSiape())){
            query.setParameter("siape", docente.getSiape());
        }
        if (!Objects.isNull(docente.getIsActive())){
            query.setParameter("status", docente.getIsActive());
        }
        if (!Objects.isNull(docente.getIsDocenteComissao())){
            query.setParameter("docente_comissao", docente.getIsDocenteComissao());
        }
        if (!Objects.isNull(docente.getDepartamento())){
            query.setParameter("id_departamento", docente.getDepartamento());
        }
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idDocente", docente.getId());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public Optional<Docente> getDocenteById(Long id) {
        try {
            TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.id = :id", Docente.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        }finally {
            em.close();
        }
    }

    @Override
    public Boolean loginDocente(String email, String senha) {
        try {
            TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email and d.isActive = 1", Docente.class);
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
    public Boolean checkIfDocenteIsComissao(String email) {
        try {
            TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email and d.isActive = 1", Docente.class);
            query.setParameter("email", email);
            if (!query.getResultList().isEmpty()){
                return query.getSingleResult().getIsDocenteComissao();
            }
            return false;
        }finally {
            em.close();
        }
    }
    @Override
    public void editProfileDocente(Docente docente) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE Docente set ");
        if (Strings.isNotBlank(docente.getNome())){
            sql.append("nome = :nome ,");
        }
        if (Strings.isNotBlank(docente.getSiape())){
            sql.append("siape = :siape ,");
        }
        sql.append("data_hora_ult_atualizacao = :data_hora_ult_atualizacao ");
        sql.append("WHERE id = :idDocente");
        Query query = em.createQuery(sql.toString());
        if (Strings.isNotBlank(docente.getNome())){
            query.setParameter("nome", docente.getNome());
        }
        if (Strings.isNotBlank(docente.getSiape())){
            query.setParameter("siape", docente.getSiape());
        }
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idDocente", docente.getId());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void deleteDocente(Docente docente) {
        Query query = em.createQuery("UPDATE Docente set status = :status , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :id");
        query.setParameter("id", docente.getId());
        query.setParameter("status", docente.getIsActive());
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Boolean checkSiapeOfDocente(Docente docente) {
        try {
            TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.siape = :siape and d.isActive = 1", Docente.class);
            query.setParameter("siape", docente.getSiape());
            if (!query.getResultList().isEmpty()){
                return true;
            }
            return false;
        }finally {
            em.close();
        }
    }
    @Override
    public Optional<Docente> getDocenteByEmail(String email) {
        try {
            TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email and d.isActive = 1", Docente.class);
            query.setParameter("email", email);
            return query.getResultList().isEmpty()? Optional.empty(): Optional.ofNullable(query.getSingleResult());
        }finally {
            em.close();
        }
    }
    @Override
    public List<Docente> getAllDocentes() {
        try {
            return em.createQuery("SELECT d FROM Docente d ", Docente.class).getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public List<Docente> getAllDocentesOutComissao() {
        try {
            return em.createQuery("SELECT d FROM Docente d where d.isDocenteComissao <> 1 and d.isActive = 1", Docente.class).getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public List<Docente> getAllDocentesOfComissaoFromThisDepartamento(Departamento departamento) {
        try {
            TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d where d.isDocenteComissao = 1 and d.departamento = :departamento and d.isActive = 1", Docente.class);
            query.setParameter("departamento", departamento);
            return query.getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public void changePasswordDocente(String email, String novaSenha) {
        Query query = em.createQuery("UPDATE Docente set senha = :senha WHERE email = :email and status = 1");
        query.setParameter("email", email);
        query.setParameter("senha", CryptUtil.encryptPassword(novaSenha));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
