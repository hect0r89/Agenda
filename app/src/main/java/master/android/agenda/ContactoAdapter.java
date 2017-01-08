package master.android.agenda;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hector on 24/10/16.
 */

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {



    private ArrayList<Contacto> datos;
    private static Context context;
    private  OnItemClickListener listener;

    public ContactoAdapter(ArrayList<Contacto> contactos, Context context) {
        this.datos = contactos;
        this.context = context;


    }

    public static class ContactoViewHolder
            extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtNumero;
        private TextView txtOval;


        public ContactoViewHolder(View itemView) {
            super(itemView);
            txtOval = (TextView) itemView.findViewById(R.id.txt_oval);
            txtNombre = (TextView) itemView.findViewById(R.id.label_name);
            txtNumero = (TextView) itemView.findViewById(R.id.label_tlf);
        }

        public void bindContacto(Contacto contacto) {
            String initial = contacto.getNombre().substring(0, 1).toUpperCase();
            txtNombre.setText(contacto.getNombre() + " " + contacto.getApellidos());
            txtNumero.setText((contacto.getTelefono() == null || contacto.getTelefono().getNumero().isEmpty()) ? "Sin número" : contacto.getTelefono().getNumero());
            txtOval.setText(initial);
            GradientDrawable bgShape = (GradientDrawable) txtOval.getBackground();
            bgShape.setColor(contacto.getColor());
        }
    }

    @Override
    public ContactoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler, parent, false);

        return new ContactoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactoViewHolder holder, int position) {
        final int pos = position;
        Contacto item = datos.get(pos);
        holder.bindContacto(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)
                    listener.onItemClick(datos.get(pos));
            }

        });


    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Contacto item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }
}
