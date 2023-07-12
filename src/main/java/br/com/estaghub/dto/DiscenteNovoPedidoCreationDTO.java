package br.com.estaghub.dto;

import br.com.estaghub.domain.Curso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscenteNovoPedidoCreationDTO {
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
    private String endereco;
    @NotBlank
    private String cargaHorariaCumprida;
    @NotBlank
    private Curso curso;

}
