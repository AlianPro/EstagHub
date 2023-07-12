package br.com.estaghub.domain.embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.persistence.Embeddable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TermoAditivo {
    @NotBlank
    private String dataAntiga;
    @NotBlank
    private String dataNova;
    @NotBlank
    private String nomeSeguradora;
    @NotBlank
    private String codApolice;
}
