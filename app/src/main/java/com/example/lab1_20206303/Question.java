package com.example.lab1_20206303;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question implements Serializable {
    private String text;
    private List<String> options;
    private int correctOptionIndex;
    private String difficulty;
    private List<String> shuffledOptions;
    private int shuffledCorrectIndex;

    public Question(String text, List<String> options, int correctOptionIndex, String difficulty) {
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.difficulty = difficulty;
        shuffleOptions();
    }

    private void shuffleOptions() {
        String correctOption = options.get(correctOptionIndex);
        shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions);
        shuffledCorrectIndex = shuffledOptions.indexOf(correctOption);
    }

    public String getText() { return text; }
    public List<String> getShuffledOptions() { return shuffledOptions; }
    public int getShuffledCorrectIndex() { return shuffledCorrectIndex; }
    public String getDifficulty() { return difficulty; }
}
