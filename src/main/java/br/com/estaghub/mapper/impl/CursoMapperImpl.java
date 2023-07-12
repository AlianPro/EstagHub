package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.dto.CursoCreationDTO;
import br.com.estaghub.mapper.CursoMapper;

public class CursoMapperImpl implements CursoMapper {
    @Override
    public Curso toDocenteCreateCurso(CursoCreationDTO cursoCreationDTO) {
        return Curso.builder().nome(cursoCreationDTO.getNome())
                .departamento(cursoCreationDTO.getDepartamento())
                .isActive(true)
                .build();
    }
}
