package br.com.estaghub.domain.embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.persistence.Embeddable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RenovacaoEstagio {
    @NotBlank
    private String nomeEmpresa;
    @NotBlank
    private String enderecoEmpresa;
    @NotBlank
    private String resumoAtividades;
    @NotBlank
    private String tempoEstagio;
    @NotBlank
    private String contribuicaoEstagio;
    @NotBlank
    private String avaliacaoSupervisor;

}
