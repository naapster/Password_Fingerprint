package com.example.password_fingerprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AfterLoginPass extends AppCompatActivity {

    Button buttonChange1;
    Button buttonShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_pass);

        buttonChange1 = findViewById(R.id.buttonChange1);
        buttonShow = findViewById(R.id.buttonNote);
        buttonChange1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterLoginPass.this, changepass.class);
                startActivity(intent);
            }
        });
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterLoginPass.this, ShowNote.class);
                startActivity(intent);

            }
        });
    }
}
