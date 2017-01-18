package master.android.agenda;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UpdateContactTest {

    private static final String CONTACT_NAME = "Bruce Wayne";

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void teardown() {
        Intents.release();
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void updateContactTest() {
        onView(withId(R.id.RecView)).perform(actionOnItemAtPosition(0, click()));
        hasComponent(DetailActivity.class.getName());
        onView(withId(R.id.action_edit)).perform(click());
        hasComponent(EditActivity.class.getName());
        onView(withId(R.id.etEditNombre)).perform(click(), clearText(), typeText(CONTACT_NAME));
        onView(withId(R.id.action_create)).perform(click());
        hasComponent(DetailActivity.class.getName());
    }
}
