package br.com.estaghub.repository;

import br.com.estaghub.domain.Empresa;

import java.util.List;

public interface EmpresaRepository {
    void criarEmpresa(Empresa empresa);
    void alterarEmpresa(Empresa empresa);
    Empresa getEmpresaByCnpj(String cnpj);
    Empresa getEmpresaById(Long id);
    List<Empresa> listAllEmpresa();

}
