package com.almyk.mediviaviplist.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.UI.VipList.VipListFragment;
import com.almyk.mediviaviplist.ViewModel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawer;
    private NavigationView mNavView;

    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences_vip_list, false);

        mDrawer = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        setupViewModel();

        if(savedInstanceState == null) {
            VipListFragment vipListFragment = VipListFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container, vipListFragment).commit();
        }
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mViewModel.getOnlineLegacy().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayerEntity> playerEntities) {
                mNavView.getMenu().findItem(R.id.nav_online_legacy).setTitle("Legacy (" + playerEntities.size() +")");
            }
        });

        mViewModel.getOnlineDestiny().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayerEntity> playerEntities) {
                mNavView.getMenu().findItem(R.id.nav_online_destiny).setTitle("Destiny (" + playerEntities.size() +")");
            }
        });

        mViewModel.getOnlinePendulum().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayerEntity> playerEntities) {
                mNavView.getMenu().findItem(R.id.nav_online_pendulum).setTitle("Pendulum (" + playerEntities.size() +")");
            }
        });

        mViewModel.getOnlineProphecy().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayerEntity> playerEntities) {
                mNavView.getMenu().findItem(R.id.nav_online_prophecy).setTitle("Prophecy (" + playerEntities.size() +")");
            }
        });
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
        // TODO maybe change this switch to use menuItem.getGroupId() instead
        switch(menuItem.getItemId()) {
            case R.id.nav_vip_list:
                getSupportActionBar().setTitle(R.string.app_name);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new VipListFragment()).commit();
                break;
            case R.id.nav_online_legacy:
                showOnlineList(menuItem, "Legacy");
                break;
            case R.id.nav_online_pendulum:
                showOnlineList(menuItem, "Pendulum");
                break;
            case R.id.nav_online_destiny:
                showOnlineList(menuItem, "Destiny");
                break;
            case R.id.nav_online_prophecy:
                showOnlineList(menuItem, "Prophecy");
                break;
            case R.id.nav_highscore_destiny:
            case R.id.nav_highscore_legacy:
            case R.id.nav_highscore_pendulum:
            case R.id.nav_highscore_prophecy:
                HighscoreFragment highscoreFragment = HighscoreFragment.newInstance();
                highscoreFragment.setServer(menuItem.getTitle().toString());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, highscoreFragment).commit();
                break;
            case R.id.nav_search_player:
                SearchCharacterFragment searchCharacterFragment = SearchCharacterFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, searchCharacterFragment).commit();
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showOnlineList(@NonNull MenuItem menuItem, String server) {
        getSupportActionBar().setTitle(menuItem.getTitle());
        OnlineListFragment onlineListFragment = OnlineListFragment.newInstance();
        onlineListFragment.setServer(server);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, onlineListFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManagerCompat.from(getApplication()).cancelAll();
    }
}
