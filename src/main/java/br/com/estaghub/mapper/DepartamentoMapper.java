package br.com.estaghub.mapper;

import br.com.estaghub.domain.Departamento;
import br.com.estaghub.dto.DepartamentoCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartamentoMapper {
    DepartamentoMapper INSTANCE = Mappers.getMapper(DepartamentoMapper.class);
    Departamento toDocenteCreateDepartamento(DepartamentoCreationDTO departamentoCreationDTO);
}
