package br.com.estaghub.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TermoAditivo {
    private String dataAntiga;
    private String dataNova;
    private String nomeSeguradora;
    private String codApolice;
}
