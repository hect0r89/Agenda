package master.android.agenda;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import master.android.agenda.ContactContract.ContactEntry;
import master.android.agenda.PhoneContract.PhoneEntry;

/**
 * Created by hector on 16/11/16.
 */

public class DAOContentProvider {


    private ContentResolver cr;


    public DAOContentProvider(Context context) {
        cr = context.getContentResolver();
    }

    public long insertContact(Contacto contact) {

        ContentValues valuesPhone = new ContentValues();

        Telefono tlf = contact.getTelefono();
        valuesPhone.put(PhoneEntry.COLUMN_NAME_NUMBER, tlf.getNumero());
        valuesPhone.put(PhoneEntry.COLUMN_NAME_TYPE, tlf.getEnumTipo().ordinal());

        Uri u = cr.insert(Uri.parse(PhoneEntry.phoneContactUri), valuesPhone);
        long idTlf = ContentUris.parseId(u);

        ContentValues valuesContact = new ContentValues();

        valuesContact.put(ContactEntry.COLUMN_NAME_FIRST_NAME, contact.getNombre());
        valuesContact.put(ContactEntry.COLUMN_NAME_LAST_NAME, contact.getApellidos());
        valuesContact.put(ContactEntry.COLUMN_NAME_PHONE, idTlf);
        valuesContact.put(ContactEntry.COLUMN_NAME_EMAIL, contact.getCorreo());
        valuesContact.put(ContactEntry.COLUMN_NAME_ADDRESS, contact.getDireccion());
        valuesContact.put(ContactEntry.COLUMN_NAME_COLOR, contact.getColor());


        Uri uContact = cr.insert(Uri.parse(ContactEntry.contactUri), valuesContact);
        return ContentUris.parseId(uContact);

    }

    public ArrayList<Contacto> getAllContacts() {
        ArrayList<Contacto> data = new ArrayList<>();
        String[] projection = new String[]{
                ContactEntry._ID,
                ContactEntry.COLUMN_NAME_FIRST_NAME,
                ContactEntry.COLUMN_NAME_LAST_NAME,
                ContactEntry.COLUMN_NAME_EMAIL,
                ContactEntry.COLUMN_NAME_PHONE,
                ContactEntry.COLUMN_NAME_ADDRESS,
                ContactEntry.COLUMN_NAME_COLOR};

        Uri contactosUri = Uri.parse(ContactEntry.contactUri);



        Cursor cur = cr.query(contactosUri,
                projection, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados

        if (cur != null) {


            if (cur.moveToFirst()) {
                String nombre;
                String apellidos;
                String direccion;
                long telefono;
                int color;
                String email;
                long id;

                int colNombre = cur.getColumnIndex(ContactEntry.COLUMN_NAME_FIRST_NAME);
                int colApellido = cur.getColumnIndex(ContactEntry.COLUMN_NAME_LAST_NAME);
                int colTelefono = cur.getColumnIndex(ContactEntry.COLUMN_NAME_PHONE);
                int colEmail = cur.getColumnIndex(ContactEntry.COLUMN_NAME_EMAIL);
                int colDireccion = cur.getColumnIndex(ContactEntry.COLUMN_NAME_ADDRESS);
                int colColor = cur.getColumnIndex(ContactEntry.COLUMN_NAME_COLOR);
                int colId = cur.getColumnIndex(ContactEntry._ID);


                do {
                    nombre = cur.getString(colNombre);
                    apellidos = cur.getString(colApellido);
                    telefono = cur.getLong(colTelefono);
                    email = cur.getString(colEmail);
                    direccion = cur.getString(colDireccion);
                    color = cur.getInt(colColor);
                    id = cur.getLong(colId);
                    Telefono tlf = getTelefono(telefono);
                    data.add(new Contacto(nombre, apellidos, tlf, email, direccion, id, color));

                } while (cur.moveToNext());
            }
        }
        return data;
    }

    public int deleteContact(Contacto contact){
        int cont;
        Uri contactosUri =  Uri.parse(ContactEntry.contactUri+"/"+String.valueOf(contact.getId()));
        Uri telefonosUri =  Uri.parse(PhoneEntry.phoneContactUri+"/"+String.valueOf(contact.getTelefono().getId()));
        cont = cr.delete(contactosUri,null, null);
        cr.delete(telefonosUri,null, null);
        return cont;
    }

    public int updateContact(Contacto contact){
        ContentValues valuesPhone = new ContentValues();

        Telefono tlf = contact.getTelefono();
        valuesPhone.put(PhoneEntry.COLUMN_NAME_NUMBER, tlf.getNumero());
        valuesPhone.put(PhoneEntry.COLUMN_NAME_TYPE, tlf.getEnumTipo().ordinal());

        cr.update(Uri.parse(PhoneEntry.phoneContactUri+"/"+contact.getTelefono().getId()), valuesPhone, null, null);

        ContentValues valuesContact = new ContentValues();

        valuesContact.put(ContactEntry.COLUMN_NAME_FIRST_NAME, contact.getNombre());
        valuesContact.put(ContactEntry.COLUMN_NAME_LAST_NAME, contact.getApellidos());
        valuesContact.put(ContactEntry.COLUMN_NAME_EMAIL, contact.getCorreo());
        valuesContact.put(ContactEntry.COLUMN_NAME_ADDRESS, contact.getDireccion());
        valuesContact.put(ContactEntry.COLUMN_NAME_COLOR, contact.getColor());


        return cr.update(Uri.parse(ContactEntry.contactUri+"/"+contact.getId()), valuesContact, null, null);


    }

    public Telefono getTelefono(long id) {
        Telefono telefono = null;

        String[] projectionPhone = new String[]{
                PhoneEntry._ID,
                PhoneEntry.COLUMN_NAME_NUMBER,
                PhoneEntry.COLUMN_NAME_TYPE};

        Uri telefonosUri =  Uri.parse(PhoneEntry.phoneContactUri+"/"+String.valueOf(id));
        Cursor curPh = cr.query(telefonosUri,
                projectionPhone, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);

        if(curPh != null){
            if(curPh.moveToFirst()){
                int colNumero = curPh.getColumnIndex(PhoneEntry.COLUMN_NAME_NUMBER);
                int colTipo = curPh.getColumnIndex(PhoneEntry.COLUMN_NAME_TYPE);
                int colId = curPh.getColumnIndex(PhoneEntry._ID);

                telefono = new Telefono(curPh.getString(colNumero), Tipo.values()[curPh.getInt(colTipo)], curPh.getLong(colId));
            }

        }
        return telefono;

    }
}
