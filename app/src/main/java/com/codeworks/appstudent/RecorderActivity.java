package com.codeworks.appstudent;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class RecorderActivity extends AppCompatActivity {

    Button buttonStart, buttonStop, buttonPlayLastRecordAudio, buttonStopPlayingRecording ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    String idusuario;
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recorder_layout);

        buttonStart = (Button) findViewById(R.id.btnRecord);
        buttonStop = (Button) findViewById(R.id.btnStop);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.btnPlay);
        buttonStopPlayingRecording = (Button)findViewById(R.id.btnStopPlay);
        final EditText nameAudio = findViewById(R.id.nameAudio);

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("IdUsuario")) {
            idusuario = sharedpreferences.getString("IdUsuario", "");
        }

        loadGrabaciones();

        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()) {
                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + nameAudio.getText().toString() +" - " + CreateRandomAudioFileName(5) + ".3gp";
                    MediaRecorderReady();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                    buttonPlayLastRecordAudio.setEnabled(false);
                    nameAudio.setEnabled(false);
                    Toast.makeText(RecorderActivity.this, "Grabación Iniciada!", Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                String name_audio = nameAudio.getText().toString();
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(RecorderActivity.this,"AppStudent", null, 1);
                SQLiteDatabase db = admin.getWritableDatabase();
                /*if (name_audio.matches("")){
                    name_audio = "Grabacion";
                }*/
                ContentValues registro = new ContentValues();
                registro.put("id_usuario", idusuario);
                registro.put("ruta_ubicacion", AudioSavePathInDevice);
                db.insert("grabaciones", null, registro);
                db.close();
                nameAudio.setText("");
                loadGrabaciones();
                Toast.makeText(RecorderActivity.this, "Grabación Completada!", Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                Toast.makeText(RecorderActivity.this, "Reproduciendo Grabación...", Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                nameAudio.setEnabled(true);
                nameAudio.setText("");
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(RecorderActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(RecorderActivity.this, "Permisos Concedidos",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecorderActivity.this,"Permisos Denegados",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
    public void loadGrabaciones(){
        ArrayList<HashMap<String, String>> recList = new ArrayList<>();
        final ListView listGrabacion = (ListView) findViewById(R.id.listGrabacion);
        listGrabacion.setAdapter(null);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"AppStudent", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT id, ruta_ubicacion FROM grabaciones WHERE id_usuario="+idusuario , null);
        if (fila.moveToFirst()) {
            while (!fila.isAfterLast()) {
                final HashMap<String,String> grabacion = new HashMap<>();
                String id = fila.getString(fila.getColumnIndex("id"));
                String nombre_materia = fila.getString(fila.getColumnIndex("ruta_ubicacion"));
                grabacion.put("list_id_grabacion",id);
                grabacion.put("list_name_grabacion",nombre_materia);
                recList.add(grabacion);
                listGrabacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view = listGrabacion.getChildAt(position);
                        TextView nameGrabacion = view.findViewById(R.id.list_name_grabacion);
                        AudioSavePathInDevice = nameGrabacion.getText().toString();
                        mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(AudioSavePathInDevice);
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                        Toast.makeText(RecorderActivity.this, "Reproduciendo Grabación...", Toast.LENGTH_LONG).show();

                    }
                });
                fila.moveToNext();
            }
            ListAdapter adapter = new SimpleAdapter(this, recList, R.layout.list_row_grabaciones,new String[]{"list_id_grabacion","list_name_grabacion"}, new int[]{R.id.list_id_grabacion, R.id.list_name_grabacion});
            listGrabacion.setAdapter(adapter);
        }
    }
}