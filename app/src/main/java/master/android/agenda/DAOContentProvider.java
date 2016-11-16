package master.android.agenda;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by hector on 16/11/16.
 */

public class DAOContentProvider {
    public static final String COLUMN_NAME_FIRST_NAME = "nombre";
    public static final String COLUMN_NAME_LAST_NAME = "apellidos";
    public static final String COLUMN_NAME_EMAIL = "correo";
    public static final String COLUMN_NAME_ADDRESS = "direccion";
    public static final String COLUMN_NAME_COLOR = "color";
    public static final String _ID = "_id";
    public static final	String AUTHORITY ="com.master.agendacontentprovider";
    private static final String uri =
            "content://"+AUTHORITY+"/"+"Contactos";

    public static final Uri CONTENT_URI = Uri.parse(uri);
    private final Context context;


    public DAOContentProvider(Context context) {
        this.context = context;
    }

    public ArrayList<Contacto> getAllContacts(){
        ArrayList<Contacto> data = new ArrayList<>();
        String[] projection = new String[]{
                _ID,
                COLUMN_NAME_FIRST_NAME,
                COLUMN_NAME_LAST_NAME,
                COLUMN_NAME_EMAIL,
                COLUMN_NAME_ADDRESS,
                COLUMN_NAME_COLOR};

        Uri contactosUri =  CONTENT_URI;

        ContentResolver cr = context.getContentResolver();

//Hacemos la consulta
        Cursor cur = cr.query(contactosUri,
                projection, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados

        if (cur.moveToFirst())
        {
            String nombre;
            String apellidos;
            String direccion;
            int color;
            String email;
            String id;

            int colNombre = cur.getColumnIndex(COLUMN_NAME_FIRST_NAME);
            int colApellido = cur.getColumnIndex(COLUMN_NAME_LAST_NAME);
            int colEmail = cur.getColumnIndex(COLUMN_NAME_EMAIL);
            int colDireccion = cur.getColumnIndex(COLUMN_NAME_ADDRESS);
            int colColor = cur.getColumnIndex(COLUMN_NAME_COLOR);
            int colId = cur.getColumnIndex(_ID);




            do
            {
                nombre = cur.getString(colNombre);
                apellidos = cur.getString(colApellido);
                email = cur.getString(colEmail);
                direccion = cur.getString(colDireccion);
                color = cur.getInt(colColor);
                id = cur.getString(colId);
                data.add(new Contacto(nombre, apellidos, new Telefono("666666666", Tipo.MOVIL), email, direccion, id, color));

            } while (cur.moveToNext());
        }
        return data;
    }
}
