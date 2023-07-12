package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.embeddable.TCE;
import br.com.estaghub.dto.TCECreationDTO;
import br.com.estaghub.mapper.TCEMapper;

public class TCEMapperImpl implements TCEMapper {

    @Override
    public TCE toDiscenteCreateDocumento(TCECreationDTO tceCreationDTO) {
        return TCE.builder().cnpjEmpresa(tceCreationDTO.getCnpjEmpresa())
                .horarioInicio(tceCreationDTO.getHorarioInicio())
                .horarioFim(tceCreationDTO.getHorarioFim())
                .totalHoras(tceCreationDTO.getTotalHoras())
                .intervalo(tceCreationDTO.getIntervalo())
                .dataInicio(tceCreationDTO.getDataInicio())
                .dataFim(tceCreationDTO.getDataFim())
                .bolsa(tceCreationDTO.getBolsa())
                .auxTransporte(tceCreationDTO.getAuxTransporte())
                .codApolice(tceCreationDTO.getCodApolice())
                .nomeSeguradora(tceCreationDTO.getNomeSeguradora())
                .build();
    }
}
