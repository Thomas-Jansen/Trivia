package jansen.thomas.trivia;

import java.io.Serializable;

// A class to hold the high scores.
public class Highscore implements Serializable{

    private String name;
    private Long score;

    public Highscore(String name, Long score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Long getScore() {
        return score;
    }
}
