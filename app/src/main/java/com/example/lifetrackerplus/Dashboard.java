package com.example.lifetrackerplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
 * I think since I don't really know where this app is going, I will start with the add task form,
 * so I can get some data in the internal storage.
 */
public class Dashboard extends AppCompatActivity {

    Button addEventBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Hooks
        addEventBtn = findViewById(R.id.dash_btn_addTrack);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, AddTrack.class));
            }
        });
    }
}
