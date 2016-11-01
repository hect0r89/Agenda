package master.android.agenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CreateActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextApellidos;
    private EditText editTextTelefono;
    private EditText editTextCorreo;
    private EditText editTextDireccion;
    private Spinner spinnerTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        editTextNombre = (EditText) findViewById(R.id.etNombre);
        editTextApellidos = (EditText) findViewById(R.id.etApellidos);
        editTextTelefono = (EditText) findViewById(R.id.etTelefono);
        editTextCorreo = (EditText) findViewById(R.id.etCorreo);
        editTextDireccion = (EditText) findViewById(R.id.etDireccion);

        ArrayAdapter<Tipo> adapter = new ArrayAdapter<Tipo>(this, android.R.layout.simple_spinner_item, Tipo.values());
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

        spinnerTipo.setAdapter(adapter);


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
                Contacto contacto = new Contacto(editTextNombre.getText().toString(), editTextApellidos.getText().toString(), new Telefono(editTextTelefono.getText().toString(), (Tipo) spinnerTipo.getSelectedItem()), editTextCorreo.getText().toString(), editTextDireccion.getText().toString());
                Gson gson = new Gson();
                String json = gson.toJson(contacto);
                String filename = java.util.UUID.randomUUID().toString()+".json";
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(json.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("filename",filename);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
