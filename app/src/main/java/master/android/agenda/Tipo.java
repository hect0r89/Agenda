package master.android.agenda;

/**
 * Created by hector on 24/10/16.
 */

public enum Tipo {
    CASA("Casa"), TRABAJO("Trabajo"), MOVIL("Móvil");

    private final String tipo;

    Tipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo(){
        return tipo;
    }

}
