package master.android.agenda;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hector on 24/10/16.
 */

public class Contacto implements Parcelable {
    private String nombre;
    private String apellidos;
    private Telefono telefono;
    private String correo;
    private String direccion;
    private String uuid;
    private int color;

    private Contacto(Parcel in) {
        telefono = in.readParcelable(Telefono.class.getClassLoader());
        nombre = in.readString();
        apellidos = in.readString();
        correo = in.readString();
        direccion = in.readString();
        uuid = in.readString();
        color = in.readInt();
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

    public Contacto(String nombre, Telefono telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Contacto(String nombre, String apellidos, Telefono telefono, String correo, String direccion, String uuid, int color) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.uuid = uuid;
        this.color = color;
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

    public Telefono getTelefono() {
        return telefono;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(telefono, i);
        parcel.writeString(nombre);
        parcel.writeString(apellidos);
        parcel.writeString(correo);
        parcel.writeString(direccion);
        parcel.writeString(uuid);
        parcel.writeInt(color);
    }
}
