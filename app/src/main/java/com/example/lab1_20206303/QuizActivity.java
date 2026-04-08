package com.example.lab1_20206303;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private String userName;
    private String difficulty;
    private List<Question> questions;
    private int currentIndex = 0;
    private int totalScore = 0;
    private int hintsUsedTotal = 0;
    private int streak = 0;

    private Integer[] userAnswers;
    private boolean[] hintsUsedInQuestion;
    private int[] questionScores;
    private long startTime;

    private TextView textViewDifficulty, textViewScore, textViewQuestionNum, textViewQuestionText;
    private Button[] optionButtons;
    private TextView[] resultTexts;
    private Button buttonPrevious, buttonNext;
    private ImageButton buttonHint;
    private LinearLayout optionsContainer, finalResultContainer;
    private TextView textViewFinalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        userName = getIntent().getStringExtra("USER_NAME");
        difficulty = getIntent().getStringExtra("DIFFICULTY");
        questions = QuestionRepository.getQuestions(difficulty);

        userAnswers = new Integer[questions.size()];
        hintsUsedInQuestion = new boolean[questions.size()];
        questionScores = new int[questions.size()];
        startTime = System.currentTimeMillis();

        StatsManager.getInstance().setCurrentGame(new GameRecord(
                userName, difficulty, new Date(startTime), 0, 0, 0, questions.size(), 0, 0, false, true
        ));

        initViews();
        displayQuestion();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("ArtemisQuiz");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_revert); // Flechita
        }
    }

    private void initViews() {
        textViewDifficulty = findViewById(R.id.textViewDifficulty);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestionNum = findViewById(R.id.textViewQuestionNum);
        textViewQuestionText = findViewById(R.id.textViewQuestionText);
        
        optionButtons = new Button[]{
                findViewById(R.id.buttonOption1),
                findViewById(R.id.buttonOption2),
                findViewById(R.id.buttonOption3),
                findViewById(R.id.buttonOption4)
        };
        
        resultTexts = new TextView[]{
                findViewById(R.id.textViewResult1),
                findViewById(R.id.textViewResult2),
                findViewById(R.id.textViewResult3),
                findViewById(R.id.textViewResult4)
        };

        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        buttonHint = findViewById(R.id.buttonHint);
        optionsContainer = findViewById(R.id.optionsContainer);
        finalResultContainer = findViewById(R.id.finalResultContainer);
        textViewFinalScore = findViewById(R.id.textViewFinalScore);

        textViewDifficulty.setText("Dificultad:\n" + difficulty);

        for (int i = 0; i < optionButtons.length; i++) {
            final int index = i;
            optionButtons[i].setOnClickListener(v -> submitAnswer(index));
        }

        buttonNext.setOnClickListener(v -> {
            if (buttonNext.getText().equals("Volver a jugar")) {
                restartGame();
            } else if (currentIndex < questions.size() - 1) {
                currentIndex++;
                displayQuestion();
            } else {
                showFinalResults();
            }
        });

        buttonPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayQuestion();
            }
        });

        buttonHint.setOnClickListener(v -> useHint());
    }

    private void displayQuestion() {
        Question q = questions.get(currentIndex);
        textViewQuestionNum.setText((currentIndex + 1) + ".");
        textViewQuestionText.setText(q.getText());
        textViewScore.setText(String.valueOf(totalScore));

        List<String> options = q.getShuffledOptions();
        optionsContainer.setVisibility(View.VISIBLE);
        finalResultContainer.setVisibility(View.GONE);

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i));
            optionButtons[i].setEnabled(userAnswers[currentIndex] == null);
            // Reset colors
            optionButtons[i].setBackgroundResource(android.R.drawable.btn_default);
            optionButtons[i].setVisibility(View.VISIBLE);
            resultTexts[i].setVisibility(View.INVISIBLE);
        }

        if (userAnswers[currentIndex] != null) {
            int selected = userAnswers[currentIndex];
            int correct = q.getShuffledCorrectIndex();
            
            if (selected == correct) {
                optionButtons[selected].setBackgroundColor(Color.parseColor("#C8E6C9")); // Verde claro
            } else {
                optionButtons[selected].setBackgroundColor(Color.parseColor("#FFCDD2")); // Rojo claro
                optionButtons[correct].setBackgroundColor(Color.parseColor("#C8E6C9")); // Mostrar correcta también
            }
            
            resultTexts[selected].setVisibility(View.VISIBLE);
            resultTexts[selected].setText((questionScores[currentIndex] >= 0 ? "+" : "") + questionScores[currentIndex]);
            resultTexts[selected].setTextColor(questionScores[currentIndex] >= 0 ? Color.GREEN : Color.RED);
            
            buttonNext.setEnabled(true);
        } else {
            buttonNext.setEnabled(false);
        }

        buttonPrevious.setEnabled(currentIndex > 0);
        buttonHint.setEnabled(userAnswers[currentIndex] == null && hintsUsedTotal < 3 && !hintsUsedInQuestion[currentIndex]);
        
        if (currentIndex == questions.size() - 1 && userAnswers[currentIndex] != null) {
            buttonNext.setText("Finalizar");
        } else {
            buttonNext.setText("Siguiente");
        }
    }

    private void submitAnswer(int selectedIndex) {
        userAnswers[currentIndex] = selectedIndex;
        Question q = questions.get(currentIndex);
        boolean isCorrect = (selectedIndex == q.getShuffledCorrectIndex());

        int base;
        String qDiff = q.getDifficulty();
        int points = 0;

        if (isCorrect) {
            base = qDiff.equals("Fácil") ? 2 : 4;
            // Si hay racha positiva previa, suma +1
            int bonus = (streak > 0) ? 1 : 0;
            points = base + bonus;
            
            if (streak < 0) streak = 0;
            streak++;
        } else {
            base = qDiff.equals("Fácil") ? -3 : -6;
            // Si hay racha negativa previa, suma -1 (resta 1 más)
            int penalty = (streak < 0) ? -1 : 0;
            points = base + penalty;
            
            if (streak > 0) streak = 0;
            streak--;
        }

        questionScores[currentIndex] = points;
        totalScore += points;
        
        // Update current record in StatsManager
        GameRecord current = StatsManager.getInstance().getCurrentGame();
        if (current != null) {
            current.setScore(totalScore);
            current.setCorrectAnswers(countCorrectAnswers());
            current.setMaxStreak(getMaxStreak());
        }

        displayQuestion();
    }

    private void useHint() {
        if (hintsUsedTotal >= 3 || hintsUsedInQuestion[currentIndex]) return;

        Question q = questions.get(currentIndex);
        int correctIndex = q.getShuffledCorrectIndex();

        List<Integer> incorrectIndices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != correctIndex) incorrectIndices.add(i);
        }

        Collections.shuffle(incorrectIndices);
        optionButtons[incorrectIndices.get(0)].setVisibility(View.INVISIBLE);
        optionButtons[incorrectIndices.get(1)].setVisibility(View.INVISIBLE);

        hintsUsedTotal++;
        hintsUsedInQuestion[currentIndex] = true;
        totalScore -= 2;
        textViewScore.setText(String.valueOf(totalScore));
        buttonHint.setEnabled(false);
        
        GameRecord current = StatsManager.getInstance().getCurrentGame();
        if (current != null) {
            current.setHintsUsed(hintsUsedTotal);
            current.setScore(totalScore);
        }
        
        Toast.makeText(this, "Pista usada (-2 pts)", Toast.LENGTH_SHORT).show();
    }

    private void showFinalResults() {
        optionsContainer.setVisibility(View.GONE);
        textViewQuestionNum.setVisibility(View.GONE);
        textViewQuestionText.setVisibility(View.GONE);
        buttonHint.setVisibility(View.GONE);
        
        finalResultContainer.setVisibility(View.VISIBLE);
        textViewFinalScore.setText(String.valueOf(totalScore));
        if (totalScore >= 0) {
            textViewFinalScore.setBackgroundColor(Color.parseColor("#C8E6C9"));
        } else {
            textViewFinalScore.setBackgroundColor(Color.parseColor("#FFCDD2"));
        }

        buttonNext.setText("Volver a jugar");
        buttonNext.setEnabled(true);
        
        // Save game record
        GameRecord record = new GameRecord(
                userName,
                difficulty,
                new Date(startTime),
                System.currentTimeMillis() - startTime,
                hintsUsedTotal,
                totalScore,
                questions.size(),
                countCorrectAnswers(),
                getMaxStreak(),
                false,
                false
        );
        StatsManager.getInstance().addGame(record);
    }

    private void restartGame() {
        Intent intent = new Intent(QuizActivity.this, DifficultyActivity.class);
        intent.putExtra("USER_NAME", userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private int countCorrectAnswers() {
        int count = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] != null && userAnswers[i] == questions.get(i).getShuffledCorrectIndex()) {
                count++;
            }
        }
        return count;
    }

    private int getMaxStreak() {
        int max = 0;
        int current = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] != null && userAnswers[i] == questions.get(i).getShuffledCorrectIndex()) {
                current++;
                if (current > max) max = current;
            } else {
                current = 0;
            }
        }
        return max;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            cancelGame();
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelGame() {
        if (finalResultContainer.getVisibility() != View.VISIBLE) {
            GameRecord record = new GameRecord(
                    userName,
                    difficulty,
                    new Date(startTime),
                    System.currentTimeMillis() - startTime,
                    hintsUsedTotal,
                    totalScore,
                    questions.size(),
                    countCorrectAnswers(),
                    getMaxStreak(),
                    true,
                    false
            );
            StatsManager.getInstance().addGame(record);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelGame();
        super.onBackPressed();
    }
}
