package br.com.estaghub.mapper;

import br.com.estaghub.domain.embeddable.PlanoAtividades;
import br.com.estaghub.dto.PlanoAtividadesCreationDTO;

import java.util.List;
import java.util.Optional;

public interface PlanoAtividadesMapper {
    PlanoAtividades toDiscenteCreateDocumento(PlanoAtividadesCreationDTO PlanoAtividadesCreationDTO, List<Optional<String>> atividades);
}
