package br.com.estaghub.mapper;

import br.com.estaghub.domain.embeddable.TermoAditivo;
import br.com.estaghub.dto.TermoAditivoCreationDTO;

public interface TermoAditivoMapper {
    TermoAditivo toDiscenteCreateDocumento(TermoAditivoCreationDTO termoAditivoCreationDTO);
}
