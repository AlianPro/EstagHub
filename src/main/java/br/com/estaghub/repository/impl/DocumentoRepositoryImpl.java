package br.com.estaghub.repository.impl;

import br.com.estaghub.domain.Documento;
import br.com.estaghub.repository.DocumentoRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class DocumentoRepositoryImpl implements DocumentoRepository {
    EntityManagerFactory emf;
    EntityManager em;
    public DocumentoRepositoryImpl() {
        this.emf = Persistence.createEntityManagerFactory("estaghub");
        this.em = this.emf.createEntityManager();
    }

    @Override
    public void criarDocumento(Documento documento) {
        documento.setDataHoraCriacao(LocalDateTime.now());
        em.getTransaction().begin();
        em.persist(documento);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    @Override
    public Documento getDocumentoById(Long id) {
        Documento documento = em.find(Documento.class,id);
        em.close();
        emf.close();
        return documento;
    }
}
