package master.android.agenda;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ContactoAdapter.OnItemClickListener {
    boolean mDualPane;
    boolean noContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null;
        if(!mDualPane){
            if (savedInstanceState != null) {
                return;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
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
        if (mDualPane) {
            noContact = detailsFrame.getVisibility() == View.GONE;
            if (noContact) {
                detailsFrame.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DetailFragment fragment = DetailFragment.newInstance(c);
                fragmentTransaction.add(R.id.details, fragment);
                fragmentTransaction.commit();
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DetailFragment fragment = DetailFragment.newInstance(c);
                fragmentTransaction.replace(R.id.details, fragment);
                fragmentTransaction.commit();

            }
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailFragment fragment = DetailFragment.newInstance(c);
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}
