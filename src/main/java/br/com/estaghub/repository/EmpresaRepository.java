package br.com.estaghub.repository;

import br.com.estaghub.domain.Empresa;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository {
    void criarEmpresa(Empresa empresa);
    Optional<Empresa> getEmpresaByCnpj(String cnpj);
    Optional<Empresa> getEmpresaByEmail(String email);
    Boolean checkIfPossibleToCreateEmpresa(String email, String cnpj);
    List<Empresa> listAllEmpresa();

}
