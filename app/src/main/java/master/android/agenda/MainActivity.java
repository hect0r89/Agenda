package master.android.agenda;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recView;
    private ArrayList<Contacto> datos;
    private ContactoAdapter adaptador;
    static final int CREATE_CONTACT = 1;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = readData();


        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        adaptador = new ContactoAdapter(datos, getApplicationContext());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CREATE_CONTACT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Contacto c = data.getExtras().getParcelable("contacto");
                Log.d("sd","sad");
//                String filename = data.getStringExtra("filename");
//                BufferedReader input = null;
//                File file = null;
//                Gson gson = new Gson();
//                try {
//                    file = new File(getFilesDir(), filename); // Pass getFilesDir() and "MyFile" to read file
//
//                    input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//                    String line;
//                    StringBuffer buffer = new StringBuffer();
//                    while ((line = input.readLine()) != null) {
//                        buffer.append(line);
//                    }
//
//                    datos.add(gson.fromJson(buffer.toString(), Contacto.class));
//                    adaptador.notifyDataSetChanged();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }


}
