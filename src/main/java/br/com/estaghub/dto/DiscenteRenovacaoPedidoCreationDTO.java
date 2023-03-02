package br.com.estaghub.dto;

import br.com.estaghub.domain.Curso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscenteRenovacaoPedidoCreationDTO {
    @NotBlank
    private String cpf;
    @NotBlank
    private String rg;
    @NotBlank
    private String orgaoExpedidorRg;
    @NotBlank
    private String ira;
    @NotBlank
    private String periodo;
    @NotBlank
    private String cargaHorariaCumprida;
    @NotBlank
    private String endereco;
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
    private Curso curso;
}
