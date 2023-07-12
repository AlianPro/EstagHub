package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.Docente;
import br.com.estaghub.dto.DocenteCreationDTO;
import br.com.estaghub.mapper.DocenteMapper;

import java.time.LocalDateTime;

public class DocenteMapperImpl implements DocenteMapper {

    @Override
    public Docente toDocenteCreateAccount(DocenteCreationDTO docenteCreationDTO, Docente docenteComissao) {
        return Docente.builder().nome(docenteCreationDTO.getNome())
                .email(docenteCreationDTO.getEmail())
                .senha(docenteCreationDTO.getSenha())
                .siape(docenteCreationDTO.getSiape())
                .isDocenteComissao(docenteCreationDTO.getIsDocenteComissao())
                .departamento(docenteCreationDTO.getDepartamento())
                .dataHoraCriacao(LocalDateTime.now())
                .docenteResponsavelCriacao(docenteComissao)
                .isActive(true)
                .build();
    }
}
