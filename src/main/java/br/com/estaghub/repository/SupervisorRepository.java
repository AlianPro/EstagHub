package br.com.estaghub.repository;

import br.com.estaghub.domain.Empresa;
import br.com.estaghub.domain.Supervisor;

import java.util.Optional;

public interface SupervisorRepository {
    void criarSupervisor(Supervisor supervisor, Empresa Empresa);
    void vincularEmpresa(Supervisor supervisor, String cnpjEmpresaVinculada);
    Optional<Supervisor> getSupervisorByEmail(String email);
    Boolean loginSupervisor(String email, String senha);
    void editProfileSupervisor(Supervisor supervisor);
    void deleteSupervisor(Supervisor supervisor);
    void changePasswordSupervisor(String email, String novaSenha);

}
