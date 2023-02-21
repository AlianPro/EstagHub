package br.com.estaghub.repository;

import br.com.estaghub.domain.Docente;

import java.util.Optional;

public interface DocenteRepository {
    void criarDocente(Docente Docente);
    Boolean loginDocente(String email, String senha);
    Boolean checkIfDocenteIsComissao(String email);
    Optional<Docente> getDocenteByEmail(String email);
}
