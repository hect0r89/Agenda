package master.android.agenda;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;

public class DetailActivity extends AppCompatActivity {

    private static final int EDIT_CONTACT = 2;
    private TextView nombre;
    private TextView telefono;
    private TextView tipo;
    private TextView email;
    private TextView direccion;
    private Contacto contacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nombre = (TextView) findViewById(R.id.txtNombreApellidos);
        telefono = (TextView) findViewById(R.id.txtTelefono);
        tipo = (TextView) findViewById(R.id.txtTipo);
        email = (TextView) findViewById(R.id.txtEmail);
        direccion = (TextView) findViewById(R.id.txtDireccion);

        Intent intent = getIntent();
        contacto = intent.getParcelableExtra("contacto");
        initializeData(contacto);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(this, EditActivity.class);
                i.putExtra("contacto", contacto);
                startActivityForResult(i, EDIT_CONTACT);
                return true;
            case R.id.action_delete:
                AlertDialog alertbox = new AlertDialog.Builder(this)
                        .setMessage("¿Está seguro de querer eliminar este contacto?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteContact(contacto);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .show();

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void deleteContact(Contacto contacto) {
        File fdelete = new File(getFilesDir(), contacto.getUuid());
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("contacto", contacto);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            } else {
                new AlertDialog.Builder(this).setTitle("Error").setMessage("Error al eliminar el contacto").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EDIT_CONTACT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                contacto = data.getExtras().getParcelable("contacto");
                initializeData(contacto);
            }

        }
    }

    public void initializeData(Contacto contacto){
        nombre.setText(contacto.getNombre() + " " + contacto.getApellidos());
        telefono.setText(contacto.getTelefono().getNumero());
        tipo.setText(contacto.getTelefono().getTipo());
        email.setText(contacto.getCorreo());
        direccion.setText(contacto.getDireccion());
    }
}
