package br.com.estaghub.mapper;

import br.com.estaghub.domain.Docente;
import br.com.estaghub.dto.DocenteCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocenteMapper {
    DocenteMapper INSTANCE = Mappers.getMapper(DocenteMapper.class);
    Docente toDocenteCreateAccount(DocenteCreationDTO docenteCreationDTO, Docente docenteComissao);
}
