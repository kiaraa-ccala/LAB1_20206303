package com.example.lab1_20206303;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionRepository {
    private static final List<Question> easyQuestions = Arrays.asList(
            new Question("¿Cuál es el objetivo principal del programa Artemis?", Arrays.asList("Llegar a Marte", "Volver a la Luna", "Explorar Júpiter", "Vivir en el Sol"), 1, "Fácil"),
            new Question("¿Qué agencia espacial lidera el programa Artemis?", Arrays.asList("ESA", "Roscosmos", "NASA", "SpaceX"), 2, "Fácil"),
            new Question("¿Cómo se llama la cápsula en la que viajarán los astronautas?", Arrays.asList("Apollo", "Orion", "Dragon", "Soyuz"), 1, "Fácil"),
            new Question("¿En qué año se lanzó la misión Artemis I?", Arrays.asList("2020", "2021", "2022", "2023"), 2, "Fácil"),
            new Question("¿Quién fue la diosa griega Artemis?", Arrays.asList("Diosa de la sabiduría", "Diosa de la Luna", "Diosa del Sol", "Diosa de la guerra"), 1, "Fácil")
    );

    private static final List<Question> hardQuestions = Arrays.asList(
            new Question("¿Cómo se llama el cohete más potente del mundo usado en Artemis?", Arrays.asList("Falcon Heavy", "Saturn V", "SLS (Space Launch System)", "Starship"), 2, "Difícil"),
            new Question("¿Qué región de la Luna es el objetivo del aterrizaje de Artemis III?", Arrays.asList("Mar de la Tranquilidad", "Polo Sur lunar", "Cara oculta", "Ecuador lunar"), 1, "Difícil"),
            new Question("¿Cómo se llama la futura estación espacial que orbitará la Luna?", Arrays.asList("ISS", "Tiangong", "Gateway", "Skylab"), 2, "Difícil"),
            new Question("¿Cuál es el nombre del maniquí que voló en Artemis I?", Arrays.asList("Moonikin Campos", "Starman", "Buzz", "Neil"), 0, "Difícil"),
            new Question("¿Qué empresa fue seleccionada para construir el sistema de aterrizaje humano (HLS)?", Arrays.asList("Blue Origin", "Boeing", "SpaceX", "Lockheed Martin"), 2, "Difícil")
    );

    public static List<Question> getQuestions(String difficulty) {
        List<Question> pool = new ArrayList<>();
        if (difficulty.equals("Fácil")) {
            pool.addAll(easyQuestions);
        } else if (difficulty.equals("Difícil")) {
            pool.addAll(hardQuestions);
        } else {
            pool.addAll(easyQuestions);
            pool.addAll(hardQuestions);
        }
        
        List<Question> randomizedPool = new ArrayList<>();
        for (Question q : pool) {
            // Re-shuffle for each game instance
            randomizedPool.add(new Question(q.getText(), q.getShuffledOptions(), q.getShuffledCorrectIndex(), q.getDifficulty()));
        }
        
        Collections.shuffle(randomizedPool);
        return randomizedPool.subList(0, Math.min(5, randomizedPool.size()));
    }
}
