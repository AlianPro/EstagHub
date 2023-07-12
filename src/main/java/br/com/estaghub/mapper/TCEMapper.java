package br.com.estaghub.mapper;

import br.com.estaghub.domain.embeddable.TCE;
import br.com.estaghub.dto.TCECreationDTO;

public interface TCEMapper {
    TCE toDiscenteCreateDocumento(TCECreationDTO tceCreationDTO);
}
