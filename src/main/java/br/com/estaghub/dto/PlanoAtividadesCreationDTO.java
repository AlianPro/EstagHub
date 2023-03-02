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
public class PlanoAtividadesCreationDTO {
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
}
