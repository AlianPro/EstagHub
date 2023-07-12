package br.com.estaghub.mapper;

import br.com.estaghub.domain.Empresa;
import br.com.estaghub.dto.EmpresaCreationDTO;

public interface EmpresaMapper {
    Empresa toEmpresaCreateAccount(EmpresaCreationDTO empresaCreationDTO);
}
