package br.com.estaghub.mapper;

import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.dto.SupervisorCreationDTO;

public interface SupervisorMapper {
    Supervisor toSupervisorCreateAccount(SupervisorCreationDTO supervisorCreationDTO);
}
