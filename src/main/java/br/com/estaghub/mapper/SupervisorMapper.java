package br.com.estaghub.mapper;

import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.dto.EmpresaCreationDTO;
import br.com.estaghub.dto.SupervisorCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupervisorMapper {
    SupervisorMapper INSTANCE = Mappers.getMapper(SupervisorMapper.class);
    Supervisor toSupervisorCreateAccount(SupervisorCreationDTO supervisorCreationDTO);
}
