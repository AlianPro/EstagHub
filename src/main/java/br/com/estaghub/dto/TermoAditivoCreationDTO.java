package br.com.estaghub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermoAditivoCreationDTO {
    @NotBlank
    private String dataAntiga;
    @NotBlank
    private String dataNova;
    @NotBlank
    private String nomeSeguradora;
    @NotBlank
    private String codApolice;

}
