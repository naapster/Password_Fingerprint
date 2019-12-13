package com.example.password_fingerprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.example.password_fingerprint.password.*;

public class changepass extends AppCompatActivity {

    EditText editOld;
    EditText editNew;
    EditText editNew1;
    Button buttonChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);


        editOld = findViewById(R.id.editOld);
        editNew = findViewById(R.id.editNew);
        editNew1 = findViewById(R.id.editNew1);
        buttonChange = findViewById(R.id.buttonChange);

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (password.validatePassword(editOld.getText().toString(), password.loadData()) && editNew.getText().toString().equals(editNew1.getText().toString())){
                        String interSaltHash = password.generateStorngPasswordHash(editNew1.getText().toString());
                        password.saveData(interSaltHash);
                        Toast.makeText(changepass.this, "You changed your password", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(changepass.this, MainActivity.class);
                        startActivity(intent);


                    }
                    else {
                        Toast.makeText(changepass.this, "no", Toast.LENGTH_SHORT).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
