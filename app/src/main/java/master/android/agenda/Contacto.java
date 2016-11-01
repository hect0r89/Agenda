package master.android.agenda;

/**
 * Created by hector on 24/10/16.
 */

public class Contacto {
    private String nombre;
    private String apellidos;
    private Telefono num_tlf;
    private String correo;
    private String direccion;

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
}
