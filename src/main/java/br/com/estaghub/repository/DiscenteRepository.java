package br.com.estaghub.repository;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.enums.TipoPedido;

import java.util.Optional;

public interface DiscenteRepository {
    void criarDiscente(Discente discente);
    Boolean loginDiscente(String email, String senha);
    Optional<Discente> getDiscenteByEmail(String email);
    Boolean checkIfDiscenteAlreadyHavePedido(Discente discente, TipoPedido tipoPedido);
    void addInfoNovoPedidoInDiscente(Discente discente);

}
