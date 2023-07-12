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
public class DepartamentoCreationDTO {
    @NotBlank
    private String nome;
    @NotBlank
    private String sigla;

}
