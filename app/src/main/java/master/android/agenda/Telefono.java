package master.android.agenda;

/**
 * Created by hector on 24/10/16.
 */


public class Telefono {
    private String numero;
    private Tipo tipo;

    public Telefono(String numero, Tipo tipo) {
        this.numero = numero;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
}
