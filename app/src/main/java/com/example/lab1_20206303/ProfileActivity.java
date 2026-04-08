package com.example.lab1_20206303;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userName = getIntent().getStringExtra("USER_NAME");
        StatsManager stats = StatsManager.getInstance();

        TextView textViewPlayerName = findViewById(R.id.textViewPlayerName);
        TextView textViewStartTime = findViewById(R.id.textViewStartTime);
        TextView textViewTotalGames = findViewById(R.id.textViewTotalGames);
        TextView textViewMaxStreak = findViewById(R.id.textViewMaxStreak);
        TextView textViewCorrectPercentage = findViewById(R.id.textViewCorrectPercentage);
        TextView textViewAverageScore = findViewById(R.id.textViewAverageScore);
        RecyclerView recyclerViewHistory = findViewById(R.id.recyclerViewHistory);

        textViewPlayerName.setText("Jugador: " + userName);
        
        // Use the first game start time as "Inicio de la aplicación" or similar
        List<GameRecord> history = stats.getHistory();
        if (!history.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm a", Locale.getDefault());
            textViewStartTime.setText("Inicio: " + sdf.format(history.get(0).getStartTime()));
        } else if (stats.getCurrentGame() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm a", Locale.getDefault());
            textViewStartTime.setText("Inicio: " + sdf.format(stats.getCurrentGame().getStartTime()));
        }

        textViewTotalGames.setText("Cantidad de Partidas: " + stats.getTotalGames());
        textViewMaxStreak.setText("Racha más larga de aciertos: " + stats.getMaxStreak());
        textViewCorrectPercentage.setText(String.format(Locale.getDefault(), "Preguntas acertadas: %.0f%%", stats.getCorrectPercentage()));
        textViewAverageScore.setText(String.format(Locale.getDefault(), "Promedio de puntaje: %.2f", stats.getAverageScore()));

        List<GameRecord> displayList = new ArrayList<>(history);
        if (stats.getCurrentGame() != null) {
            displayList.add(stats.getCurrentGame());
        }

        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(new GameRecordAdapter(displayList));

        getSupportActionBar().setTitle("ArtemisQuiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
