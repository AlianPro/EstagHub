package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.repository.CursoRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CursoRepositoryImpl implements CursoRepository {
    EntityManagerFactory emf;
    EntityManager em;
    public CursoRepositoryImpl() {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
    }

    @Override
    public void criarCurso(Curso curso) {
        TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c WHERE c.nome = :nome", Curso.class);
        query.setParameter("nome", curso.getNome());
        if (query.getResultList().isEmpty()){
            em.getTransaction().begin();
            em.persist(curso);
            em.getTransaction().commit();
        }
        em.close();
        emf.close();
    }

    @Override
    public Optional<Curso> getCursoById(Long id) {
        TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c WHERE c.id = :id", Curso.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Curso> getAllCursos() {
        return em.createQuery("SELECT c FROM Curso c", Curso.class).getResultList();
    }
}
