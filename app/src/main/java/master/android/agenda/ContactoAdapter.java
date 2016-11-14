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

    private static final int EDIT_CONTACT = 2;
    private static final int DETAIL_CONTACT = 3;
    private ArrayList<Contacto> datos;
    private static Context context;

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
            txtNombre.setText(contacto.getNombre()+" "+contacto.getApellidos());
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
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("contacto", datos.get(pos));
                ((Activity) context).startActivityForResult(i, DETAIL_CONTACT);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View sheetView = li.inflate(R.layout.sheet_contacto, null);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();

                LinearLayout edit = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
                LinearLayout delete = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);


                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, EditActivity.class);
                        i.putExtra("contacto", datos.get(pos));
                        ((Activity) context).startActivityForResult(i, EDIT_CONTACT);
                        mBottomSheetDialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                        AlertDialog alertbox = new AlertDialog.Builder(context)
                                .setMessage("¿Está seguro de querer eliminar este contacto?")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        deleteContact(datos.get(pos));

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
                return true;
            }
        });
    }

    private void deleteContact(Contacto contacto) {
        File fdelete = new File(context.getFilesDir(), contacto.getId());
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                CoordinatorLayout coord = (CoordinatorLayout) ((Activity) context).findViewById(R.id.activity_main);
                Snackbar.make(coord, "Contacto eliminado correctamente", Snackbar.LENGTH_LONG)
                        .show();
                datos.remove(contacto);
                notifyDataSetChanged();
            } else {
                new AlertDialog.Builder(context).setTitle("Error").setMessage("Error al eliminar el contacto").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }
}
