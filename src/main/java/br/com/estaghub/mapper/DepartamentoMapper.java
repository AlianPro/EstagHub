package br.com.estaghub.mapper;

import br.com.estaghub.domain.Departamento;
import br.com.estaghub.dto.DepartamentoCreationDTO;

public interface DepartamentoMapper {
    Departamento toDocenteCreateDepartamento(DepartamentoCreationDTO departamentoCreationDTO);
}
