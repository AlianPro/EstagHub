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
public class TCE {
    private String cnpjEmpresa;
    private String horarioInicio;
    private String horarioFim;
    private String totalHoras;
    private String intervalo;
    private String dataInicio;
    private String dataFim;
    private String bolsa;
    private String auxTransporte;
    private String codApolice;
    private String nomeSeguradora;
}
