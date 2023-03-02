package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Departamento;
import br.com.estaghub.repository.DepartamentoRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class DepartamentoRepositoryImpl implements DepartamentoRepository {
    EntityManagerFactory emf;
    EntityManager em;
    public DepartamentoRepositoryImpl() {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
    }
    @Override
    public void criarDepartamento(Departamento departamento) {
        TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d WHERE d.sigla = :sigla", Departamento.class);
        query.setParameter("sigla", departamento.getSigla());
        if (query.getResultList().isEmpty()){
            em.getTransaction().begin();
            em.persist(departamento);
            em.getTransaction().commit();
        }
        em.close();
        emf.close();
    }
    @Override
    public Departamento getDepartamentoById(Long id) {
        return em.find(Departamento.class,id);
    }
    @Override
    public List<Departamento> getAllDepartamentos() {
        return em.createQuery("SELECT d FROM Departamento d", Departamento.class).getResultList();
    }
}
