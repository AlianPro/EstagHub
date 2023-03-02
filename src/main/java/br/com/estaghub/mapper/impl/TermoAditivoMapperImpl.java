package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.TermoAditivo;
import br.com.estaghub.dto.TermoAditivoCreationDTO;
import br.com.estaghub.mapper.TermoAditivoMapper;

public class TermoAditivoMapperImpl implements TermoAditivoMapper {

    @Override
    public TermoAditivo toDiscenteCreateDocumento(TermoAditivoCreationDTO termoAditivoCreationDTO) {
        return TermoAditivo.builder().dataAntiga(termoAditivoCreationDTO.getDataAntiga())
                .dataNova(termoAditivoCreationDTO.getDataNova())
                .nomeSeguradora(termoAditivoCreationDTO.getNomeSeguradora())
                .codApolice(termoAditivoCreationDTO.getCodApolice())
                .build();
    }
}
