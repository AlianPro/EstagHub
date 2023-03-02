package br.com.estaghub.repository;

import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.PlanoAtividades;
import br.com.estaghub.domain.TCE;
import br.com.estaghub.domain.TermoAditivo;
import br.com.estaghub.enums.TipoDocumento;

import java.util.Optional;

public interface DocumentoRepository {
    void criarDocumento(Documento documento);
    Documento getDocumentoById(Long id);
    Optional<Documento> getDocumentoByIdPedidoAndTipoDocumento(Long idPedido, TipoDocumento tipoDocumento);
    void addPlanoAtividadesInDocumento(String idDocumento, PlanoAtividades planoAtividades);
    void addTCEInDocumento(String idDocumento, TCE tce);
    void addTermoAditivoInDocumento(String idDocumento, TermoAditivo termoAditivo);
    void removeDocumento(Long idPedido, TipoDocumento tipoDocumento);
}
