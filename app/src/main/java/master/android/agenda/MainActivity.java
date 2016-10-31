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
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recView;
    private ArrayList<Contacto> datos;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = new ArrayList<>();
        for(int i=0; i<50; i++)
            datos.add(new Contacto("Nombre"+i, new Telefono("69120317"+i,Tipo.CASA)));

//        Gson gson = new Gson();
//        String json = gson.toJson(new Contacto("Héctor", new Telefono("69120317",Tipo.CASA)));

        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        final ContactoAdapter adaptador = new ContactoAdapter(datos, getApplicationContext());

        recView.setAdapter(adaptador);
        recView.setAdapter(adaptador);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent i = new Intent(this, CreateActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

            startActivity(i);
            }
        });

        recView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));


    }


}
