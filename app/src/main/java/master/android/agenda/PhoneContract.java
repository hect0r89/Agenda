package master.android.agenda;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hector on 17/11/16.
 */


public class PhoneContract {
    private PhoneContract() {
    }

    /* Inner class that defines the table contents */
    public static class PhoneEntry implements BaseColumns {
        public static final String TABLE_NAME = "Telefonos";
        public static final String COLUMN_NAME_NUMBER = "numero";
        public static final String COLUMN_NAME_TYPE = "tipo";
        public static final	String AUTHORITY ="com.master.agendacontentprovider";
        public static final String phoneContactUri =
                "content://" + AUTHORITY + "/" + "telefonos";


    }
}

