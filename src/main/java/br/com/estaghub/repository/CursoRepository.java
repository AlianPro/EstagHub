package br.com.estaghub.repository;

import br.com.estaghub.domain.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoRepository {
//    void criarCurso(Curso curso);
    Optional<Curso> getCursoById(Long id);
    List<Curso> getAllCursos();

}
