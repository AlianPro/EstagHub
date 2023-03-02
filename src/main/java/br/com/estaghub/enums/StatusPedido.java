package br.com.estaghub.enums;

public enum StatusPedido {
    PEDIDO_ENCERRADO(1), NOVO_STEP1(2), NOVO_STEP2(3), NOVO_STEP2_REJEITADO(4), NOVO_STEP2_JUSTIFICADO(5), NOVO_STEP3(6), NOVO_STEP3_DISCENTE_ASSINADO(7), NOVO_STEP4(8), NOVO_STEP4_DOCENTE_ASSINADO(9), NOVO_STEP4_PLANO_ATIVIDADES(10), NOVO_STEP4_TCE(11), NOVO_STEP4_ATIVIDADES_TCE(12), NOVO_PEDIDO_FIM(13), RENOVACAO_STEP1(14), RENOVACAO_STEP2(15), RENOVACAO_STEP3(16), RENOVACAO_STEP3_REJEITADO(17), RENOVACAO_STEP3_JUSTIFICADO(18), RENOVACAO_STEP4(19), RENOVACAO_STEP4_DISCENTE_ASSINADO(20);
    private final int status;

    StatusPedido(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static StatusPedido getInstance(int status) {
        for (StatusPedido sp : values()){
            if(sp.getStatus() == status)
                return sp;
        }
        return null;
    }
}