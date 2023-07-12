package br.com.estaghub.enums;

public enum TipoDocumento {
    TCE (1), TCE_ASSINADO_DISCENTE (2), TCE_ASSINADO_DOCENTE (3), PLANO_ATIVIDADES (4), PLANO_ATIVIDADES_ASSINADO_DISCENTE (5), PLANO_ATIVIDADES_ASSINADO_DOCENTE (6), TERMO_ADITIVO (7), TERMO_ADITIVO_ASSINADO_DISCENTE (8), HISTORICO_ACADEMICO (9), GRADE_HORARIO (10);
    private final int tipo;

    TipoDocumento(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

}
