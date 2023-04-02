package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.PlanoAtividades;
import br.com.estaghub.dto.PlanoAtividadesCreationDTO;
import br.com.estaghub.mapper.PlanoAtividadesMapper;

import java.util.List;
import java.util.Optional;

public class PlanoAtividadesMapperImpl implements PlanoAtividadesMapper {

    @Override
    public PlanoAtividades toDiscenteCreateDocumento(PlanoAtividadesCreationDTO PlanoAtividadesCreationDTO, List<Optional<String>> atividades) {
        return PlanoAtividades.builder().nomeEmpresa(PlanoAtividadesCreationDTO.getNomeEmpresa())
                .responsavelEmpresa(PlanoAtividadesCreationDTO.getResponsavelEmpresa())
                .enderecoEmpresa(PlanoAtividadesCreationDTO.getEnderecoEmpresa())
                .telefoneEmpresa(PlanoAtividadesCreationDTO.getTelefoneEmpresa())
                .emailEmpresa(PlanoAtividadesCreationDTO.getEmailEmpresa())
                .nomeSupervisor(PlanoAtividadesCreationDTO.getNomeSupervisor())
                .formacaoSupervisor(PlanoAtividadesCreationDTO.getFormacaoSupervisor())
                .primeiraAtividade(atividades.get(0).isPresent()? atividades.get(0).get() : "")
                .segundaAtividade(atividades.get(1).isPresent()? atividades.get(1).get() : "")
                .terceiraAtividade(atividades.get(2).isPresent()? atividades.get(2).get() : "")
                .quartaAtividade(atividades.get(3).isPresent()? atividades.get(3).get() : "")
                .quintaAtividade(atividades.get(4).isPresent()? atividades.get(4).get() : "")
                .build();
    }
}
