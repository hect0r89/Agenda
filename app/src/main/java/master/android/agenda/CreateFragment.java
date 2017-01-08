package master.android.agenda;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import static master.android.agenda.Utils.validateContacto;


public class CreateFragment extends Fragment {

    private EditText editTextNombre;
    private EditText editTextApellidos;
    private EditText editTextTelefono;
    private EditText editTextCorreo;
    private EditText editTextDireccion;
    private Spinner spinnerTipo;
    private Button btnCrear;
    private Button btnCancelar;

    private OnOkCreateContactListener mListener;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_create, container, false);
        spinnerTipo = (Spinner) v.findViewById(R.id.spinnerTipo);
        editTextNombre = (EditText) v.findViewById(R.id.etNombre);
        editTextApellidos = (EditText) v.findViewById(R.id.etApellidos);
        editTextTelefono = (EditText) v.findViewById(R.id.etTelefono);
        editTextCorreo = (EditText) v.findViewById(R.id.etCorreo);
        editTextDireccion = (EditText) v.findViewById(R.id.etDireccion);

        btnCrear = (Button) v.findViewById(R.id.btnEditar);
        btnCancelar = (Button) v.findViewById(R.id.btnCancelar);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contacto contacto = new Contacto(editTextNombre.getText().toString(), editTextApellidos.getText().toString(), new Telefono(editTextTelefono.getText().toString(), (Tipo) spinnerTipo.getSelectedItem()), editTextCorreo.getText().toString(), editTextDireccion.getText().toString(), Utils.getMatColor("500", getContext()));
                String errors = validateContacto(contacto);
                if(errors.isEmpty()){
                    DAOContentProvider dao = new DAOContentProvider(getContext());
                    ArrayList<Long> ids = dao.insertContact(contacto);
                    contacto.setId(ids.get(0));
                    contacto.getTelefono().setId(ids.get(1));
                    if (mListener != null) {
                        mListener.onOkCreateContact(contacto);
                    }

                }else{
                    new AlertDialog.Builder(getContext()).setTitle("Error").setMessage(errors).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ArrayAdapter<Tipo> adapter = new ArrayAdapter<Tipo>(getContext(), android.R.layout.simple_spinner_item, Tipo.values());
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

        spinnerTipo.setAdapter(adapter);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOkCreateContactListener) {
            mListener = (OnOkCreateContactListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOkCreateContactListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnOkCreateContactListener {
        // TODO: Update argument type and name
        void onOkCreateContact(Contacto c);
    }
}
