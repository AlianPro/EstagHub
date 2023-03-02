package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.repository.DocenteRepository;
import br.com.estaghub.util.CryptUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class DocenteRepositoryImpl implements DocenteRepository {
    EntityManagerFactory emf;
    EntityManager em;
    public DocenteRepositoryImpl() {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
    }

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
        emf.close();
    }
    @Override
    public Docente getDocenteById(Long id) {
        Docente docente = em.find(Docente.class,id);
//        em.close();
//        emf.close();
        return docente;
    }

    @Override
    public Boolean loginDocente(String email, String senha) {
        TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email", Docente.class);
        query.setParameter("email", email);
        if (!query.getResultList().isEmpty()){
            return CryptUtil.checkPassword(senha, query.getSingleResult().getSenha());
        }
        return false;
    }
    @Override
    public Boolean checkIfDocenteIsComissao(String email) {
        TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email", Docente.class);
        query.setParameter("email", email);
        if (!query.getResultList().isEmpty()){
            return query.getSingleResult().getIsDocenteComissao();
        }
        return false;
    }
    @Override
    public Optional<Docente> getDocenteByEmail(String email) {
        TypedQuery<Docente> query = em.createQuery("SELECT d FROM Docente d WHERE d.email = :email", Docente.class);
        query.setParameter("email", email);
        return Optional.ofNullable(query.getSingleResult());
    }
    @Override
    public List<Docente> getAllDocentes() {
        return em.createQuery("SELECT d FROM Docente d ", Docente.class).getResultList();
    }
    @Override
    public List<Docente> getAllDocentesNoComissao() {
        return em.createQuery("SELECT d FROM Docente d where d.isDocenteComissao <> 1", Docente.class).getResultList();
    }
}
