package master.android.agenda;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DetailActivity extends AppCompatActivity {

    private static final int EDIT_CONTACT = 2;
    private TextView nombre;
    private TextView telefono;
    private TextView tipo;
    private TextView email;
    private TextView direccion;
    private Contacto contacto;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nombre = (TextView) findViewById(R.id.txtNombreApellidos);
        telefono = (TextView) findViewById(R.id.txtTelefono);
        tipo = (TextView) findViewById(R.id.txtTipo);
        email = (TextView) findViewById(R.id.txtEmail);
        direccion = (TextView) findViewById(R.id.txtDireccion);
        layout = (LinearLayout) findViewById(R.id.layoutDetalle);



    }


}
