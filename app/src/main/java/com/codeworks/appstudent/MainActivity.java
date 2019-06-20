package com.codeworks.appstudent;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        ViewFlipper vf = findViewById(R.id.vf);
        vf.setDisplayedChild(0);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Usuario: "+nomusuario, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        /*PARAM ITINERARIO*/
        FloatingActionButton fab_itin = findViewById(R.id.fab_itinerario);
        fab_itin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crdItinerario = new Intent(v.getContext(), CrdItinerarioActivity.class);
                crdItinerario.putExtra("idTask", "0");
                startActivity(crdItinerario);
            }
        });
        /*END ITINERARIO*/

        /*PARAM NOTAS*/
        FloatingActionButton fab_notas = findViewById(R.id.fab_notas);
        fab_notas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crdNotas = new Intent(v.getContext(), CrdNotasActivity.class);
                crdNotas.putExtra("idTask", "0");
                startActivity(crdNotas);
            }
        });
        /*END NOTAS*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItinerarios();
        loadNotas();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_itinerario) {
            ViewFlipper vf = findViewById(R.id.vf);
            vf.setDisplayedChild(1);
            loadItinerarios();
        } else if (id == R.id.menu_notas) {
            ViewFlipper vf = findViewById(R.id.vf);
            vf.setDisplayedChild(2);
            loadNotas();
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

    public void loadItinerarios(){
        ArrayList<HashMap<String, String>> itinerarioList = new ArrayList<>();
        final ListView listItinerario = (ListView) findViewById(R.id.list_itinerario);
        listItinerario.setAdapter(null);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"AppStudent", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT id, nombre_itinerario, fecha_itinerario FROM  itinerarios WHERE id_usuario="+idusuario , null);
        if (fila.moveToFirst()) {
            while (!fila.isAfterLast()) {
                final HashMap<String,String> itinerario = new HashMap<>();
                String id = fila.getString(fila.getColumnIndex("id"));
                String nombre_itinerario = fila.getString(fila.getColumnIndex("nombre_itinerario"));
                String fecha_itinerario = fila.getString(fila.getColumnIndex("fecha_itinerario"));
                itinerario.put("list_id_itinerario",id);
                itinerario.put("list_name_itinerario",nombre_itinerario);
                itinerario.put("list_date_itinerario",fecha_itinerario);
                itinerarioList.add(itinerario);
                listItinerario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view = listItinerario.getChildAt(position);
                        TextView editText = view.findViewById(R.id.list_id_itinerario);
                        String id_itinerario = editText.getText().toString();
                        Intent initIntent = new Intent(view.getContext(), CrdItinerarioActivity.class);
                        initIntent.putExtra("idTask", "1");
                        initIntent.putExtra("idItinerario", id_itinerario);
                        startActivity(initIntent);
                    }
                });
                fila.moveToNext();
            }
            ListAdapter adapter = new SimpleAdapter(this, itinerarioList, R.layout.list_row_itinerario,new String[]{"list_id_itinerario","list_name_itinerario","list_date_itinerario"}, new int[]{R.id.list_id_itinerario, R.id.list_name_itinerario, R.id.list_date_itinerario});
            listItinerario.setAdapter(adapter);
        }
    }
    public void loadNotas(){
        ArrayList<HashMap<String, String>> notasList = new ArrayList<>();
        final ListView listNotas = (ListView) findViewById(R.id.list_notas);
        listNotas.setAdapter(null);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"AppStudent", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT id, nombre_materia, nota1, nota2, nota3 FROM notas WHERE id_usuario="+idusuario , null);
        if (fila.moveToFirst()) {
            while (!fila.isAfterLast()) {
                final HashMap<String,String> notas = new HashMap<>();
                String id = fila.getString(fila.getColumnIndex("id"));
                String nombre_materia = fila.getString(fila.getColumnIndex("nombre_materia"));
                String nota1 = fila.getString(fila.getColumnIndex("nota1"));
                String nota2 = fila.getString(fila.getColumnIndex("nota2"));
                String nota3 = fila.getString(fila.getColumnIndex("nota3"));
                String score_notas = "Nota 1: "+ nota1 +", Nota 2: "+ nota2 +", Nota 3: "+ nota3;
                notas.put("list_id_nota",id);
                notas.put("list_name_nota",nombre_materia);
                notas.put("list_score_nota",score_notas);
                notasList.add(notas);
                listNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view = listNotas.getChildAt(position);
                        TextView editText = view.findViewById(R.id.list_id_nota);
                        String id_nota = editText.getText().toString();
                        Intent initIntent = new Intent(view.getContext(), CrdNotasActivity.class);
                        initIntent.putExtra("idTask", "1");
                        initIntent.putExtra("idNota", id_nota);
                        startActivity(initIntent);
                    }
                });
                fila.moveToNext();
            }
            ListAdapter adapter = new SimpleAdapter(this, notasList, R.layout.list_row_notas,new String[]{"list_id_nota","list_name_nota","list_score_nota"}, new int[]{R.id.list_id_nota, R.id.list_name_nota, R.id.list_score_nota});
            listNotas.setAdapter(adapter);
        }

    }
}