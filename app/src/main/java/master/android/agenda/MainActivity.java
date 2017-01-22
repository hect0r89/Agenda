package master.android.agenda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static master.android.agenda.Utils.getMatColor;
import static master.android.agenda.Utils.validateContacto;

public class MainActivity extends AppCompatActivity {

    static final int CREATE_CONTACT = 1;
    private static final int EDIT_CONTACT = 2;
    private static final int DETAIL_CONTACT = 3;
    private RecyclerView recView;
    private ArrayList<Contacto> datos;
    private ContactoAdapter adaptador;
    private DAOContentProvider dao;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ServiceRetrofit contactService = ServiceRetrofit.retrofit.create(ServiceRetrofit.class);
        datos = new ArrayList<>();
        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        adaptador = new ContactoAdapter(orderData(datos), MainActivity.this);

        recView.setAdapter(adaptador);
        recView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        dialog = ProgressDialog.show(MainActivity.this, "", "Cargando...");

        Call<ArrayList<Contacto>> call = contactService.getContacts();
        call.enqueue(new Callback<ArrayList<Contacto>>() {


            @Override
            public void onResponse(Call<ArrayList<Contacto>> call, Response<ArrayList<Contacto>> response) {
                datos.clear();
                datos.addAll(response.body());
                orderData(datos);
                adaptador.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<Contacto>> call, Throwable t) {
                System.out.print("sd");
                dialog.dismiss();

            }

        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent i = new Intent(this, CreateActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                startActivityForResult(i, CREATE_CONTACT);
            }
        });


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
                file = new File(myDir.getAbsolutePath() + "/contacts.json");
                if (file.exists()) {
                    input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String line;
                    buffer = new StringBuffer();

                    while ((line = input.readLine()) != null) {
                        buffer.append(line);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.exists()) {
                Contacto[] contactos = gson.fromJson(buffer.toString(), Contacto[].class);
                datos.clear();
                saveData(new ArrayList<>(Arrays.asList(contactos)));
                datos.addAll(dao.getAllContacts());
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
        } else {
            new AlertDialog.Builder(this).setTitle("Error").setMessage("No se han encontrado contactos para importar").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();
        }

    }

    private ArrayList<Contacto> saveData(ArrayList<Contacto> datosNuevos) {
        ProgressDialog dialog;
        dialog = ProgressDialog.show(MainActivity.this, "", "Cargando...");
        for (final Contacto contacto : datosNuevos) {
            final Contacto contact = contacto;
            String errors = validateContacto(contacto);
            if (errors.isEmpty()) {


                final ServiceRetrofit contactService = ServiceRetrofit.retrofit.create(ServiceRetrofit.class);
                final Call<Contacto> call = contactService.postContact(contacto);
                call.enqueue(new Callback<Contacto>() {

                    @Override
                    public void onResponse(Call<Contacto> call, Response<Contacto> response) {
                        contact.setId(response.body().getId());

                    }

                    @Override
                    public void onFailure(Call<Contacto> call, Throwable t) {

                    }
                });


            } else {
                new AlertDialog.Builder(this).setTitle("Error").setMessage(errors).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
            }
        }
        dialog.dismiss();
        datos.addAll(datosNuevos);
        orderData(datos);
        adaptador.notifyDataSetChanged();
        return datosNuevos;
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
                    if (c.getId() == (contacto != null ? contacto.getId() : -1)) {
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
                    if (c.getId() == (contacto != null ? contacto.getId() : -1)) {
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
                    if (c.getId() == (contacto != null ? contacto.getId() : -1)) {
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
        ArrayList<Contacto> dataGenerated = new ArrayList<>();
        String[] nombres = {"Luis", "María", "Juan", "Norberto", "Laura", "Pelayo", "Jose", "Juana", "Julia", "Irene", "Julia", "Yasen", "Roman", "Alan", "Ivan", "Javi", "Alberto", "Cristina", "Miguel", "David", "Liang", "Omar", "Nacho", "Manuel", "Alejandro", "Daniel", "Jorge"};
        String[] apellidos = {"De Diego", "Martín", "Antón", "Clavo", "Alonso", "Díaz", "Crespo", "Fernandez", "Torres", "De Murcia", "Rodriguez", "Gomez", "Amo", "Sousa", "Ibarra", "De Andres", "Diaz", "Roldan", "Del Mar", "Perez", "Shu", "Rios", "Palacios", "Casariego", "Nicolas", "Hernandez", "Valle"};
        for (int i = 0; i < 30; i++) {
            String nombre = nombres[(int) (Math.random() * (26 - 0 + 1) + 0)];
            String apellido = apellidos[(int) (Math.random() * (26 - 0 + 1) + 0)];
            dataGenerated.add(new Contacto(nombre, apellido, new Telefono(generaTelefonos(), Tipo.MOVIL), "", "", getMatColor("500", this)));
        }
        saveData(dataGenerated);

    }

    private String generaTelefonos() {
        return String.valueOf((int) (Math.random() * (699999999 - 600000000 + 1) + 600000000));
    }

    @Override
    protected void onResume() {
        if (datos != null) {
            orderData(datos);
            adaptador.notifyDataSetChanged();
        }
        super.onResume();
    }

    private class getContacts extends AsyncTask<ArrayList<List>, Void, Void> {


        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected Void doInBackground(ArrayList<List>... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.dismiss();
        }
    }
}
