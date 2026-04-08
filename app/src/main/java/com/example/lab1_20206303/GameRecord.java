package com.example.lab1_20206303;

import java.io.Serializable;
import java.util.Date;

public class GameRecord implements Serializable {
    private String playerName;
    private String difficulty;
    private Date startTime;
    private long durationMillis;
    private int hintsUsed;
    private int score;
    private int totalQuestions;
    private int correctAnswers;
    private int maxStreak;
    private boolean cancelled;
    private boolean inProgress;

    public GameRecord(String playerName, String difficulty, Date startTime, long durationMillis, int hintsUsed, int score, int totalQuestions, int correctAnswers, int maxStreak, boolean cancelled, boolean inProgress) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.startTime = startTime;
        this.durationMillis = durationMillis;
        this.hintsUsed = hintsUsed;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.maxStreak = maxStreak;
        this.cancelled = cancelled;
        this.inProgress = inProgress;
    }

    public String getPlayerName() { return playerName; }
    public String getDifficulty() { return difficulty; }
    public Date getStartTime() { return startTime; }
    public long getDurationMillis() { return durationMillis; }
    public int getHintsUsed() { return hintsUsed; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getMaxStreak() { return maxStreak; }
    public boolean isCancelled() { return cancelled; }
    public boolean isInProgress() { return inProgress; }
    public void setInProgress(boolean inProgress) { this.inProgress = inProgress; }
    public void setScore(int score) { this.score = score; }
    public void setHintsUsed(int hintsUsed) { this.hintsUsed = hintsUsed; }
    public void setDurationMillis(long durationMillis) { this.durationMillis = durationMillis; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
    public void setMaxStreak(int maxStreak) { this.maxStreak = maxStreak; }
}
