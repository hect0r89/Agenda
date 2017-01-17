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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.FileOutputStream;

import static master.android.agenda.Utils.validateContacto;

public class EditActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextApellidos;
    private EditText editTextTelefono;
    private EditText editTextCorreo;
    private EditText editTextDireccion;
    private Spinner spinnerTipo;
    private Contacto contacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        spinnerTipo = (Spinner) findViewById(R.id.spinnerEditTipo);
        editTextNombre = (EditText) findViewById(R.id.etEditNombre);
        editTextApellidos = (EditText) findViewById(R.id.etEditApellidos);
        editTextTelefono = (EditText) findViewById(R.id.etEditTelefono);
        editTextCorreo = (EditText) findViewById(R.id.etEditCorreo);
        editTextDireccion = (EditText) findViewById(R.id.etEditDireccion);

        Intent intent = getIntent();
        contacto = intent.getParcelableExtra("contacto");

        ArrayAdapter<Tipo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Tipo.values());
// Specify the layout to use when the list of choices appears

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

        spinnerTipo.setAdapter(adapter);
        spinnerTipo.setSelection(Tipo.get(contacto.getTelefono().getTipo()).ordinal());

        editTextNombre.setText(contacto.getNombre());
        editTextApellidos.setText(contacto.getApellidos());
        editTextTelefono.setText(contacto.getTelefono().getNumero());
        editTextCorreo.setText(contacto.getCorreo());
        editTextDireccion.setText(contacto.getDireccion());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                contacto.setNombre(editTextNombre.getText().toString());
                contacto.setApellidos(editTextApellidos.getText().toString());
                contacto.setTelefono(new Telefono(editTextTelefono.getText().toString(), (Tipo) spinnerTipo.getSelectedItem(),contacto.getId()));
                contacto.setCorreo(editTextCorreo.getText().toString());
                contacto.setDireccion(editTextDireccion.getText().toString());
                String errors = validateContacto(contacto);
                if(errors.isEmpty()){
                    DAOContentProvider dao = new DAOContentProvider(getApplicationContext());
                    dao.updateContact(contacto);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("contacto", contacto);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else{
                    new AlertDialog.Builder(this).setTitle("Error").setMessage(errors).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
                }


                return true;
            case R.id.action_cancel:
                finish();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
