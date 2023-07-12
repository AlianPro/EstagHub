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
public class SupervisorCreationDTO {
    @NotBlank
    private String nome;
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
    @NotBlank
    private Long numeroPedido;
    @NotBlank
    private String cargo;
    @NotBlank
    private String telefone;
    @NotBlank
    private String nomeEmpresa;
    @NotBlank
    private String cnpjEmpresa;

}
