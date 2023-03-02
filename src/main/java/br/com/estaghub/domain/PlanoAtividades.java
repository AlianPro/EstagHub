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
public class PlanoAtividades {
    private String nomeEmpresa;
    private String responsavelEmpresa;
    private String enderecoEmpresa;
    private String telefoneEmpresa;
    private String emailEmpresa;
    private String nomeSupervisor;
    private String formacaoSupervisor;
    private String primeiraAtividade;
    private String segundaAtividade;
    private String terceiraAtividade;
    private String quartaAtividade;
    private String quintaAtividade;
}
