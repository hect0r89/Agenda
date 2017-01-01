package master.android.agenda;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private String nombreToBetyped;
    private String apellidosToBetyped;
    private String telefonoBetyped;
    private String emailToBetyped;
    private String direccionToBetyped;

    @Rule
    public ActivityTestRule<CreateActivity> mActivityRule = new ActivityTestRule<>(
            CreateActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        nombreToBetyped = "Nombre contacto";
        apellidosToBetyped = "Apellidos contacto";
        telefonoBetyped = "666666666";
        emailToBetyped = "email@gmail.com";
        direccionToBetyped = "Direccion contacto";
    }

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.etNombre))
                .perform(typeText(nombreToBetyped), closeSoftKeyboard());

        onView(withId(R.id.etNombre))
                .check(matches(withText(nombreToBetyped)));


        onView(withId(R.id.etApellidos))
                .perform(typeText(apellidosToBetyped), closeSoftKeyboard());

        onView(withId(R.id.etApellidos))
                .check(matches(withText(apellidosToBetyped)));


        onView(withId(R.id.etTelefono))
                .perform(typeText(telefonoBetyped), closeSoftKeyboard());

        onView(withId(R.id.etTelefono))
                .check(matches(withText(telefonoBetyped)));


        onView(withId(R.id.etCorreo))
                .perform(typeText(emailToBetyped), closeSoftKeyboard());

        onView(withId(R.id.etCorreo))
                .check(matches(withText(emailToBetyped)));


        onView(withId(R.id.etDireccion))
                .perform(typeText(direccionToBetyped), closeSoftKeyboard());

        onView(withId(R.id.etDireccion))
                .check(matches(withText(direccionToBetyped)));

    }
}
