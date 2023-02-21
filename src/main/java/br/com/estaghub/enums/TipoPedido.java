package br.com.estaghub.enums;

public enum TipoPedido {
    NOVO(1), RENOVACAO(2);
    private final int tipo;

    TipoPedido(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public static TipoPedido getInstance(int tipo) {
        for (TipoPedido tp : values()){
            if(tp.getTipo() == tipo)
                return tp;
        }
        return null;
    }
}
