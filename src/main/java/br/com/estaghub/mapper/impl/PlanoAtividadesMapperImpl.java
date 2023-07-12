package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.embeddable.PlanoAtividades;
import br.com.estaghub.dto.PlanoAtividadesCreationDTO;
import br.com.estaghub.mapper.PlanoAtividadesMapper;

import java.util.List;
import java.util.Optional;

public class PlanoAtividadesMapperImpl implements PlanoAtividadesMapper {

    @Override
    public PlanoAtividades toDiscenteCreateDocumento(PlanoAtividadesCreationDTO PlanoAtividadesCreationDTO, List<Optional<String>> atividades) {
        PlanoAtividades planoAtividades = PlanoAtividades.builder().nomeEmpresa(PlanoAtividadesCreationDTO.getNomeEmpresa())
                .responsavelEmpresa(PlanoAtividadesCreationDTO.getResponsavelEmpresa())
                .enderecoEmpresa(PlanoAtividadesCreationDTO.getEnderecoEmpresa())
                .telefoneEmpresa(PlanoAtividadesCreationDTO.getTelefoneEmpresa())
                .emailEmpresa(PlanoAtividadesCreationDTO.getEmailEmpresa())
                .nomeSupervisor(PlanoAtividadesCreationDTO.getNomeSupervisor())
                .formacaoSupervisor(PlanoAtividadesCreationDTO.getFormacaoSupervisor())
                .build();
        planoAtividades.setPrimeiraAtividade(atividades.get(0).isPresent()? atividades.get(0).get() : "");
        planoAtividades.setSegundaAtividade(atividades.get(1).isPresent()? atividades.get(1).get() : "");
        for (int i = 2; i < atividades.size(); i++) {
            if (i==2){
                planoAtividades.setTerceiraAtividade(atividades.get(i).isPresent()? atividades.get(i).get() : "");
            }else if(i==3){
                planoAtividades.setQuartaAtividade(atividades.get(i).isPresent()? atividades.get(i).get() : "");
            }else if(i==4){
                planoAtividades.setQuintaAtividade(atividades.get(i).isPresent()? atividades.get(i).get() : "");
            }
        }
        return planoAtividades;
    }
}
