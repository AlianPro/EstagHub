package br.com.estaghub.mapper;

import br.com.estaghub.domain.TCE;
import br.com.estaghub.domain.TermoAditivo;
import br.com.estaghub.dto.TCECreationDTO;
import br.com.estaghub.dto.TermoAditivoCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TermoAditivoMapper {
    TermoAditivoMapper INSTANCE = Mappers.getMapper(TermoAditivoMapper.class);
    TermoAditivo toDiscenteCreateDocumento(TermoAditivoCreationDTO termoAditivoCreationDTO);
}
