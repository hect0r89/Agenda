package master.android.agenda;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hector on 17/11/16.
 */

public final class ContactContract {
    private ContactContract() {}

    /* Inner class that defines the table contents */
    public static class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "Contactos";
        public static final String COLUMN_NAME_FIRST_NAME = "nombre";
        public static final String COLUMN_NAME_LAST_NAME = "apellidos";
        public static final String COLUMN_NAME_PHONE = "telefono";
        public static final String COLUMN_NAME_EMAIL = "correo";
        public static final String COLUMN_NAME_ADDRESS = "direccion";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final	String AUTHORITY ="com.master.agendacontentprovider";
        public static final String contactUri =
                "content://"+AUTHORITY+"/"+"contactos";


    }
}

