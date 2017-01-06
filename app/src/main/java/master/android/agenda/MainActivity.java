package master.android.agenda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContactoAdapter.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListFragment frgListado
                =(ListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.FrgListado);


    }

    @Override
    public void onItemClick(Contacto c) {
        boolean hayDetalle =
                (getSupportFragmentManager().findFragmentById(R.id.FrgDetail) != null);
        if(hayDetalle) {
            ((DetailFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.FrgDetail)).mostrarDetalle(c);
        }
    }


}
