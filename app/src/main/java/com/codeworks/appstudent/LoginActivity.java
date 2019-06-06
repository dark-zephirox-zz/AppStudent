package com.codeworks.appstudent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        /*Button btnContinue = (Button) findViewById(R.id.boton_ingresar);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent initIntent = new Intent(v.getContext(), ListItinerarioActivity.class);
                startActivity(initIntent);
            }
        });*/
    }
}