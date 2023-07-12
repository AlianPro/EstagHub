package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.domain.Departamento;
import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.repository.CursoRepository;
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

public class CursoRepositoryImpl implements CursoRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();


    @Override
    public void criarCurso(Curso curso) {
        em.getTransaction().begin();
        em.persist(curso);
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public Boolean checkIfDepartamentoAlreadyHaveThatCourse(String nameCourse, Departamento departamento) {
        try {
            TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c WHERE c.nome = :nome and c.departamento = :id_departamento and c.isActive = 1", Curso.class);
            query.setParameter("nome", nameCourse);
            query.setParameter("id_departamento", departamento);
            return query.getResultList().isEmpty();
        }finally {
            em.close();
        }
    }
    @Override
    public void updateCurso(Curso curso) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE Curso set ");
        if (Strings.isNotBlank(curso.getNome())){
            sql.append("nome = :nome ,");
        }
        if (!Objects.isNull(curso.getIsActive())){
            sql.append("status = :status ,");
        }
        if (!Objects.isNull(curso.getDepartamento())){
            sql.append("id_departamento = :id_departamento ,");
        }
        sql.append("data_hora_ult_atualizacao = :data_hora_ult_atualizacao ");
        sql.append("WHERE id = :idCurso");
        Query query = em.createQuery(sql.toString());
        if (Strings.isNotBlank(curso.getNome())){
            query.setParameter("nome", curso.getNome());
        }
        if (!Objects.isNull(curso.getIsActive())){
            query.setParameter("status", curso.getIsActive());
        }
        if (!Objects.isNull(curso.getDepartamento())){
            query.setParameter("id_departamento", curso.getDepartamento());
        }
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idCurso", curso.getId());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Curso> getCursoById(Long id) {
        try {
            TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c WHERE c.id = :id", Curso.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        }finally {
            em.close();
        }
    }

    @Override
    public List<Curso> getAllCursos() {
        try {
            return em.createQuery("SELECT c FROM Curso c", Curso.class).getResultList();
        }finally {
            em.close();
        }
    }
}
