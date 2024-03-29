package com.codeworks.appstudent;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.Toast;

public class CrdNotasActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    String idusuario, nomusuario;
    private EditText et1, et2, et3, et4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud_notas_layout);
        sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("IdUsuario")) {
            idusuario = sharedpreferences.getString("IdUsuario", "");
        }
        if (sharedpreferences.contains("NomUsuario")) {
            nomusuario = sharedpreferences.getString("NomUsuario", "");
        }
        et1 = findViewById(R.id.nameMateria);
        et2 = findViewById(R.id.scoreNota1);
        et3 = findViewById(R.id.scoreNota2);
        et4 = findViewById(R.id.scoreNota3);
        String idTask = getIntent().getStringExtra("idTask");
        if (idTask.equals("1")){
            loadNotas();
        }
    }

    public void loadNotas(){
        try{
            String idNotas = getIntent().getStringExtra("idNota");
            int idNt = Integer.parseInt(idNotas);
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "AppStudent", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("SELECT nombre_materia, nota1, nota2, nota3 FROM notas WHERE id=" + idNt, null);
            if (fila.moveToFirst()) {
                et1.setText(fila.getString(fila.getColumnIndex("nombre_materia")));
                et2.setText(fila.getString(fila.getColumnIndex("nota1")));
                et3.setText(fila.getString(fila.getColumnIndex("nota2")));
                et4.setText(fila.getString(fila.getColumnIndex("nota3")));
            }else{
                Toast.makeText(this, "Error al cargar Notas", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void saveNota(View v){
        try{
            String idTask = getIntent().getStringExtra("idTask");
            if (idTask.equals("1")){
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"AppStudent", null, 1);
                SQLiteDatabase db = admin.getWritableDatabase();
                String idNota = getIntent().getStringExtra("idNota");
                String name_materia = et1.getText().toString();
                String score_nota1 = et2.getText().toString();
                String score_nota2 = et3.getText().toString();
                String score_nota3 = et4.getText().toString();
                if (score_nota1.matches("")){
                    score_nota1 = "0";
                }
                if (score_nota2.matches("")){
                    score_nota2 = "0";
                }
                if (score_nota3.matches("")){
                    score_nota3 = "0";
                }
                ContentValues registro = new ContentValues();
                registro.put("nombre_materia", name_materia);
                registro.put("nota1", score_nota1);
                registro.put("nota2", score_nota2);
                registro.put("nota3", score_nota3);
                int cant = db.update("notas", registro, "id=" + idNota,null);
                db.close();
                if (cant == 1){
                    Toast.makeText(this, "Se ha modificado las notas de "+name_materia, Toast.LENGTH_SHORT).show();
                    db = admin.getWritableDatabase();
                    db.execSQL("INSERT INTO operaciones (registro) VALUES ('Materia y Notas editadas')");
                    db.close();
                    this.finish();
                }else{
                    Toast.makeText(this, "No se pudieron editar las notas de "+name_materia,Toast.LENGTH_SHORT).show();
                }
            }else{
                String gradename = et1.getText().toString();
                String score1 = et2.getText().toString();
                String score2 = et3.getText().toString();
                String score3 = et4.getText().toString();
                if (gradename.matches("") ){
                    Toast.makeText(this, "Escribe un nombre para la Materia", Toast.LENGTH_SHORT).show();
                } else if ((score1.matches("") && score2.matches("") && score3.matches(""))){
                    Toast.makeText(this, "Digita al menos una Nota", Toast.LENGTH_SHORT).show();
                } else {
                    try{
                        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"AppStudent", null, 1);
                        SQLiteDatabase db = admin.getWritableDatabase();
                        if (score1.matches("")){
                            score1 = "0";
                        }
                        if (score2.matches("")){
                            score2 = "0";
                        }
                        if (score3.matches("")){
                            score3 = "0";
                        }
                        ContentValues registro = new ContentValues();
                        registro.put("id_usuario", idusuario);
                        registro.put("nombre_materia", gradename);
                        registro.put("nota1", score1);
                        registro.put("nota2", score2);
                        registro.put("nota3", score3);
                        db.insert("notas", null, registro);
                        db.close();
                        et1.setText("");
                        et2.setText("");
                        et3.setText("");
                        et4.setText("");
                        Toast.makeText(this, "Notas de "+ gradename +" creadas exitosamente!", Toast.LENGTH_SHORT).show();
                        db = admin.getWritableDatabase();
                        db.execSQL("INSERT INTO operaciones (registro) VALUES ('Materia y Notas creadas')");
                        db.close();
                        this.finish();
                    }catch (Exception e){
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void delNota(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"AppStudent", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String idNota = getIntent().getStringExtra("idNota");
        int cant = db.delete("notas", "id=" + idNota, null);
        db.close();
        if (cant == 1){
            Toast.makeText(this, "Materia eliminada exitosamente!", Toast.LENGTH_SHORT).show();
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");
            db = admin.getWritableDatabase();
            db.execSQL("INSERT INTO operaciones (registro) VALUES ('Materia Eliminada')");
            db.close();
            this.finish();
        }else{
            Toast.makeText(this, "No se pudo eliminar la Materia", Toast.LENGTH_SHORT).show();
        }
    }
}
