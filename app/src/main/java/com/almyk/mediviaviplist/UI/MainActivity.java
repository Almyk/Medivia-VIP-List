package com.almyk.mediviaviplist.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.ViewModel.NavigationViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawer;

    private NavigationViewModel mNavigationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_vip_list, false);

        mNavigationViewModel = ViewModelProviders.of(this).get(NavigationViewModel.class);

        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            VipListFragment vipListFragment = VipListFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container, vipListFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_vip_list:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new VipListFragment()).commit();
                break;
            case R.id.nav_online_legacy:
                mNavigationViewModel.prepareOnlineListFragment(menuItem.getTitle().toString().toLowerCase());
                break;
            case R.id.nav_online_pendulum:
                mNavigationViewModel.prepareOnlineListFragment(menuItem.getTitle().toString().toLowerCase());
                break;
            case R.id.nav_online_destiny:
                mNavigationViewModel.prepareOnlineListFragment(menuItem.getTitle().toString().toLowerCase());
                break;
            case R.id.nav_online_prophecy:
                mNavigationViewModel.prepareOnlineListFragment(menuItem.getTitle().toString().toLowerCase());
                break;
        }
        Toast.makeText(this, menuItem.getTitle().toString(), Toast.LENGTH_LONG).show();
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
