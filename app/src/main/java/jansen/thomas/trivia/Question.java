package jansen.thomas.trivia;

import java.io.Serializable;

// A class to hold the question as an Object.
public class Question implements Serializable{

    private String question;
    private String correctAnswer;
    private String[] answers = new String[3];
    private int maxPoints;


    public Question(String aQuestion, String aCorrectAnswer, int aMaxPoints) {
        question = aQuestion;
        correctAnswer = aCorrectAnswer;
        maxPoints = aMaxPoints;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String answer, int i) {
        this.answers[i] = answer;
    }
}
