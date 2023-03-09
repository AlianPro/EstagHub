package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Empresa;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.repository.SupervisorRepository;
import br.com.estaghub.util.CryptUtil;
import br.com.estaghub.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Objects;

public class SupervisorRepositoryImpl implements SupervisorRepository {
    EntityManager em = HibernateUtil.emf.createEntityManager();

    @Override
    public void criarSupervisor(Supervisor supervisor, Empresa empresa, String numPedido) {
        TypedQuery<Supervisor> query = em.createQuery("SELECT s FROM Supervisor s WHERE s.email = :email", Supervisor.class);
        query.setParameter("email", supervisor.getEmail());
        Pedido pedido = new Pedido();
        if (query.getResultList().isEmpty()){
            if (Objects.isNull(empresa.getEmpresaByCnpj(empresa.getCnpj()))) {
                empresa.criarEmpresa(empresa);
            }
            supervisor.setEmpresa(empresa);
            supervisor.setSenha(CryptUtil.encryptPassword(supervisor.getSenha()));
            em.getTransaction().begin();
            em.persist(supervisor);
            em.getTransaction().commit();
            pedido.addSupervisorNoPedido(supervisor,numPedido);
        }
        em.close();
    }
    @Override
    public void vincularEmpresa(Supervisor supervisor, String cnpjEmpresaVinculada, String numPedido) {
        TypedQuery<Supervisor> query = em.createQuery("SELECT s FROM Supervisor s WHERE s.email = :email", Supervisor.class);
        query.setParameter("email", supervisor.getEmail());
        Pedido pedido = new Pedido();
        EmpresaRepositoryImpl empresaRepository = new EmpresaRepositoryImpl();
        Empresa empresa = empresaRepository.getEmpresaByCnpj(cnpjEmpresaVinculada);
        if (query.getResultList().isEmpty()){
            if (Objects.nonNull(empresa)) {
                supervisor.setEmpresa(empresa);
                supervisor.setSenha(CryptUtil.encryptPassword(supervisor.getSenha()));
                em.getTransaction().begin();
                em.persist(supervisor);
                em.getTransaction().commit();
                pedido.addSupervisorNoPedido(supervisor,numPedido);
            }
        }
        em.close();
    }

    @Override
    public Long getIdSupervisor(Supervisor supervisor) {
        try {
            TypedQuery<Supervisor> queryFinal = em.createQuery("SELECT e FROM Supervisor e WHERE e.email = :email", Supervisor.class);
            queryFinal.setParameter("email", supervisor.getEmail());
            return queryFinal.getResultList().isEmpty()? null: queryFinal.getSingleResult().getId();
        }finally {
            em.close();
        }
    }
    public Supervisor getSupervisorByEmail(String email) {
        try {
            TypedQuery<Supervisor> queryFinal = em.createQuery("SELECT e FROM Supervisor e WHERE e.email = :email", Supervisor.class);
            queryFinal.setParameter("email", email);
            return queryFinal.getResultList().isEmpty()? null: queryFinal.getSingleResult();
        }finally {
            em.close();
        }
    }
    @Override
    public Boolean loginSupervisor(String email, String senha) {
        try {
            TypedQuery<Supervisor> query = em.createQuery("SELECT s FROM Supervisor s WHERE s.email = :email", Supervisor.class);
            query.setParameter("email", email);
            if (!query.getResultList().isEmpty()){
                return CryptUtil.checkPassword(senha, query.getSingleResult().getSenha());
            }
            return false;
        }finally {
            em.close();
        }
    }
//    @Override
//    public void addFormacaoSupervisor(String idPedido, String formacao) {
//        if (Objects.nonNull(getPedidoById(Long.parseLong(idPedido)))){
//            Query query = em.createQuery("UPDATE Pedido set status = :status , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idPedido");
//            query.setParameter("status", statusPedido);
//            query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
//            query.setParameter("idPedido", Long.parseLong(idPedido));
//            em.getTransaction().begin();
//            query.executeUpdate();
//            em.getTransaction().commit();
//            em.close();
//            emf.close();
//        }
//    }
}
