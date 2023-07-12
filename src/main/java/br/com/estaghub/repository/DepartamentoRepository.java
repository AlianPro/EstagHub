package br.com.estaghub.repository;

import br.com.estaghub.domain.Departamento;

import java.util.List;

public interface DepartamentoRepository {
    void criarDepartamento(Departamento departamento);
    void updateDepartamento(Departamento departamento);
    Boolean checkIfListOfDepartamentoAlreadyHaveThatDepartamento(String nameDepartamento, String siglaDepartamento);
    Departamento getDepartamentoById(Long id);
    List<Departamento> getAllDepartamentos();
    List<Departamento> getAllDepartamentosWithStatusActive();

}
