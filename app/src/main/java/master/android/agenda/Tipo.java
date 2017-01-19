package master.android.agenda;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hector on 24/10/16.
 */

public enum Tipo implements Parcelable {
    @SerializedName("0")
    CASA("Casa"),
    @SerializedName("1")
    TRABAJO("Trabajo"),
    @SerializedName("2")
    MOVIL("Móvil");

    private final String tipo;

    Tipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tipo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tipo> CREATOR = new Creator<Tipo>() {
        @Override
        public Tipo createFromParcel(Parcel in) {
            return get(in.readString());
        }

        @Override
        public Tipo[] newArray(int size) {
            return new Tipo[size];
        }
    };

    public String getTipo(){
        return tipo;
    }

    public static Tipo get(String value){
        Tipo tipo = CASA;
        switch (value){
            case "Casa":
                tipo = Tipo.valueOf("CASA");
                break;
            case "Trabajo":
                tipo= Tipo.valueOf("TRABAJO");
                break;
            case "Móvil":
                tipo = Tipo.valueOf("MOVIL");
                break;
        }
         return tipo;
    }

}
