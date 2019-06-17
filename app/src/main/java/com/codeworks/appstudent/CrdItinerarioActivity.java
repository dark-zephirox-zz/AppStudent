package com.codeworks.appstudent;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

public class CrdItinerarioActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    String idusuario, nomusuario;
    private EditText et1, et2;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud_itinerario_layout);
        sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("IdUsuario")) {
            idusuario = sharedpreferences.getString("IdUsuario", "");
        }
        if (sharedpreferences.contains("NomUsuario")) {
            nomusuario = sharedpreferences.getString("NomUsuario", "");
        }
        et1 = findViewById(R.id.nameItinerario);
        et2 = findViewById(R.id.dateItinerario);
        ImageButton date = findViewById(R.id.btnDate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(CrdItinerarioActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            EditText dateItinerario = findViewById(R.id.dateItinerario);
                            dateItinerario.setText(dayOfMonth + "/"+ (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            }
        });
        loadItinerario();
    }
    public void loadItinerario() {
        String idItinerario = getIntent().getStringExtra("idItinerario");
        int idIt = Integer.parseInt(idItinerario);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "AppStudent", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT nombre_itinerario, fecha_itinerario FROM itinerarios WHERE id=" + idIt, null);
        if (fila.moveToFirst()) {
            et1.setText(fila.getString(fila.getColumnIndex("nombre_itinerario")));
            et2.setText(fila.getString(fila.getColumnIndex("fecha_itinerario")));
        }else{
            Toast.makeText(this, "Error al cargar Itinerario", Toast.LENGTH_SHORT).show();
        }
    }
    public void insItinerario(View v){
        try{

            String schedulename = et1.getText().toString();
            String dateschedule = et2.getText().toString();
            if (schedulename.matches("") || dateschedule.matches("")){
                Toast.makeText(this, "Por favor, diligenciar todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"AppStudent", null, 1);
                SQLiteDatabase db = admin.getWritableDatabase();
                ContentValues registro = new ContentValues();
                registro.put("id_usuario", idusuario);
                registro.put("nombre_itinerario", schedulename);
                registro.put("fecha_itinerario", dateschedule);
                db.insert("itinerarios", null, registro);
                db.close();
                et1.setText("");
                et2.setText("");
                Toast.makeText(this, "Itinerario creado exitosamente!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void delItinerario(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"AppStudent", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String idItinerario = getIntent().getStringExtra("idItinerario");
        int idIt = Integer.parseInt(idItinerario);
        int cant = db.delete("itinerarios", "id=" + idIt, null);
        db.close();
        if (cant == 1){
            Toast.makeText(this, "Itinerario eliminado exitosamente!", Toast.LENGTH_SHORT).show();
            et1.setText("");
            et2.setText("");
        }else{
            Toast.makeText(this, "No se pudo eliminar el Itinerario", Toast.LENGTH_SHORT).show();
        }
    }
}