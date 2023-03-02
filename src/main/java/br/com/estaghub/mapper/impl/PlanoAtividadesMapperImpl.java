package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.PlanoAtividades;
import br.com.estaghub.dto.PlanoAtividadesCreationDTO;
import br.com.estaghub.mapper.PlanoAtividadesMapper;

import java.util.List;

public class PlanoAtividadesMapperImpl implements PlanoAtividadesMapper {

    @Override
    public PlanoAtividades toDiscenteCreateDocumento(PlanoAtividadesCreationDTO PlanoAtividadesCreationDTO, List<String> atividades) {
        return PlanoAtividades.builder().nomeEmpresa(PlanoAtividadesCreationDTO.getNomeEmpresa())
                .responsavelEmpresa(PlanoAtividadesCreationDTO.getResponsavelEmpresa())
                .enderecoEmpresa(PlanoAtividadesCreationDTO.getEnderecoEmpresa())
                .telefoneEmpresa(PlanoAtividadesCreationDTO.getTelefoneEmpresa())
                .emailEmpresa(PlanoAtividadesCreationDTO.getEmailEmpresa())
                .nomeSupervisor(PlanoAtividadesCreationDTO.getNomeSupervisor())
                .formacaoSupervisor(PlanoAtividadesCreationDTO.getFormacaoSupervisor())
                .primeiraAtividade(atividades.get(0))
                .segundaAtividade(atividades.get(1))
                .terceiraAtividade(atividades.get(2))
                .quartaAtividade(atividades.get(3))
                .quintaAtividade(atividades.get(4))
                .build();
    }
}
