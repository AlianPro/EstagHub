package br.com.estaghub.repository;

import br.com.estaghub.domain.Departamento;
import br.com.estaghub.domain.Docente;

import java.util.List;
import java.util.Optional;

public interface DocenteRepository {
    void criarDocente(Docente Docente);
    void updateDocente(Docente docente);
    Boolean loginDocente(String email, String senha);
    Boolean checkIfDocenteIsComissao(String email);
    void editProfileDocente(Docente docente);
    void deleteDocente(Docente docente);
    Boolean checkSiapeOfDocente(Docente docente);
    Optional<Docente> getDocenteByEmail(String email);
    List<Docente> getAllDocentes();
    List<Docente> getAllDocentesOutComissao();
    List<Docente> getAllDocentesOfComissaoFromThisDepartamento(Departamento departamento);
    Optional<Docente> getDocenteById(Long id);
    void changePasswordDocente(String email, String novaSenha);
}
