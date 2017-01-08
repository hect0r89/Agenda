package master.android.agenda;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button btnEditar;
    private Button btnEliminar;

    private OnEditContacListener mListener;
    private OnDeleteContacListener deleteListener;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        nombre = (TextView) v.findViewById(R.id.txtNombreApellidos);
        telefono = (TextView) v.findViewById(R.id.txtTelefono);
        tipo = (TextView) v.findViewById(R.id.txtTipo);
        email = (TextView) v.findViewById(R.id.txtEmail);
        direccion = (TextView) v.findViewById(R.id.txtDireccion);
        layout = (LinearLayout) v.findViewById(R.id.layoutDetalle);
        btnEditar = (Button) v.findViewById(R.id.btnEditar);
        btnEliminar = (Button) v.findViewById(R.id.btnEliminar);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onEditContact(contacto);
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        contacto = getArguments().getParcelable("contacto");

        nombre.setText(contacto.getApellidos().isEmpty() ? contacto.getNombre() : contacto.getNombre() + " " + contacto.getApellidos());
        telefono.setText(contacto.getTelefono().getNumero());
        tipo.setText(contacto.getTelefono().getTipo());
        email.setText(contacto.getCorreo());
        direccion.setText(contacto.getDireccion());
        layout.setBackgroundColor(contacto.getColor());

        return v;
    }





    private void deleteContact(Contacto contacto) {
        DAOContentProvider dao = new DAOContentProvider(getActivity());
        if (dao.deleteContact(contacto) != 0) {
            if (deleteListener != null) {
                deleteListener.onDeleteContact(contacto);
            }

        } else {
            new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage("Error al eliminar el contacto").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditContacListener) {
            mListener = (OnEditContacListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEditContacListener");
        }
        if (context instanceof OnDeleteContacListener) {
            deleteListener = (OnDeleteContacListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDeleteContacListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        deleteListener = null;
    }

    public void updateEditContact(Contacto c) {
        initializeData(c);
    }

    public interface OnEditContacListener {
        void onEditContact(Contacto c);
    }

    public interface OnDeleteContacListener {
        void onDeleteContact(Contacto c);
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
