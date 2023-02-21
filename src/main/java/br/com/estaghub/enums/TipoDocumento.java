package br.com.estaghub.enums;

public enum TipoDocumento {
    TCE (1), PLANO_ATIVIDADES (2), TERMO_ADITIVO (3), HISTORICO_ACADEMICO (4), GRADE_HORARIO (5), RELATORIO_ATIVIDADES (6), AVALIACAO_DESEMPENHO (7);
    private final int tipo;

    TipoDocumento(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public static TipoDocumento getInstance(int tipo) {
        for (TipoDocumento td : values()){
            if(td.getTipo() == tipo)
                return td;
        }
        return null;
    }

}
