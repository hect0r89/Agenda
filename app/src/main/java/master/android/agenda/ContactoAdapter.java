package master.android.agenda;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by hector on 24/10/16.
 */

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>{

    private ArrayList<Contacto> datos;
    private static Context context;

    public ContactoAdapter(ArrayList<Contacto> contactos, Context context){
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
            txtOval = (TextView)itemView.findViewById(R.id.txt_oval);
            txtNombre = (TextView)itemView.findViewById(R.id.label_name);
            txtNumero = (TextView)itemView.findViewById(R.id.label_tlf);
        }

        public void bindContacto(Contacto contacto) {
            String initial = contacto.getNombre().substring(0,1).toUpperCase();
            txtNombre.setText(contacto.getNombre());
            txtNumero.setText(contacto.getNum_tlf().toString());
            txtOval.setText(initial);
            GradientDrawable bgShape = (GradientDrawable)txtOval.getBackground();
            bgShape.setColor(Utils.getMatColor("500",context));
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
        Contacto item = datos.get(position);

        holder.bindContacto(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }
}
