package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.Empresa;
import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.dto.EmpresaCreationDTO;
import br.com.estaghub.dto.SupervisorCreationDTO;
import br.com.estaghub.mapper.EmpresaMapper;

import java.time.LocalDateTime;

public class EmpresaMapperImpl implements EmpresaMapper {
    @Override
    public Empresa toEmpresaCreateAccount(EmpresaCreationDTO empresaCreationDTO) {
        return Empresa.builder().nome(empresaCreationDTO.getNome())
                .cnpj(empresaCreationDTO.getCnpj())
                .telefone(empresaCreationDTO.getTelefone())
                .email(empresaCreationDTO.getEmail())
                .endereco(empresaCreationDTO.getEndereco())
                .dataHoraCriacao(LocalDateTime.now())
                .build();
    }
}
