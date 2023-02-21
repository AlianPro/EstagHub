package br.com.estaghub.enums;

public enum StatusPedido {
    //TODO passar os passos de cada etapa do processo, fazer essa classe enquanto desenvolvo o sistema.
    NOVO_STEP1(1), RENOVACAO(2);
    private final int tipo;

    StatusPedido(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public static StatusPedido getInstance(int tipo) {
        for (StatusPedido tp : values()){
            if(tp.getTipo() == tipo)
                return tp;
        }
        return null;
    }
}
