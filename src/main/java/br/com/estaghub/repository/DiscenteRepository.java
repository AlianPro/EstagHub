package br.com.estaghub.repository;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.enums.TipoPedido;

import java.util.Optional;

public interface DiscenteRepository {
    void criarDiscente(Discente discente);
    Boolean checkIfPossibleToCreateDiscente(Discente discente);
    Boolean loginDiscente(String email, String senha);
    Optional<Discente> getDiscenteByEmail(String email);
    void editProfileDiscente(Discente discente);
    void deleteDiscente(Discente discente);
    Optional<Discente> getDiscenteByMatricula(String matricula);
    void addInfoNovoPedidoInDiscente(Discente discente);
    void changePasswordDiscente(String email, String novaSenha);
    Optional<Discente> getDiscenteById(Long id);

}
