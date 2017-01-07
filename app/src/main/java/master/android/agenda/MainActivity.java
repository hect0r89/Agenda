package master.android.agenda;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ContactoAdapter.OnItemClickListener, DetailFragment.OnEditContacListener, EditFragment.OnOkEditContactListener, CreateFragment.OnOkCreateContactListener, ListFragment.OnCreateContacListener, DetailFragment.OnDeleteContacListener {
    boolean mDualPane;
    boolean noContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null;


        if (!mDualPane) {
            Fragment l = (fragmentManager.findFragmentById(R.id.container));
            if (savedInstanceState != null && l != null) {
                return;
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ListFragment fragment = new ListFragment();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }


    }

    @Override
    public void onItemClick(Contacto c) {

        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mDualPane) {
            noContact = detailsFrame.getVisibility() == View.GONE;
            if (noContact) {
                detailsFrame.setVisibility(View.VISIBLE);
                DetailFragment fragment = DetailFragment.newInstance(c);
                fragmentTransaction.add(R.id.details, fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            } else {
                DetailFragment fragment = DetailFragment.newInstance(c);
                fragmentTransaction.replace(R.id.details, fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();

            }
        } else {
            DetailFragment fragment = DetailFragment.newInstance(c);
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }

    }


    @Override
    public void onEditContact(Contacto c) {
        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mDualPane) {
            EditFragment fragment = EditFragment.newInstance(c);
            fragmentTransaction.replace(R.id.details, fragment);
            fragmentTransaction.commit();
        } else {
            EditFragment fragment = EditFragment.newInstance(c);
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onOkEditContact(Contacto c) {
        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mDualPane) {
            ((ListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.FrgListado)).updateEditList(c);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailFragment fragment = DetailFragment.newInstance(c);
            fragmentTransaction.replace(R.id.details, fragment);
            fragmentTransaction.commit();
        } else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onOkCreateContact(Contacto c) {
        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mDualPane) {
            ((ListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.FrgListado)).updateCreateList(c);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailFragment fragment = DetailFragment.newInstance(c);
            fragmentTransaction.replace(R.id.details, fragment);
            fragmentTransaction.commit();
        } else {
            fragmentManager.popBackStackImmediate();
            ((ListFragment) fragmentManager.findFragmentById(R.id.container)).updateCreateList(c);
        }
    }

    @Override
    public void onCreateContactFab() {
        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mDualPane) {
            noContact = detailsFrame.getVisibility() == View.GONE;
            if (noContact) {
                detailsFrame.setVisibility(View.VISIBLE);
                CreateFragment fragment = new CreateFragment();
                fragmentTransaction.add(R.id.details, fragment);
                fragmentTransaction.commit();
            } else {
                CreateFragment fragment = new CreateFragment();
                fragmentTransaction.replace(R.id.details, fragment);
                fragmentTransaction.commit();
            }
        } else {
            CreateFragment fragment = new CreateFragment();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onDeleteContact(Contacto c) {
        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mDualPane) {
            ((ListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.FrgListado)).deleteContactList(c);

            fragmentManager.popBackStackImmediate();
            detailsFrame.setVisibility(View.GONE);
        } else {
            fragmentManager.popBackStackImmediate();
            ((ListFragment) fragmentManager.findFragmentById(R.id.container)).deleteContactList(c);
        }
    }
}
