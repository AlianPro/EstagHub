package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Empresa;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.repository.SupervisorRepository;
import br.com.estaghub.util.CryptUtil;
import br.com.estaghub.util.HibernateUtil;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Optional;

public class SupervisorRepositoryImpl implements SupervisorRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();

    @Override
    public void criarSupervisor(Supervisor supervisor, Empresa empresa) {
        empresa.criarEmpresa(empresa);
        supervisor.setEmpresa(empresa);
        supervisor.setSenha(CryptUtil.encryptPassword(supervisor.getSenha()));
        supervisor.setIsActive(true);
        em.getTransaction().begin();
        em.persist(supervisor);
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void vincularEmpresa(Supervisor supervisor, String cnpjEmpresaVinculada) {
        supervisor.setEmpresa(new Empresa().getEmpresaByCnpj(cnpjEmpresaVinculada).get());
        supervisor.setSenha(CryptUtil.encryptPassword(supervisor.getSenha()));
        supervisor.setIsActive(true);
        em.getTransaction().begin();
        em.persist(supervisor);
        em.getTransaction().commit();
        em.close();
    }
    public Optional<Supervisor> getSupervisorByEmail(String email) {
        try {
            TypedQuery<Supervisor> query = em.createQuery("SELECT s FROM Supervisor s WHERE s.email = :email and s.isActive = 1", Supervisor.class);
            query.setParameter("email", email);
            return query.getResultList().isEmpty()? Optional.empty(): Optional.ofNullable(query.getSingleResult());
        }finally {
            em.close();
        }
    }
    @Override
    public Boolean loginSupervisor(String email, String senha) {
        try {
            TypedQuery<Supervisor> query = em.createQuery("SELECT s FROM Supervisor s WHERE s.email = :email and s.isActive = 1", Supervisor.class);
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
    public void editProfileSupervisor(Supervisor supervisor) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE Supervisor set ");
        if (Strings.isNotBlank(supervisor.getNome())){
            sql.append("nome = :nome ,");
        }
        if (Strings.isNotBlank(supervisor.getCargo())){
            sql.append("cargo = :cargo ,");
        }
        if (Strings.isNotBlank(supervisor.getTelefone())){
            sql.append("telefone = :telefone ,");
        }
        sql.append("data_hora_ult_atualizacao = :data_hora_ult_atualizacao ");
        sql.append("WHERE id = :idSupervisor");
        Query query = em.createQuery(sql.toString());
        if (Strings.isNotBlank(supervisor.getNome())){
            query.setParameter("nome", supervisor.getNome());
        }
        if (Strings.isNotBlank(supervisor.getCargo())){
            query.setParameter("cargo", supervisor.getCargo());
        }
        if (Strings.isNotBlank(supervisor.getTelefone())){
            query.setParameter("telefone", supervisor.getTelefone());
        }
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idSupervisor", supervisor.getId());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void deleteSupervisor(Supervisor supervisor) {
        Query query = em.createQuery("UPDATE Supervisor set status = :status , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :id");
        query.setParameter("id", supervisor.getId());
        query.setParameter("status", supervisor.getIsActive());
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    @Override
    public void changePasswordSupervisor(String email, String novaSenha) {
        Query query = em.createQuery("UPDATE Supervisor set senha = :senha WHERE email = :email and status = 1");
        query.setParameter("email", email);
        query.setParameter("senha", CryptUtil.encryptPassword(novaSenha));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
