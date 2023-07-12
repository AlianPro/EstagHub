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
public class PlanoAtividades {
    @NotBlank
    private String nomeEmpresa;
    @NotBlank
    private String responsavelEmpresa;
    @NotBlank
    private String enderecoEmpresa;
    @NotBlank
    private String telefoneEmpresa;
    @NotBlank
    private String emailEmpresa;
    @NotBlank
    private String nomeSupervisor;
    @NotBlank
    private String formacaoSupervisor;
    @NotBlank
    private String primeiraAtividade;
    @NotBlank
    private String segundaAtividade;
    @NotBlank
    private String terceiraAtividade;
    @NotBlank
    private String quartaAtividade;
    @NotBlank
    private String quintaAtividade;
}
