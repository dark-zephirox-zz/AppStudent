package com.codeworks.appstudent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Button btnContinue = findViewById(R.id.login_button);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent initIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(initIntent);
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
}