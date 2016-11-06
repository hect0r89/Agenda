package master.android.agenda;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hector on 24/10/16.
 */


public class Telefono implements Parcelable {
    private String numero;
    private Tipo tipo;

    public Telefono(String numero, Tipo tipo) {
        this.numero = numero;
        this.tipo = tipo;
    }

    private Telefono(Parcel in) {
        tipo = in.readParcelable(Tipo.class.getClassLoader());
        numero = in.readString();


    }

    public static final Parcelable.Creator<Telefono> CREATOR
            = new Parcelable.Creator<Telefono>() {
        public Telefono createFromParcel(Parcel in) {
            return new Telefono(in);
        }

        public Telefono[] newArray(int size) {
            return new Telefono[size];
        }
    };

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

    public String getTipo() {
        return tipo.getTipo();
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(tipo, i);
        parcel.writeString(numero);

    }
}
