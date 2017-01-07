package master.android.agenda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import static master.android.agenda.Utils.validateContacto;


public class EditFragment extends Fragment {
    private EditText editTextNombre;
    private EditText editTextApellidos;
    private EditText editTextTelefono;
    private EditText editTextCorreo;
    private EditText editTextDireccion;
    private Spinner spinnerTipo;
    private Contacto contacto;

    private OnOkEditContactListener mListener;

//    private OnFragmentInteractionListener mListener;

    public EditFragment() {
        // Required empty public constructor
    }


    public static EditFragment newInstance(Contacto c) {
        EditFragment f = new EditFragment();
        Bundle args = new Bundle();
        args.putParcelable("contacto", c);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        spinnerTipo = (Spinner) v.findViewById(R.id.spinnerEditTipo);
        editTextNombre = (EditText) v.findViewById(R.id.etEditNombre);
        editTextApellidos = (EditText) v.findViewById(R.id.etEditApellidos);
        editTextTelefono = (EditText) v.findViewById(R.id.etEditTelefono);
        editTextCorreo = (EditText) v.findViewById(R.id.etEditCorreo);
        editTextDireccion = (EditText) v.findViewById(R.id.etEditDireccion);


        contacto = getArguments().getParcelable("contacto");

        ArrayAdapter<Tipo> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Tipo.values());
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
        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_create, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                contacto.setNombre(editTextNombre.getText().toString());
                contacto.setApellidos(editTextApellidos.getText().toString());
                contacto.setTelefono(new Telefono(editTextTelefono.getText().toString(), (Tipo) spinnerTipo.getSelectedItem(), 1));
                contacto.setCorreo(editTextCorreo.getText().toString());
                contacto.setDireccion(editTextDireccion.getText().toString());
                String errors = validateContacto(contacto);
                if (errors.isEmpty()) {
                    DAOContentProvider dao = new DAOContentProvider(getContext());
                    dao.updateContact(contacto);
                    if (mListener != null) {
                        mListener.onOkEditContact(contacto);
                    }
                    return true;

                } else {
                    new AlertDialog.Builder(getContext()).setTitle("Error").setMessage(errors).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
                }


                return true;
            case R.id.action_cancel:

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public interface OnOkEditContactListener {
        void onOkEditContact(Contacto c);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOkEditContactListener) {
            mListener = (OnOkEditContactListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOkEditContactListener");
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
