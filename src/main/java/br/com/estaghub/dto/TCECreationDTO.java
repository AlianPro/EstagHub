package br.com.estaghub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TCECreationDTO {
    @NotBlank
    private String cnpjEmpresa;
    @NotBlank
    private String horarioInicio;
    @NotBlank
    private String horarioFim;
    @NotBlank
    private String totalHoras;
    @NotBlank
    private String intervalo;
    @NotBlank
    private String dataInicio;
    @NotBlank
    private String dataFim;
    @NotBlank
    private String bolsa;
    @NotBlank
    private String auxTransporte;
    @NotBlank
    private String codApolice;
    @NotBlank
    private String nomeSeguradora;

}
