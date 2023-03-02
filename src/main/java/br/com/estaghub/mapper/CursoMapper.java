package br.com.estaghub.mapper;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.dto.CursoCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CursoMapper {
    CursoMapper INSTANCE = Mappers.getMapper(CursoMapper.class);
    Curso toDocenteCreateCurso(CursoCreationDTO cursoCreationDTO);
}
