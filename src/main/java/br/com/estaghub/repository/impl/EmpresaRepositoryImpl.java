package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Empresa;
import br.com.estaghub.repository.EmpresaRepository;
import br.com.estaghub.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Log4j2
public class EmpresaRepositoryImpl implements EmpresaRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();

    @Override
    public void criarEmpresa(Empresa empresa) {
        TypedQuery<Empresa> query = em.createQuery("SELECT e FROM Empresa e WHERE e.cnpj = :cnpj or e.email = :email", Empresa.class);
        query.setParameter("cnpj", empresa.getCnpj());
        query.setParameter("email", empresa.getEmail());
        if (query.getResultList().isEmpty()){
            em.getTransaction().begin();
            em.persist(empresa);
            em.getTransaction().commit();
        }
        em.close();
    }
    @Override
    public void alterarEmpresa(Empresa empresa) {
        em.getTransaction().begin();
        em.persist(empresa);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Empresa getEmpresaByCnpj(String cnpj) {
        try {
            TypedQuery<Empresa> queryFinal = em.createQuery("SELECT e FROM Empresa e WHERE e.cnpj = :cnpj", Empresa.class);
            queryFinal.setParameter("cnpj", cnpj);
            return queryFinal.getResultList().isEmpty()? null: queryFinal.getSingleResult();
        }finally {
            em.close();
        }
    }

    @Override
    public Empresa getEmpresaById(Long id) {
        try {
            return em.find(Empresa.class,id);
        }finally {
            em.close();
        }
    }

    @Override
    public List<Empresa> listAllEmpresa() {
        try {
            List<Empresa> resultList = em.createQuery("FROM Empresa").getResultList();
            return resultList.isEmpty()? Collections.emptyList(): resultList;
        }finally {
            em.close();
        }
    }
}
