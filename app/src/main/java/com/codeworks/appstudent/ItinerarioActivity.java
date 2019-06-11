package com.codeworks.appstudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ItinerarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itinerario_layout);

        /*Button btnAddItinerario = (Button) findViewById(R.id.create_itinerario);
        btnAddItinerario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent initIntent = new Intent(v.getContext(), AddItinerarioActivity.class);
                startActivity(initIntent);
            }
        });
        try {
            String nametItin = getIntent().getStringExtra("nameItinerario");
            String cantItin = getIntent().getStringExtra("cantItinerario");
            ListView listItinerario = (ListView) findViewById(R.id.list_itinerarios);
            ArrayList <String> infoItinerario = new ArrayList();
            infoItinerario.add(nametItin.toString());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    infoItinerario );
            listItinerario.setAdapter(arrayAdapter);
        }catch (Exception e){};*/


    }
}
