package com.example.lab1_20206303;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextName = findViewById(R.id.editTextName);
        Button buttonEnter = findViewById(R.id.buttonEnter);

        buttonEnter.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, DifficultyActivity.class);
                intent.putExtra("USER_NAME", name);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Por favor ingrese su nombre", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
