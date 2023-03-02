package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Docente;
import br.com.estaghub.repository.DocenteRepository;
import br.com.estaghub.util.CryptUtil;
import br.com.estaghub.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class DocenteRepositoryImpl implements DocenteRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();


    @Override
    public void criarDocente(Docente docente) {
        TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email", Docente.class);
        query.setParameter("email", docente.getEmail());
        if (query.getResultList().isEmpty()){
            docente.setSenha(CryptUtil.encryptPassword(docente.getSenha()));
            em.getTransaction().begin();
            em.persist(docente);
            em.getTransaction().commit();
        }
        em.close();
    }
    @Override
    public Docente getDocenteById(Long id) {
        try {
            return em.find(Docente.class,id);
        }finally {
            em.close();
        }
    }

    @Override
    public Boolean loginDocente(String email, String senha) {
        try {
            TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email", Docente.class);
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
            TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email", Docente.class);
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
    public Optional<Docente> getDocenteByEmail(String email) {
        try {
            TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email", Docente.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
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
    public List<Docente> getAllDocentesNoComissao() {
        try {
            return em.createQuery("SELECT d FROM Docente d where d.isDocenteComissao <> 1", Docente.class).getResultList();
        }finally {
            em.close();
        }
    }
}
