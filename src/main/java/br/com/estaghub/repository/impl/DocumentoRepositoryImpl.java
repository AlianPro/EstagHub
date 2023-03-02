package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.*;
import br.com.estaghub.enums.TipoDocumento;
import br.com.estaghub.repository.DocumentoRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class DocumentoRepositoryImpl implements DocumentoRepository {
    EntityManagerFactory emf;
    EntityManager em;

    @Override
    public void criarDocumento(Documento documento) {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
        documento.setDataHoraCriacao(LocalDateTime.now());
        em.getTransaction().begin();
        em.persist(documento);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    @Override
    public Documento getDocumentoById(Long id) {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
        Documento documento = em.find(Documento.class,id);
        em.close();
        emf.close();
        return documento;
    }
    @Override
    public Optional<Documento> getDocumentoByIdPedidoAndTipoDocumento(Long idPedido, TipoDocumento tipoDocumento) {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
        Pedido pedido = new Pedido();
        em.getTransaction().begin();
        TypedQuery<Documento> queryGetDocumentoByIdPedidoAndTipoDocumento = em.createQuery("Select d from Documento d where d.pedido = :pedido and d.tipoDocumento = :tipo", Documento.class);
        queryGetDocumentoByIdPedidoAndTipoDocumento.setParameter("pedido", pedido.getPedidoById(idPedido));
        queryGetDocumentoByIdPedidoAndTipoDocumento.setParameter("tipo", tipoDocumento);
        
        em.close();
        emf.close();
        return Optional.ofNullable(queryGetDocumentoByIdPedidoAndTipoDocumento.getResultList().isEmpty()? null:queryGetDocumentoByIdPedidoAndTipoDocumento.getSingleResult());
    }
    @Override
    public void addPlanoAtividadesInDocumento(String idDocumento, PlanoAtividades planoAtividades) {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
        Query query = em.createQuery("UPDATE Documento set email_empresa_plano_atividades = :email_empresa_plano_atividades, endereco_empresa_plano_atividades = :endereco_empresa_plano_atividades, nome_empresa_plano_atividades = :nome_empresa_plano_atividades, nome_supervisor_plano_atividades = :nome_supervisor_plano_atividades, primeira_atividade_plano_atividades = :primeira_atividade_plano_atividades , quarta_atividade_plano_atividades = :quarta_atividade_plano_atividades , quinta_atividade_plano_atividades = :quinta_atividade_plano_atividades , responsavel_empresa_plano_atividades = :responsavel_empresa_plano_atividades , segunda_atividade_plano_atividades = :segunda_atividade_plano_atividades , telefone_empresa_plano_atividades = :telefone_empresa_plano_atividades , terceira_atividade_plano_atividades = :terceira_atividade_plano_atividades , formacao_supervisor_plano_atividades = :formacao_supervisor_plano_atividades , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idDocumento");
        query.setParameter("email_empresa_plano_atividades", planoAtividades.getEmailEmpresa());
        query.setParameter("endereco_empresa_plano_atividades", planoAtividades.getEnderecoEmpresa());
        query.setParameter("nome_supervisor_plano_atividades", planoAtividades.getNomeSupervisor());
        query.setParameter("primeira_atividade_plano_atividades", planoAtividades.getPrimeiraAtividade());
        query.setParameter("quarta_atividade_plano_atividades", planoAtividades.getQuartaAtividade());
        query.setParameter("quinta_atividade_plano_atividades", planoAtividades.getQuintaAtividade());
        query.setParameter("responsavel_empresa_plano_atividades", planoAtividades.getResponsavelEmpresa());
        query.setParameter("segunda_atividade_plano_atividades", planoAtividades.getSegundaAtividade());
        query.setParameter("telefone_empresa_plano_atividades", planoAtividades.getTelefoneEmpresa());
        query.setParameter("terceira_atividade_plano_atividades", planoAtividades.getTerceiraAtividade());
        query.setParameter("nome_empresa_plano_atividades", planoAtividades.getNomeEmpresa());
        query.setParameter("formacao_supervisor_plano_atividades", planoAtividades.getFormacaoSupervisor());
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idDocumento", Long.parseLong(idDocumento));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
    @Override
    public void addTCEInDocumento(String idDocumento, TCE tce) {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
        Query query = em.createQuery("UPDATE Documento set nome_empresa_tce = :nome_empresa_tce, cnpj_empresa_tce = :cnpj_empresa_tce, horario_inicio_tce = :horario_inicio_tce, horario_fim_tce = :horario_fim_tce, total_horas_tce = :total_horas_tce , intervalo_tce = :intervalo_tce , data_inicio_tce = :data_inicio_tce , data_fim_tce = :data_fim_tce , bolsa_tce = :bolsa_tce , aux_transporte_tce = :aux_transporte_tce , cod_apolice_tce = :cod_apolice_tce , nome_seguradora_tce = :nome_seguradora_tce , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idDocumento");
        query.setParameter("nome_empresa_tce", tce.getNomeEmpresa());
        query.setParameter("cnpj_empresa_tce", tce.getCnpjEmpresa());
        query.setParameter("horario_inicio_tce", tce.getHorarioInicio());
        query.setParameter("horario_fim_tce", tce.getHorarioFim());
        query.setParameter("total_horas_tce", tce.getTotalHoras());
        query.setParameter("intervalo_tce", tce.getIntervalo());
        query.setParameter("data_inicio_tce", tce.getDataInicio());
        query.setParameter("data_fim_tce", tce.getDataFim());
        query.setParameter("bolsa_tce", tce.getBolsa());
        query.setParameter("aux_transporte_tce", tce.getAuxTransporte());
        query.setParameter("cod_apolice_tce", tce.getCodApolice());
        query.setParameter("nome_seguradora_tce", tce.getNomeSeguradora());
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idDocumento", Long.parseLong(idDocumento));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
    @Override
    public void addTermoAditivoInDocumento(String idDocumento, TermoAditivo termoAditivo) {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
        Query query = em.createQuery("UPDATE Documento set data_antiga_termo_aditivo = :data_antiga_termo_aditivo, data_nova_termo_aditivo = :data_nova_termo_aditivo, nome_seguradora_termo_aditivo = :nome_seguradora_termo_aditivo , codigo_apolice_termo_aditivo = :codigo_apolice_termo_aditivo , data_hora_ult_atualizacao = :data_hora_ult_atualizacao WHERE id = :idDocumento");
        query.setParameter("data_antiga_termo_aditivo", termoAditivo.getDataAntiga());
        query.setParameter("data_nova_termo_aditivo", termoAditivo.getDataNova());
        query.setParameter("nome_seguradora_termo_aditivo", termoAditivo.getNomeSeguradora());
        query.setParameter("codigo_apolice_termo_aditivo", termoAditivo.getCodApolice());
        query.setParameter("data_hora_ult_atualizacao", LocalDateTime.now());
        query.setParameter("idDocumento", Long.parseLong(idDocumento));
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
