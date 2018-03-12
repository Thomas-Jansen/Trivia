package jansen.thomas.trivia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {

    TextView questionView;
    EditText answerView;
    TextView questionNumberView;
    Question currentQuestion;
    int questionCount;
    int totalScore;
    String givenAnswer;
    int givenAnswers = 0;
    Boolean getPoints = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent nextQuestionIntent = getIntent();
        questionCount = nextQuestionIntent.getIntExtra("questionCount", 1);
        totalScore = nextQuestionIntent.getIntExtra("totalScore", 0);
        if (questionCount == 6) {
            goToScore();
        }

        questionView = findViewById(R.id.textViewQuestion);
        answerView = findViewById(R.id.editTextAnswer);
        questionNumberView = findViewById(R.id.textViewQuestionNumber);
        questionNumberView.setText("Question " + questionCount);

        Button quitButton = findViewById(R.id.buttonQuit);
        quitButton.setOnClickListener(new onQuitClick());
        Button checkButton = findViewById(R.id.buttonCheckAnswer);
        checkButton.setOnClickListener(new onCheckAnswerClick());

        new TriviaHelper(getApplicationContext()).getNextQuestion(this);
    }

    @Override
    public void gotQuestion(ArrayList<Question> newQuestion) {
        currentQuestion = newQuestion.get(0);
        String questionString = currentQuestion.getQuestion();
        System.out.println(questionString);
        System.out.println(currentQuestion.getCorrectAnswer());
        questionView.setText(questionString);
    }

    @Override
    public void gotQuestionError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public class onQuitClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // AlertDialog method found on https://stackoverflow.com/questions/23195208/how-to-pop-up-
            // a-dialog-to-confirm-delete-when-user-long-press-on-the-list-item
            AlertDialog.Builder alertQuit = new AlertDialog.Builder(GameActivity.this);
            alertQuit.setMessage("Are you sure you want to quit?");
            alertQuit.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intentQuit = new Intent(GameActivity.this, MainActivity.class);
                    startActivity(intentQuit);
                }
            });

            alertQuit.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertQuit.show();
        }
    }

    public class onCheckAnswerClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            givenAnswer = String.valueOf(answerView.getText());
            String correctAnswer = currentQuestion.getCorrectAnswer();

            String message;
            if (givenAnswer.equalsIgnoreCase(correctAnswer)) {
                message = "Winner winner chicken dinner!";
                getPoints = true;
            }
            else {
                message = "Your answer is incorrect!";
                getPoints = false;
            }

            AlertDialog.Builder alertAnswer = new AlertDialog.Builder(GameActivity.this);
            alertAnswer.setMessage(message);
            alertAnswer.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    currentQuestion.setAnswers(String[0] = givenAnswer);
                    Intent intentNext = new Intent(GameActivity.this, GameActivity.class);
                    questionCount += 1;
                    if (getPoints) {
                        totalScore += currentQuestion.getMaxPoints();
                    }
                    intentNext.putExtra("questionCount", questionCount);
                    intentNext.putExtra("totalScore", totalScore);
                    startActivity(intentNext);
                }
            });

            if (givenAnswers < 3 && !getPoints) {
                alertAnswer.setNegativeButton("Retry", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        givenAnswers += 1;
                        totalScore -= 10;
                        dialog.dismiss();
                        answerView.setText("");
                    }
                });
            }
            alertAnswer.setCancelable(false);
            alertAnswer.show();
        }
    }

    @Override
    public void onBackPressed() {
        // Do nothing to prevent redoing questions
    }

    public void goToScore() {

        final String[] name = new String[1];

        final EditText nameTextEdit = new EditText(this);
        AlertDialog.Builder askForName = new AlertDialog.Builder(GameActivity.this);
        askForName.setView(nameTextEdit);
        askForName.setMessage("What's your name?");
        askForName.setCancelable(false);
        askForName.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                name[0] = String.valueOf(nameTextEdit.getText());

                Intent scoreIntent = new Intent(GameActivity.this, HighscoreActivity.class);
                scoreIntent.putExtra("totalScore", totalScore);
                scoreIntent.putExtra("name", name[0]);
                startActivity(scoreIntent);
            }
        });
        askForName.show();


    }
}
