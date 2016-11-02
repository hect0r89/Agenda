package master.android.agenda;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hector on 24/10/16.
 */

public class Contacto implements Parcelable{
    private String nombre;
    private String apellidos;
    private Telefono num_tlf;
    private String correo;
    private String direccion;

    private Contacto(Parcel in) {
        nombre = in.readString();
        apellidos = in.readString();
        num_tlf = in.readParcelable(Telefono.class.getClassLoader());
        correo = in.readString();
        direccion = in.readString();
    }

    public static final Parcelable.Creator<Contacto> CREATOR
            = new Parcelable.Creator<Contacto>() {
        public Contacto createFromParcel(Parcel in) {
            return new Contacto(in);
        }

        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };

    public Contacto(String nombre, Telefono num_tlf) {
        this.nombre = nombre;
        this.num_tlf = num_tlf;
    }

    public Contacto(String nombre, String apellidos, Telefono num_tlf, String correo, String direccion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.num_tlf = num_tlf;
        this.correo = correo;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Telefono getNum_tlf() {
        return num_tlf;
    }

    public void setNum_tlf(Telefono num_tlf) {
        this.num_tlf = num_tlf;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeString(apellidos);
        parcel.writeString(correo);
        parcel.writeString(direccion);
        parcel.writeParcelable(num_tlf, i);
    }
}
