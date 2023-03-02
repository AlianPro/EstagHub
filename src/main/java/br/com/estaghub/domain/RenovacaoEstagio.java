package br.com.estaghub.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RenovacaoEstagio {
    private String nomeEmpresa;
    private String enderecoEmpresa;
    private String resumoAtividades;
    private String tempoEstagio;
    private String contribuicaoEstagio;
    private String avaliacaoSupervisor;

}
