package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.domain.Docente;
import br.com.estaghub.repository.DocenteRepository;
import br.com.estaghub.util.CryptUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
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
//        TypedQuery<Discente> query = em.createQuery("SELECT d FROM Discente d WHERE d.email = :email", Discente.class);
//        query.setParameter("email", discente.getEmail());
//        if (query.getResultList().isEmpty()){
//            discente.setSenha(CryptUtil.encryptPassword(discente.getSenha()));
//            em.getTransaction().begin();
//            em.persist(discente);
//            em.getTransaction().commit();
//        }
//        em.close();
//        emf.close();
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
}
