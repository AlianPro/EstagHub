package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Departamento;
import br.com.estaghub.repository.DepartamentoRepository;
import br.com.estaghub.util.HibernateUtil;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class DepartamentoRepositoryImpl implements DepartamentoRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();

    @Override
    public void criarDepartamento(Departamento departamento) {
        em.getTransaction().begin();
        em.persist(departamento);
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void updateDepartamento(Departamento departamento) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE Departamento set ");
        if (Strings.isNotBlank(departamento.getNome())){
            sql.append("nome = :nome ,");
        }
        if (Strings.isNotBlank(departamento.getSigla())){
            sql.append("sigla = :sigla ,");
        }
        if (!Objects.isNull(departamento.getIsActive())){
            sql.append("status = :status ,");
        }
        sql.append("data_hora_ult_atualizacao = :data_hora_ult_atualizacao ");
        sql.append("WHERE id = :idDepartamento");
        Query query = em.createQuery(sql.toString());
        if (Strings.isNotBlank(departamento.getNome())){
            query.setParameter("nome", departamento.getNome());
        }
        if (Strings.isNotBlank(departamento.getSigla())){
            query.setParameter("sigla", departamento.getSigla());
        }
        if (!Objects.isNull(departamento.getIsActive())){
            query.setParameter("status", departamento.getIsActive());
        }
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idDepartamento", departamento.getId());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public Boolean checkIfListOfDepartamentoAlreadyHaveThatDepartamento(String nameDepartamento, String siglaDepartamento) {
        try {
            TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d WHERE ( d.nome = :nome or d.sigla = :sigla) and d.isActive = 1", Departamento.class);
            query.setParameter("nome", nameDepartamento);
            query.setParameter("sigla", siglaDepartamento);
            return query.getResultList().isEmpty();
        }finally {
            em.close();
        }
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
    @Override
    public List<Departamento> getAllDepartamentosWithStatusActive() {
        try {
            return em.createQuery("SELECT d FROM Departamento d where d.isActive = 1", Departamento.class).getResultList();
        }finally {
            em.close();
        }
    }
}
