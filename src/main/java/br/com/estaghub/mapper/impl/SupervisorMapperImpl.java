package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.Supervisor;
import br.com.estaghub.dto.SupervisorCreationDTO;
import br.com.estaghub.mapper.SupervisorMapper;

import java.time.LocalDateTime;

public class SupervisorMapperImpl implements SupervisorMapper {
    @Override
    public Supervisor toSupervisorCreateAccount(SupervisorCreationDTO supervisorCreationDTO) {
        return Supervisor.builder().nome(supervisorCreationDTO.getNome())
                .email(supervisorCreationDTO.getEmail())
                .senha(supervisorCreationDTO.getSenha())
                .cargo(supervisorCreationDTO.getCargo())
                .telefone(supervisorCreationDTO.getTelefone())
                .dataHoraCriacao(LocalDateTime.now())
                .build();
    }

}
