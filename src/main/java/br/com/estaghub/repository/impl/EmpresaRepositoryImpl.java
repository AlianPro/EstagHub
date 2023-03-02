package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Empresa;
import br.com.estaghub.repository.EmpresaRepository;
import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Log4j2
public class EmpresaRepositoryImpl implements EmpresaRepository {
    EntityManagerFactory emf;
    EntityManager em;

    public EmpresaRepositoryImpl() {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
    }
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
        emf.close();
    }
    @Override
    public void alterarEmpresa(Empresa empresa) {
        em.getTransaction().begin();
        em.persist(empresa);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    @Override
    public Empresa getEmpresaByCnpj(String cnpj) {

            TypedQuery<Empresa> queryFinal = em.createQuery("SELECT e FROM Empresa e WHERE e.cnpj = :cnpj", Empresa.class);
            queryFinal.setParameter("cnpj", cnpj);
            log.error(queryFinal);
//            em.close();
//            emf.close();
            return queryFinal.getResultList().isEmpty()? null: queryFinal.getSingleResult();
    }

    @Override
    public Empresa getEmpresaById(Long id) {
        Empresa empresa = em.find(Empresa.class,id);
        em.close();
        emf.close();
        return empresa;
    }

    @Override
    public List<Empresa> listAllEmpresa() {
        List<Empresa> resultList = em.createQuery("FROM Empresa").getResultList();
        em.close();
        emf.close();
        return resultList.isEmpty()? Collections.emptyList(): resultList;
    }
}
