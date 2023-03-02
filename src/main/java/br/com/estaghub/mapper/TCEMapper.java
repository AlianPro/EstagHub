package br.com.estaghub.mapper;

import br.com.estaghub.domain.TCE;
import br.com.estaghub.dto.TCECreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TCEMapper {
    TCEMapper INSTANCE = Mappers.getMapper(TCEMapper.class);
    TCE toDiscenteCreateDocumento(TCECreationDTO tceCreationDTO);
}
