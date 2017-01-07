package master.android.agenda;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DetailFragment extends Fragment {


    private static final int EDIT_CONTACT = 2;
    private TextView nombre;
    private TextView telefono;
    private TextView tipo;
    private TextView email;
    private TextView direccion;
    private Contacto contacto;
    private LinearLayout layout;


    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(Contacto c) {
        DetailFragment f = new DetailFragment();

        // Supply index input as an argument.
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        nombre = (TextView) v.findViewById(R.id.txtNombreApellidos);
        telefono = (TextView) v.findViewById(R.id.txtTelefono);
        tipo = (TextView) v.findViewById(R.id.txtTipo);
        email = (TextView) v.findViewById(R.id.txtEmail);
        direccion = (TextView) v.findViewById(R.id.txtDireccion);
        layout = (LinearLayout) v.findViewById(R.id.layoutDetalle);

        Contacto contacto = getArguments().getParcelable("contacto");

        nombre.setText(contacto.getApellidos().isEmpty() ? contacto.getNombre() : contacto.getNombre() + " " + contacto.getApellidos());
        telefono.setText(contacto.getTelefono().getNumero());
        tipo.setText(contacto.getTelefono().getTipo());
        email.setText(contacto.getCorreo());
        direccion.setText(contacto.getDireccion());
        layout.setBackgroundColor(contacto.getColor());

        return v;
    }

    public void mostrarDetalle(Contacto c) {
        initializeData(c);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(getActivity(), EditActivity.class);
                i.putExtra("contacto", contacto);
                startActivityForResult(i, EDIT_CONTACT);
                return true;
            case R.id.action_delete:
                AlertDialog alertbox = new AlertDialog.Builder(getActivity())
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
        DAOContentProvider dao = new DAOContentProvider(getActivity());
        if (dao.deleteContact(contacto) != 0) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("contacto", contacto);
//            setResult(Activity.RESULT_OK, returnIntent);
//            finish();

        } else {
            new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage("Error al eliminar el contacto").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();
        }

    }




    public void initializeData(Contacto contacto) {
        nombre.setText(contacto.getApellidos().isEmpty() ? contacto.getNombre() : contacto.getNombre() + " " + contacto.getApellidos());
        telefono.setText(contacto.getTelefono().getNumero());
        tipo.setText(contacto.getTelefono().getTipo());
        email.setText(contacto.getCorreo());
        direccion.setText(contacto.getDireccion());
        layout.setBackgroundColor(contacto.getColor());
    }

}
