package master.android.agenda;

import org.junit.Test;

import static org.junit.Assert.*;
import master.android.agenda.Contacto;

/**
 * Created by hector on 15/12/16.
 */

public class AddContactTest {
    @Test
    public void createContactCorrect() throws Exception {
        Contacto c = new Contacto("Hector", "", new Telefono("666666666", Tipo.CASA), "", "", 0);
        assertEquals("", Utils.validateContacto(c));
    }

    @Test
    public void phoneNumberEmpty() throws Exception {
        Contacto c = new Contacto("Hector", "", null, "", "", 0);
        assertEquals("El télefono es obligatorio", Utils.validateContacto(c));
    }

    @Test
    public void nameEmpty() throws Exception {
        Contacto c = new Contacto("", "", new Telefono("666666666", Tipo.CASA), "", "", 0);
        assertEquals("El nombre es obligatorio\n" +
                "\n", Utils.validateContacto(c));
    }
}
