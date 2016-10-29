package master.android.agenda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recView;
    private ArrayList<Contacto> datos;

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

        recView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));


    }
}
