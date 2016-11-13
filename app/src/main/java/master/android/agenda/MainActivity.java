package master.android.agenda;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static master.android.agenda.Utils.getMatColor;
import static master.android.agenda.Utils.validateContacto;

public class MainActivity extends AppCompatActivity {

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
    static final int CREATE_CONTACT = 1;
    private static final int EDIT_CONTACT = 2;
    private static final int DETAIL_CONTACT = 3;
    private RecyclerView recView;
    private ArrayList<Contacto> datos;
    private ContactoAdapter adaptador;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = readData();


        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        adaptador = new ContactoAdapter(orderData(datos), this);

        recView.setAdapter(adaptador);
        recView.setAdapter(adaptador);

        String[] projection = new String[]{
                _ID,
                COLUMN_NAME_FIRST_NAME,
                COLUMN_NAME_LAST_NAME,
                COLUMN_NAME_EMAIL,
                COLUMN_NAME_ADDRESS,
                COLUMN_NAME_COLOR};

        Uri contactosUri =  CONTENT_URI;

        ContentResolver cr = getContentResolver();

//Hacemos la consulta
        Cursor cur = cr.query(contactosUri,
                projection, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados

        if (cur.moveToFirst())
        {
            String nombre;
            String telefono;
            String email;

            int colNombre = cur.getColumnIndex(COLUMN_NAME_FIRST_NAME);
            int colApellido = cur.getColumnIndex(COLUMN_NAME_LAST_NAME);
            int colEmail = cur.getColumnIndex(COLUMN_NAME_EMAIL);
            int colDireccion = cur.getColumnIndex(COLUMN_NAME_ADDRESS);
            int colColor = cur.getColumnIndex(COLUMN_NAME_COLOR);




            do
            {
                nombre = cur.getString(colNombre);




            } while (cur.moveToNext());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent i = new Intent(this, CreateActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                startActivityForResult(i, CREATE_CONTACT);
            }
        });

        recView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }

    private ArrayList<Contacto> orderData(ArrayList<Contacto> datos) {
        Collections.sort(datos, new Comparator<Contacto>() {
            public int compare(Contacto v1, Contacto v2) {
                int res = v1.getNombre().compareToIgnoreCase(v2.getNombre());
                if (res != 0)
                    return res;
                return v1.getApellidos().compareToIgnoreCase(v2.getApellidos());
            }
        });
        return datos;
    }

    private ArrayList<Contacto> readData() {
        BufferedReader input = null;
        File file = null;
        Gson gson = new Gson();
        ArrayList<Contacto> data = new ArrayList<>();

        for (File f : getFilesDir().listFiles()) {
            if (f.isFile() && f.getName().endsWith(".json")) {
                String name = f.getName();
                try {
                    file = new File(getFilesDir(), name); // Pass getFilesDir() and "MyFile" to read file

                    input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String line;
                    StringBuffer buffer = new StringBuffer();
                    while ((line = input.readLine()) != null) {
                        buffer.append(line);
                    }

                    data.add(gson.fromJson(buffer.toString(), Contacto.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_export:
                exportToExternal();
                return true;
            case R.id.action_import:
                importFromExternal();
                return true;
            case R.id.action_generate:
                generateData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void importFromExternal() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/contactsAgenda");
        BufferedReader input = null;
        File file = null;
        Gson gson = new Gson();
        StringBuffer buffer = null;

        if (myDir.listFiles() != null) {
            try {
                file = new File(myDir.getAbsolutePath(), "contacts.json");

                input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                buffer = new StringBuffer();

                while ((line = input.readLine()) != null) {
                    buffer.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Contacto[] contactos = gson.fromJson(buffer.toString(), Contacto[].class);
            datos.clear();
            saveData(new ArrayList<>(Arrays.asList(contactos)));
            datos.addAll(readData());
            orderData(datos);
            adaptador.notifyDataSetChanged();

            CoordinatorLayout coord = (CoordinatorLayout) findViewById(R.id.activity_main);
            Snackbar.make(coord, "Contactos importados correctamente", Snackbar.LENGTH_LONG)
                    .show();
        } else {
            new AlertDialog.Builder(this).setTitle("Error").setMessage("No se han encontrado contactos para importar").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();
        }

    }

    private void saveData(ArrayList<Contacto> datos) {
        for (Contacto contacto : datos) {
            String filename = contacto.getUuid();

            String errors = validateContacto(contacto);
            if (errors.isEmpty()) {
                Gson gson = new Gson();
                String json = gson.toJson(contacto);
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(json.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                new AlertDialog.Builder(this).setTitle("Error").setMessage(errors).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
            }
        }

    }

    private void exportToExternal() {
        if (isExternalStorageWritable()) {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/contactsAgenda");
            if (!myDir.mkdirs()) {
                Log.e("CREAR DIRECTORIO", "Directory not created");
            }

            File file = new File(myDir, "contacts.json");
            FileOutputStream outputStream = null;
            Gson gson = new Gson();
            try {
                outputStream = new FileOutputStream(file);
                outputStream.write("[".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int cont = 0;
            for (Contacto c : datos) {

                String json = gson.toJson(c);
                try {
                    if (outputStream != null) {
                        outputStream.write(json.getBytes());
                        if (cont < datos.size() - 1) {
                            outputStream.write(",".getBytes());
                        }

                    }
                    cont++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (outputStream != null) {
                    outputStream.write("]".getBytes());
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            CoordinatorLayout coord = (CoordinatorLayout) findViewById(R.id.activity_main);
            Snackbar.make(coord, "Contactos exportados correctamente", Snackbar.LENGTH_LONG)
                    .show();
        } else {
            new AlertDialog.Builder(this).setTitle("Error").setMessage("Error al exportar contactos").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CREATE_CONTACT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Contacto contacto = data.getExtras().getParcelable("contacto");
                datos.add(contacto);
                orderData(datos);
                adaptador.notifyDataSetChanged();
            }
        } else if (requestCode == EDIT_CONTACT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Contacto contacto = data.getExtras().getParcelable("contacto");
                int index = -1;
                for (Contacto c : datos) {
                    if (c.getUuid().equals(contacto != null ? contacto.getUuid() : null)) {
                        index = datos.indexOf(c);

                    }
                }
                datos.remove(index);
                datos.add(contacto);
                orderData(datos);
                adaptador.notifyDataSetChanged();
            }
        } else if (requestCode == DETAIL_CONTACT) {
            if (resultCode == RESULT_OK) {
                Contacto contacto = data.getExtras().getParcelable("contacto");
                int index = -1;
                for (Contacto c : datos) {
                    if (c.getUuid().equals(contacto != null ? contacto.getUuid() : null)) {
                        index = datos.indexOf(c);
                    }
                }
                datos.remove(index);
                adaptador.notifyDataSetChanged();
                CoordinatorLayout coord = (CoordinatorLayout) findViewById(R.id.activity_main);
                Snackbar.make(coord, "Contacto eliminado correctamente", Snackbar.LENGTH_LONG)
                        .show();
            } else if (resultCode == RESULT_FIRST_USER) {
                Contacto contacto = data.getExtras().getParcelable("contacto");
                int index = -1;
                for (Contacto c : datos) {
                    if (c.getUuid().equals(contacto != null ? contacto.getUuid() : null)) {
                        index = datos.indexOf(c);
                    }
                }
                datos.remove(index);
                datos.add(contacto);
                orderData(datos);
                adaptador.notifyDataSetChanged();
            }
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    private void generateData() {
        datos.clear();
        String[] nombres = {"Luis", "María", "Juan", "Norberto", "Laura", "Pelayo", "Jose", "Juana", "Julia", "Irene", "Julia", "Yasen", "Roman", "Alan", "Ivan", "Javi", "Alberto", "Cristina", "Miguel", "David", "Liang", "Omar", "Nacho", "Manuel", "Alejandro", "Daniel", "Jorge"};
        String[] apellidos = {"De Diego", "Martín", "Antón", "Clavo", "Alonso", "Díaz", "Crespo", "Fernandez", "Torres", "De Murcia", "Rodriguez", "Gomez", "Amo", "Sousa", "Ibarra", "De Andres", "Diaz", "Roldan", "Del Mar", "Amor", "Shu", "Rios", "Palacios", "Casariego", "Nicolas", "Hernandez", "Valle"};
        for (int i = 0; i < 30; i++) {
            String nombre = nombres[(int) (Math.random() * (26 - 0 + 1) + 0)];
            String apellido = apellidos[(int) (Math.random() * (26 - 0 + 1) + 0)];
            datos.add(new Contacto(nombre, apellido, new Telefono(generaTelefonos(), Tipo.MOVIL), "", "", java.util.UUID.randomUUID().toString() + ".json", getMatColor("500", this)));
        }
        saveData(datos);
        orderData(datos);
        adaptador.notifyDataSetChanged();
    }

    private String generaTelefonos() {
        return String.valueOf((int) (Math.random() * (699999999 - 600000000 + 1) + 600000000));
    }

    @Override
    protected void onResume() {
        orderData(datos);
        adaptador.notifyDataSetChanged();
        super.onResume();
    }
}
