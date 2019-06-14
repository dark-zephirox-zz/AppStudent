package com.codeworks.appstudent;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedpreferences;
    String idusuario;
    String nomusuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences("pref",Context.MODE_PRIVATE);
        if (sharedpreferences.contains("IdUsuario")) {
            idusuario = sharedpreferences.getString("IdUsuario", "");
        }
        if (sharedpreferences.contains("NomUsuario")) {
            nomusuario = sharedpreferences.getString("NomUsuario", "");
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Usuario: "+nomusuario, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton fab_itin = findViewById(R.id.fab_itinerario);
        fab_itin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crdItinerario = new Intent(v.getContext(), CrdItinerarioActivity.class);
                startActivity(crdItinerario);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        ViewFlipper vf = findViewById(R.id.vf);
        vf.setDisplayedChild(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Toast.makeText(this, "Presione nuevamente para salir", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_itinerario) {
            ViewFlipper vf = findViewById(R.id.vf);
            vf.setDisplayedChild(1);
        } else if (id == R.id.menu_notas) {
            ViewFlipper vf = findViewById(R.id.vf);
            vf.setDisplayedChild(2);
        } else if (id == R.id.menu_estadisticas) {
            ViewFlipper vf = findViewById(R.id.vf);
            vf.setDisplayedChild(3);
        } else if (id == R.id.menu_grabadora) {
            ViewFlipper vf = findViewById(R.id.vf);
            vf.setDisplayedChild(4);
        } else if (id == R.id.menu_logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("IdUsuario", "");
            editor.putString("NomUsuario", "");
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
