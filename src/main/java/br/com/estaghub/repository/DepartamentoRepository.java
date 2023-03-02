package br.com.estaghub.repository;

import br.com.estaghub.domain.Departamento;

import java.util.List;

public interface DepartamentoRepository {
    void criarDepartamento(Departamento departamento);
    Departamento getDepartamentoById(Long id);
    List<Departamento> getAllDepartamentos();

}
