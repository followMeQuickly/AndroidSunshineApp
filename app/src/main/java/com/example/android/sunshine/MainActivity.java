package com.example.android.sunshine;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.PerformanceTestCase;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private String mLocation;

    private Boolean mIsTwoPane;
    private final String DETAILFRAGMENT_TAG = " DFTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocation =
                Utility.getPreferredLocation(this);
       if(findViewById(R.id.container) != null)
       {
           mIsTwoPane = true;
           if(savedInstanceState == null)
           {
               getSupportFragmentManager()
                       .beginTransaction()
                       .replace(R.id.container,
                               new DetailFragment(),
                               DETAILFRAGMENT_TAG)
                       .commit();
           }
           else
           {
               mIsTwoPane = false;
           }
       }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        if(location != null && location.equals(mLocation))
        {
            ForecastFragment forecastFragment =
                    (ForecastFragment)getSupportFragmentManager()
                            .findFragmentById(R.id.fragment_forecast);
            forecastFragment.onLocationChanged();
            mLocation = Utility.getPreferredLocation(this);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId() == R.id.preferredLocation)
        {
            openPreferredLocationInMap();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String location =
               Utility.getPreferredLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);

        mapIntent.setData(geoLocation);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
