package br.com.estaghub.repository;

import br.com.estaghub.domain.Documento;
import br.com.estaghub.domain.Pedido;
import br.com.estaghub.enums.TipoDocumento;

public interface DocumentoRepository {
    void criarDocumento(Documento documento);
    Documento getDocumentoById(Long id);

}
