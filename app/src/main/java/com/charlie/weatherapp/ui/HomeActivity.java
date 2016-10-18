package com.charlie.weatherapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
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
    private Toolbar toolbar;

    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cities) {
            // Handle the camera action
        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onWeatherClick(View view, CityWeather cityWeather) {
        Intent intent = WeatherDetailActivity.newIntent(this, cityWeather);
//        if (Utils.hasLollipop()) {
//            ActivityOptions options =
//                    ActivityOptions.makeSceneTransitionAnimation(this,
//                            Pair.create(view, getString(R.string.transition_shot)),
//                            Pair.create(view, getString(R.string.transition_shot_background)));
//            startActivityForResult(intent, REQUEST_CODE_VIEW_SHOT, options.toBundle());
//        } else {
//            startActivity(intent);
//        }
        startActivity(intent);
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
                    // TODO: show message erro
                }
            }

            @Override
            public void onFailure(Call<CitiesFindResponse> call, Throwable t) {
                showProgressBar(false);
                // TODO: show message error
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
