package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Departamento;
import br.com.estaghub.repository.DepartamentoRepository;
import br.com.estaghub.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class DepartamentoRepositoryImpl implements DepartamentoRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();

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
    }
    @Override
    public Departamento getDepartamentoById(Long id) {
        try {
            return em.find(Departamento.class,id);
        }finally {
            em.close();
        }
    }
    @Override
    public List<Departamento> getAllDepartamentos() {
        try {
            return em.createQuery("SELECT d FROM Departamento d", Departamento.class).getResultList();
        }finally {
            em.close();
        }
    }
}
