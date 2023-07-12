package br.com.estaghub.mapper;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.dto.CursoCreationDTO;

public interface CursoMapper {
    Curso toDocenteCreateCurso(CursoCreationDTO cursoCreationDTO);
}
