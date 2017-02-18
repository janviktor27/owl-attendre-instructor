package com.example.zero.Instructor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //location
    private TextView tvLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;


    //
    final String TAG = this.getClass().getName();

    //sharedpreferencesss
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public Double lat;
    public Double longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getSharedPreferences("logi.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        String username = pref.getString("username", "");
        String password = pref.getString("password", "");

        if((username.equals("") && password.equals(""))){
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
        }

        Log.d(TAG, pref.getString("username", ""));
        Log.d(TAG, pref.getString("password", ""));

        View b = findViewById(R.id.tvLocation);
        b.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(MainActivity.this, MainActivity.class);
                startActivity(home);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getname
        TextView tv = (TextView)findViewById(R.id.tvUsername);
        tv.setText(username);

        //getLocation
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tvLocation.append("\n "+location.getLatitude()+" "+location.getLongitude());
                lat = location.getLatitude();
                longi = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settings);
            }
        };

        configure_button();
    }
    //location
    private void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    //status

    public void onLeave(View v){
        HashMap data = new HashMap();
        String leave = "On Leave";
        data.put("status", leave);
        data.put("username", getIntent().getStringExtra("Username"));

        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, data, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(TAG, s);
                if(s.contains("success")){
                    //Intent i = new Intent(Main2Activity.this, Main2Activity.class);
                    //startActivity(i);
                }
            }
        });
        task.execute("http://10.0.2.2/owl_attendance/statusupdate.php");
    }

    public void onAbsent(View v){
        HashMap data = new HashMap();
        String leave = "Absent";
        data.put("status", leave);
        data.put("username", getIntent().getStringExtra("Username"));

        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, data, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(TAG, s);
                if(s.contains("success")){
                    //Intent i = new Intent(Main2Activity.this, Main2Activity.class);
                    //startActivity(i);
                }
            }
        });
        task.execute("http://10.0.2.2/owl_attendance/statusupdate.php");
    }

    public void onLate(View v){
        HashMap data = new HashMap();
        String leave = "Going Late";
        data.put("status", leave);
        data.put("username", getIntent().getStringExtra("Username"));

        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, data, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(TAG, s);
                if(s.contains("success")){
                    //Intent i = new Intent(Main2Activity.this, Main2Activity.class);
                    //startActivity(i);
                }
            }
        });
        task.execute("http://10.0.2.2/owl_attendance/statusupdate.php");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so lo ng
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Logout) {
            editor = pref.edit();
            editor.clear();
            editor.commit();
            Intent out = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(out);
        } else{
            setTitle("Account Settings");
            Intent out = new Intent(MainActivity.this, ChangePass.class);
            startActivity(out);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.first_fragment) {
            View b = findViewById(R.id.tvLocation);
            b.setVisibility(View.INVISIBLE);
            View a = findViewById(R.id.tvUsername);
            a.setVisibility(View.INVISIBLE);
            View c = findViewById(R.id.btLeave);
            c.setVisibility(View.INVISIBLE);
            View d = findViewById(R.id.btAbsent);
            d.setVisibility(View.INVISIBLE);
            View e = findViewById(R.id.btLate);
            e.setVisibility(View.INVISIBLE);
            View f = findViewById(R.id.tvTitlestatus);
            f.setVisibility(View.INVISIBLE);
            setTitle("Search");
            FirstFragment fragment = new FirstFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main,fragment,"Fragment");
            fragmentTransaction.commit();
        } else if (id == R.id.second_fragment) {
            View b = findViewById(R.id.tvLocation);
            b.setVisibility(View.VISIBLE);
            View a = findViewById(R.id.tvUsername);
            a.setVisibility(View.INVISIBLE);
            View c = findViewById(R.id.btLeave);
            c.setVisibility(View.INVISIBLE);
            View d = findViewById(R.id.btAbsent);
            d.setVisibility(View.INVISIBLE);
            View e = findViewById(R.id.btLate);
            e.setVisibility(View.INVISIBLE);
            View f = findViewById(R.id.tvTitlestatus);
            f.setVisibility(View.INVISIBLE);
            setTitle("Location Log");
            SecondFragment fragment = new SecondFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main,fragment,"Fragment");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
