package br.com.estaghub.dto;

import br.com.estaghub.domain.Departamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CursoCreationDTO {
    @NotBlank
    private String nome;

    @NotBlank
    private Departamento departamento;

}
