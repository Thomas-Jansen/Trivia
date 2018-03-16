package jansen.thomas.trivia;

import java.io.Serializable;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
