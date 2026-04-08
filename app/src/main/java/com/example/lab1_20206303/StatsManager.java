package com.example.lab1_20206303;

import java.util.ArrayList;
import java.util.List;

public class StatsManager {
    private static StatsManager instance;
    private List<GameRecord> history = new ArrayList<>();
    private GameRecord currentGame;

    private StatsManager() {}

    public static synchronized StatsManager getInstance() {
        if (instance == null) {
            instance = new StatsManager();
        }
        return instance;
    }

    public void setCurrentGame(GameRecord game) {
        this.currentGame = game;
    }

    public GameRecord getCurrentGame() {
        return currentGame;
    }

    public void addGame(GameRecord record) {
        history.add(record);
        currentGame = null;
    }

    public List<GameRecord> getHistory() {
        return history;
    }

    public int getTotalGames() {
        return history.size();
    }

    public int getMaxStreak() {
        int max = 0;
        for (GameRecord r : history) {
            if (r.getMaxStreak() > max) max = r.getMaxStreak();
        }
        return max;
    }

    public double getCorrectPercentage() {
        int totalQuestions = 0;
        int totalCorrect = 0;
        for (GameRecord r : history) {
            if (!r.isCancelled()) {
                totalQuestions += r.getTotalQuestions();
                totalCorrect += r.getCorrectAnswers();
            }
        }
        return totalQuestions == 0 ? 0 : (double) totalCorrect / totalQuestions * 100;
    }

    public double getAverageScore() {
        int totalScore = 0;
        int games = 0;
        for (GameRecord r : history) {
            if (!r.isCancelled()) {
                totalScore += r.getScore();
                games++;
            }
        }
        return games == 0 ? 0 : (double) totalScore / games;
    }
}
