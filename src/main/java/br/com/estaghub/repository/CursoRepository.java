package br.com.estaghub.repository;

import br.com.estaghub.domain.Curso;
import br.com.estaghub.domain.Departamento;

import java.util.List;
import java.util.Optional;

public interface CursoRepository {
    void criarCurso(Curso curso);
    Boolean checkIfDepartamentoAlreadyHaveThatCourse(String nameCourse, Departamento departamento);
    void updateCurso(Curso curso);
    Optional<Curso> getCursoById(Long id);
    List<Curso> getAllCursos();

}
