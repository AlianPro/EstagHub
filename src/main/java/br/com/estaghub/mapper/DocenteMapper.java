package br.com.estaghub.mapper;

import br.com.estaghub.domain.Docente;
import br.com.estaghub.dto.DocenteCreationDTO;

public interface DocenteMapper {
    Docente toDocenteCreateAccount(DocenteCreationDTO docenteCreationDTO, Docente docenteComissao);
}
