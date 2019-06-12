package com.codeworks.appstudent;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {
    private EditText et1,et2,et3,et4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        et1 = (EditText) findViewById(R.id.user_name);
        et2 = (EditText) findViewById(R.id.document_number);
        et3 = (EditText) findViewById(R.id.input_password);
        et4 = (EditText) findViewById(R.id.user_phone);
    }
    public void regUsuario (View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "registro", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String username = et1.getText().toString();
        String docnumber = et2.getText().toString();
        String password = et3.getText().toString();
        String phone = et4.getText().toString();
        if (username.matches("" )||docnumber.matches("")||password.matches("")||phone.matches("")){
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }else{
            ContentValues registro = new ContentValues();
            registro.put("nombre_usuario", username);
            registro.put("documento", docnumber);
            registro.put("password", password);
            registro.put("telefono", phone);
            db.insert("usuarios", null, registro);
            db.close();
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");
            Toast.makeText(this, "Usuario creado exitosamente!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
