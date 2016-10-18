package com.charlie.weatherapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.charlie.weatherapp.R;
import com.charlie.weatherapp.api.ApiClient;
import com.charlie.weatherapp.model.CitiesFindResponse;
import com.charlie.weatherapp.model.CityWeather;
import com.charlie.weatherapp.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        WeatherAdapter.WeatherClickListener {

    public static final int REQUEST_CODE_VIEW_SHOT = 5407;

    private boolean twoPane;
    private boolean connected;
    private ProgressBar loading;
    private ImageView noConnection;

    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getBoolean(R.bool.force_landscape)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        loading = (ProgressBar) findViewById(android.R.id.empty);
        twoPane = findViewById(R.id.detailContainer) != null;
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, getString(R.string.setting_message), Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_cities) {
            // Do nothing, we are always here for this example app
        } else if (id == R.id.nav_about) {
            startActivity(AboutActivity.getIntent(this));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onWeatherClick(View view, CityWeather cityWeather) {
        if (twoPane) {
            Fragment fragment = WeatherDetailFragment.getInstance(cityWeather, true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailContainer, fragment, WeatherDetailFragment.TAG)
                    .commit();
        } else {
            Intent intent = WeatherDetailActivity.newIntent(this, cityWeather);
            startActivity(intent);
        }

    }

    private void loadData() {
        showProgressBar(true);
        Call<CitiesFindResponse> service = ApiClient.getInstance()
                .getCityListService("9.9280690", "-84.0907250", 15);

        service.enqueue(new Callback<CitiesFindResponse>() {
            @Override
            public void onResponse(Call<CitiesFindResponse> call,
                                   Response<CitiesFindResponse> response) {
                showProgressBar(false);
                if (response.isSuccessful()) {
                    adapter.setDataSet(response.body().getList());
                } else {
                    Toast.makeText(HomeActivity.this, "Open Weather error...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CitiesFindResponse> call, Throwable t) {
                showProgressBar(false);
                Toast.makeText(HomeActivity.this, "Open Weather error...", Toast.LENGTH_SHORT).show();
                Log.getStackTraceString(t);
            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        adapter = new WeatherAdapter(this, null, this);
        recyclerView.setAdapter(adapter);
    }

    private void showProgressBar(boolean state) {
        loading.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    private void checkConnectivity() {
        final ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        connected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!connected) {
            showProgressBar(false);
            if (noConnection == null) {
                final ViewStub stub = (ViewStub) findViewById(R.id.stub_no_connection);
                noConnection = (ImageView) stub.inflate();
            }
            if (Utils.hasLollipop()) {
                final AnimatedVectorDrawableCompat avd =
                        AnimatedVectorDrawableCompat.create(this, R.drawable.avd_no_connection);
                noConnection.setImageDrawable(avd);
                avd.start();
            } else {
                // TODO show another image
            }
        }
    }


}
