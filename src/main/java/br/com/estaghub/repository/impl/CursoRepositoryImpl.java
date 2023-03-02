package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.repository.CursoRepository;
import br.com.estaghub.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CursoRepositoryImpl implements CursoRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();


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
