package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.Departamento;
import br.com.estaghub.dto.DepartamentoCreationDTO;
import br.com.estaghub.mapper.DepartamentoMapper;

public class DepartamentoMapperImpl implements DepartamentoMapper {
    @Override
    public Departamento toDocenteCreateDepartamento(DepartamentoCreationDTO departamentoCreationDTO) {
        return Departamento.builder().nome(departamentoCreationDTO.getNome())
                .sigla(departamentoCreationDTO.getSigla())
                .build();
    }
}
