package swoop.com.swoop.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import swoop.com.swoop.R;
import swoop.com.swoop.fragments.Help;
import swoop.com.swoop.fragments.History;
import swoop.com.swoop.fragments.MainMap;
import swoop.com.swoop.fragments.NoNetworkFragment;
import swoop.com.swoop.fragments.Notifications;
import swoop.com.swoop.fragments.Payment;
import swoop.com.swoop.fragments.Settings;
import swoop.com.swoop.utils.NetworkChecker;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkChecker.NetworkChangeListener {


    private MainMap mainMap;
    private NetworkChecker networkChecker;
    private NoNetworkFragment noNetworkFragment;
    private final static int PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get navigation drawer header view to manipulate items
        View headerView = navigationView.getHeaderView(0);
        TextView edit = (TextView)headerView.findViewById(R.id.edit_profile);

        //initialize variables

        networkChecker = new NetworkChecker();
        networkChecker.addListener(this);
        mainMap = new MainMap();
        this.registerReceiver(networkChecker,new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        noNetworkFragment = new NoNetworkFragment();

        //handle click events
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,EditProfile.class));
            }
        });

        //method call
        askPermission();
        setFragment(mainMap);

    }

    @Override
    public void networkAvailable() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(noNetworkFragment)
                .commit();
        setFragment(mainMap);
    }

    @Override
    public void networkUnavailable() {
        setFragment(noNetworkFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkChecker.removeListener(this);
        this.unregisterReceiver(networkChecker);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            setFragment(mainMap);
            getSupportActionBar().setTitle(R.string.title_home);
        }
        else if (id == R.id.nav_payment)
        {
            setFragment(new Payment());
            getSupportActionBar().setTitle(R.string.title_payment);
        }
        else if (id == R.id.nav_history)
        {
            setFragment(new History());
            getSupportActionBar().setTitle(R.string.title_history);
        }
        else if (id == R.id.nav_notifications)
        {
            setFragment(new Notifications());
            getSupportActionBar().setTitle(R.string.title_notifications);
        }
        else if (id == R.id.nav_settings)
        {
            setFragment(new Settings());
            getSupportActionBar().setTitle(R.string.title_settings);
        }
        else if (id == R.id.nav_help)
        {
            setFragment(new Help());
            getSupportActionBar().setTitle(R.string.help_and_feedback);
        }
        else if (id == R.id.nav_logout)
        {
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //ask for permissions
    private void askPermission(){

        int fine_location_permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (fine_location_permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_CODE);
        }

    }

    //add fragment
    private void setFragment(Fragment fragment){

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.map_layout,fragment)
                .commit();

    }

    //create alert dialog to confirm log out
    private void logOut(){

        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Home.this,Login.class));
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing happens....dialog dismissed
                    }
                }).show();

    }

}
