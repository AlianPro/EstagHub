package br.com.estaghub.mapper;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.dto.DiscenteCreationDTO;
import br.com.estaghub.dto.DiscenteNovoPedidoCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DiscenteMapper {
    DiscenteMapper INSTANCE = Mappers.getMapper(DiscenteMapper.class);
    Discente toDiscenteCreateAccount(DiscenteCreationDTO discenteCreationDTO);
    Discente toDiscenteCreateNovoEstagio(DiscenteNovoPedidoCreationDTO discenteNovoPedidoCreationDTO);
}
