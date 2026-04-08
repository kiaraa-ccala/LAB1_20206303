package com.example.lab1_20206303;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        userName = getIntent().getStringExtra("USER_NAME");
        TextView textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewWelcome.setText("BIENVENIDO\n" + userName);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("ArtemisQuiz");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_revert); // Flechita
        }

        Button buttonEasy = findViewById(R.id.buttonEasy);
        Button buttonHard = findViewById(R.id.buttonHard);
        Button buttonRandom = findViewById(R.id.buttonRandom);

        buttonEasy.setOnClickListener(v -> startQuiz("Fácil"));
        buttonHard.setOnClickListener(v -> startQuiz("Difícil"));
        buttonRandom.setOnClickListener(v -> startQuiz("Aleatorio"));
    }

    private void startQuiz(String difficulty) {
        Intent intent = new Intent(DifficultyActivity.this, QuizActivity.class);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("DIFFICULTY", difficulty);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
