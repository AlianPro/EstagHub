package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Empresa;
import br.com.estaghub.repository.EmpresaRepository;
import br.com.estaghub.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public Optional<Empresa> getEmpresaByCnpj(String cnpj) {
        try {
            TypedQuery<Empresa> queryFinal = em.createQuery("SELECT e FROM Empresa e WHERE e.cnpj = :cnpj", Empresa.class);
            queryFinal.setParameter("cnpj", cnpj);
            return queryFinal.getResultList().isEmpty()? Optional.empty(): Optional.ofNullable(queryFinal.getSingleResult());
        }finally {
            em.close();
        }
    }
    @Override
    public Optional<Empresa> getEmpresaByEmail(String email) {
        try {
            TypedQuery<Empresa> queryFinal = em.createQuery("SELECT e FROM Empresa e WHERE e.email = :email", Empresa.class);
            queryFinal.setParameter("email", email);
            return queryFinal.getResultList().isEmpty()? Optional.empty(): Optional.ofNullable(queryFinal.getSingleResult());
        }finally {
            em.close();
        }
    }
    @Override
    public Boolean checkIfPossibleToCreateEmpresa(String email, String cnpj) {
        try{
            TypedQuery<Empresa> query = em.createQuery("SELECT e FROM Empresa e WHERE e.email = :email or e.cnpj = :cnpj", Empresa.class);
            query.setParameter("email", email);
            query.setParameter("cnpj", cnpj);
            return query.getResultList().isEmpty();
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
