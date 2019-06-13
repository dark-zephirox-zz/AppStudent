package com.codeworks.appstudent;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText et1, et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        et1 = (EditText) findViewById(R.id.document_login);
        et2= (EditText) findViewById(R.id.password_login);
        Button btnContinue = findViewById(R.id.login_button);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(v);
            }
        });
        TextView register = findViewById(R.id.txtRegistrarse);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(v.getContext(), RegistroActivity.class);
                startActivity(regIntent);
            }
        });
    }
    public void logIn (View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"usuarios", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String document = et1.getText().toString();
        String password = et2.getText().toString();
        if (document.matches("") || password.matches("")){
            Toast.makeText(this, "Por favor, diligenciar todos los campos", Toast.LENGTH_SHORT).show();
        }else {
            Cursor fila = bd.rawQuery("SELECT id FROM usuarios WHERE documento='" + document + "' AND password='" + password +"'", null);
            if (fila.moveToFirst()) {
                //guardar idusuario y nomusuario en sharedpreferences
                Toast.makeText(this, fila.getString(0), Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(mainIntent);
            } else {
                Toast.makeText(this, "No existe el usuario con documento Nº " + document, Toast.LENGTH_SHORT).show();
            }
        }
    }
}