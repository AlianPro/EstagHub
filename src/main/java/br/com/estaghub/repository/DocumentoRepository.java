package br.com.estaghub.repository;

import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.embeddable.PlanoAtividades;
import br.com.estaghub.domain.embeddable.TCE;
import br.com.estaghub.domain.embeddable.TermoAditivo;
import br.com.estaghub.enums.TipoDocumento;

import java.util.List;
import java.util.Optional;

public interface DocumentoRepository {
    void criarDocumento(Documento documento);
    Optional<Documento> getDocumentoByIdPedidoAndTipoDocumento(Long idPedido, TipoDocumento tipoDocumento);
    List<Documento> getAllDocumentosFromThatPedido(Long idPedido);
    void addPlanoAtividadesInDocumento(String idDocumento, PlanoAtividades planoAtividades);
    void addTCEInDocumento(String idDocumento, TCE tce);
    void addTermoAditivoInDocumento(String idDocumento, TermoAditivo termoAditivo);
    void removeDocumento(Long idPedido, TipoDocumento tipoDocumento);
}
