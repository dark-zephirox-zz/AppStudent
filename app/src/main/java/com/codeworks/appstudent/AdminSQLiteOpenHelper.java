package com.codeworks.appstudent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name,
                                 SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (id INT PRIMARY KEY, nombre_usuario TEXT, documento TEXT, password TEXT, telefono INT)");
        db.execSQL("CREATE TABLE itinerarios (id INT PRIMARY KEY, nombre_itinerario TEXT, fecha_itinerario DATE)");
        db.execSQL("CREATE TABLE notas (id INT PRIMARY KEY, nombre_materia TEXT, nota1 DECIMAL(10,2), nota2 DECIMAL(10,2), nota3 DECIMAL(10,2))");
        db.execSQL("CREATE TABLE grabaciones (id INT PRIMARY KEY, nombre_grabacion TEXT, ruta_ubicacion TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
    }

}
