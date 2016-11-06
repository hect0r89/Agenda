package master.android.agenda;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {


    static final int CREATE_CONTACT = 1;
    private static final int EDIT_CONTACT = 2;
    private static final int DETAIL_CONTACT = 3;
    private RecyclerView recView;
    private ArrayList<Contacto> datos;
    private ContactoAdapter adaptador;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = readData();
       

        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        adaptador = new ContactoAdapter(orderData(datos), MainActivity.this);

        recView.setAdapter(adaptador);
        recView.setAdapter(adaptador);

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
                return v1.getNombre().compareToIgnoreCase(v2.getNombre());
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
                Log.i("ActionBar", "Settings!");;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void importFromExternal() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/contactsAgenda");
        if(myDir.listFiles() != null){
            for (File f : myDir.listFiles()) {
                if (f.isFile() && f.getName().endsWith(".json")) {
                    try {
                        copyFile(f,new File(getFilesDir(), f.getName()));
                        datos.clear();
                        datos.addAll(readData());
                        orderData(datos);
                        adaptador.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            new AlertDialog.Builder(this).setTitle("Información").setMessage("Contactos importados correctamente").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();
        }else{
            new AlertDialog.Builder(this).setTitle("Error").setMessage("No se han encontrado contactos para importar").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();
        }

    }

    private void exportToExternal() {
        if(isExternalStorageWritable()){
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/contactsAgenda");
            if(!myDir.mkdirs()){
                Log.e("CREAR DIRECTORIO", "Directory not created");
            }

            for (File f : getFilesDir().listFiles()) {
                if (f.isFile() && f.getName().endsWith(".json")) {
                    try {
                        copyFile(f,new File(myDir, f.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            new AlertDialog.Builder(this).setTitle("Información").setMessage("Contactos exportados correctamente").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();
        }else{
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
        }else if (requestCode == EDIT_CONTACT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Contacto contacto = data.getExtras().getParcelable("contacto");
                int index = -1;
                for(Contacto c : datos){
                    if(c.getUuid().equals(contacto != null ? contacto.getUuid() : null)){
                        index = datos.indexOf(c);

                    }
                }
                datos.remove(index);
                datos.add(contacto);
                orderData(datos);
                adaptador.notifyDataSetChanged();
            }
        }else if (requestCode == DETAIL_CONTACT) {
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
                new AlertDialog.Builder(this).setTitle("Información").setMessage("Contacto eliminado correctamente").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
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

    public static void copyFile(File src, File dst) throws IOException
    {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try
        {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
        finally
        {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }



}
