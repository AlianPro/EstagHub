package br.com.estaghub.repository;

import br.com.estaghub.domain.Empresa;
import br.com.estaghub.domain.Supervisor;

public interface SupervisorRepository {
    void criarSupervisor(Supervisor supervisor, Empresa Empresa, String numPedido);
    void vincularEmpresa(Supervisor supervisor, String cnpjEmpresaVinculada, String numPedido);
    Long getIdSupervisor(Supervisor supervisor);
    Supervisor getSupervisorByEmail(String email);
    Boolean loginSupervisor(String email, String senha);

}
