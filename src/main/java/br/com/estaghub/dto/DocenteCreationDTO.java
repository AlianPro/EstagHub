package br.com.estaghub.dto;

import br.com.estaghub.domain.Departamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocenteCreationDTO {
    @NotBlank
    private String nome;
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
    @NotBlank
    private String siape;
    @NotBlank
    private Boolean isDocenteComissao;
    @NotBlank
    private Departamento departamento;

}
