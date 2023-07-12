package br.com.estaghub.mapper.impl;

import br.com.estaghub.domain.Discente;
import br.com.estaghub.dto.DiscenteCreationDTO;
import br.com.estaghub.dto.DiscenteNovoPedidoCreationDTO;
import br.com.estaghub.dto.DiscenteRenovacaoPedidoCreationDTO;
import br.com.estaghub.mapper.DiscenteMapper;

import java.time.LocalDateTime;

public class DiscenteMapperImpl implements DiscenteMapper {
    @Override
    public Discente toDiscenteCreateAccount(DiscenteCreationDTO discenteCreationDTO) {
        return Discente.builder().nome(discenteCreationDTO.getNome())
                .email(discenteCreationDTO.getEmail())
                .senha(discenteCreationDTO.getSenha())
                .matricula(discenteCreationDTO.getMatricula())
                .telefone(discenteCreationDTO.getTelefone())
                .dataHoraCriacao(LocalDateTime.now())
                .build();
    }

    @Override
    public Discente toDiscenteCreateNovoEstagio(DiscenteNovoPedidoCreationDTO discenteNovoPedidoCreationDTO) {
        return Discente.builder().cpf(discenteNovoPedidoCreationDTO.getCpf())
                .rg(discenteNovoPedidoCreationDTO.getRg())
                .orgaoExpedidorRg(discenteNovoPedidoCreationDTO.getOrgaoExpedidorRg())
                .ira(discenteNovoPedidoCreationDTO.getIra())
                .periodo(discenteNovoPedidoCreationDTO.getPeriodo())
                .cargaHorariaCumprida(discenteNovoPedidoCreationDTO.getCargaHorariaCumprida())
                .endereco(discenteNovoPedidoCreationDTO.getEndereco())
                .curso(discenteNovoPedidoCreationDTO.getCurso())
                .build();
    }

    @Override
    public Discente toDiscenteCreateRenovacaoEstagio(DiscenteRenovacaoPedidoCreationDTO discenteRenovacaoPedidoCreationDTO) {
        return Discente.builder().cpf(discenteRenovacaoPedidoCreationDTO.getCpf())
                .rg(discenteRenovacaoPedidoCreationDTO.getRg())
                .orgaoExpedidorRg(discenteRenovacaoPedidoCreationDTO.getOrgaoExpedidorRg())
                .ira(discenteRenovacaoPedidoCreationDTO.getIra())
                .periodo(discenteRenovacaoPedidoCreationDTO.getPeriodo())
                .cargaHorariaCumprida(discenteRenovacaoPedidoCreationDTO.getCargaHorariaCumprida())
                .endereco(discenteRenovacaoPedidoCreationDTO.getEndereco())
                .curso(discenteRenovacaoPedidoCreationDTO.getCurso())
                .build();
    }
}
