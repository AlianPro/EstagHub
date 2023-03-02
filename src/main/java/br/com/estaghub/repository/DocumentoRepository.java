package br.com.estaghub.repository;

import br.com.estaghub.domain.*;
import br.com.estaghub.enums.TipoDocumento;

import java.util.Optional;

public interface DocumentoRepository {
    void criarDocumento(Documento documento);
    Documento getDocumentoById(Long id);
    Optional<Documento> getDocumentoByIdPedidoAndTipoDocumento(Long idPedido, TipoDocumento tipoDocumento);
    void addPlanoAtividadesInDocumento(String idDocumento, PlanoAtividades planoAtividades);
    void addTCEInDocumento(String idDocumento, TCE tce);
    void addTermoAditivoInDocumento(String idDocumento, TermoAditivo termoAditivo);

}
