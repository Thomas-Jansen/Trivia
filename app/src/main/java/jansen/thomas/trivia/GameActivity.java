package jansen.thomas.trivia;

import android.annotation.SuppressLint;
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

// Handles getting the questions, giving hints, returning to main and checking your answers.
public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {

    // Global variables.
    TextView questionView;
    EditText answerView;
    TextView questionNumberView;
    Question currentQuestion;
    int questionCount;
    Long totalScore;
    String givenAnswer;
    int givenAnswers = 0;
    Boolean getPoints = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get intent from MainActivity, or from previous question.
        // If intent is empty, it was from MainActivity and every value should be set to default.
        Intent nextQuestionIntent = getIntent();
        questionCount = nextQuestionIntent.getIntExtra("questionCount", 1);
        totalScore = nextQuestionIntent.getLongExtra("totalScore", 0);

        // If 5 questions have been answered, set layout to an empty one
        // and handle the rest in goToScore().
        if (questionCount == 6) {
            @SuppressLint("InflateParams") View emptyLayout = getLayoutInflater()
                    .inflate(R.layout.empty_layout, null);
            setContentView(emptyLayout);
            goToScore();
            return;
        }

        // Different views and buttons with onClickListeners.
        questionView = findViewById(R.id.textViewQuestion);
        answerView = findViewById(R.id.editTextAnswer);
        questionNumberView = findViewById(R.id.textViewQuestionNumber);
        questionNumberView.setText("Question " + questionCount);

        Button quitButton = findViewById(R.id.buttonQuit);
        quitButton.setOnClickListener(new onQuitClick());
        Button checkButton = findViewById(R.id.buttonCheckAnswer);
        checkButton.setOnClickListener(new onCheckAnswerClick());
        Button hintButton = findViewById(R.id.buttonGetHint);
        hintButton.setOnClickListener(new onHintClick());

        // Request a random question from TriviaHelper.
        new TriviaHelper(getApplicationContext()).getNextQuestion(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("currentQuestion", currentQuestion);
        outState.putInt("questionCount", questionCount);
        outState.putLong("totalScore", totalScore);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {

        currentQuestion = (Question) inState.getSerializable("currentQuestion");
        questionCount = inState.getInt("questionCount");
        totalScore = inState.getLong("totalScore");
    }

    // When the new question has arrived, show it in questionView.
    @Override
    public void gotQuestion(ArrayList<Question> newQuestion) {
        currentQuestion = newQuestion.get(0);
        String questionString = currentQuestion.getQuestion();
        System.out.println(questionString);
        System.out.println(currentQuestion.getCorrectAnswer());
        questionView.setText(questionString);
    }

    // When an error has returned, show the message.
    @Override
    public void gotQuestionError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // When the player clicks on Quit, ask for confirmation and return to MainActivity.
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

    // When the player asks for a hint, show the first character of the answer and deduct 100 points
    // from totalScore.
    public class onHintClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            totalScore -= 100;
            String hint = String.valueOf(currentQuestion.getCorrectAnswer().charAt(0));
            Toast.makeText(GameActivity.this, hint, Toast.LENGTH_LONG).show();
        }
    }

    // When the player checks his/her answer
    public class onCheckAnswerClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            givenAnswer = String.valueOf(answerView.getText());
            String correctAnswer = currentQuestion.getCorrectAnswer();

            // Check if the two answers are identical and set message accordingly.
            String message;
            if (givenAnswer.equalsIgnoreCase(correctAnswer)) {
                message = "Winner winner chicken dinner!";
                getPoints = true;
            }
            else {
                message = "Your answer is incorrect!";
                getPoints = false;
            }

            // Show alertDialog with message and ask to continue or retry.
            AlertDialog.Builder alertAnswer = new AlertDialog.Builder(GameActivity.this);
            alertAnswer.setMessage(message);
            alertAnswer.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currentQuestion.setAnswers(givenAnswer, givenAnswers);
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

            // Only show retry option if the answer was incorrect and retry count is less than 3.
            if (givenAnswers < 2 && !getPoints) {
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

            // Prevent user from cancelling.
            alertAnswer.setCancelable(false);
            alertAnswer.show();
        }
    }

    @Override
    public void onBackPressed() {
        // Do nothing to prevent redoing questions
    }

    // Method to get users name, calculate score and create a Highscore object.
    public void goToScore() {

        final String[] name = new String[1];
        final EditText nameTextEdit = new EditText(this);

        // Create alertDialog with a textEdit for user to give his/her name.
        AlertDialog.Builder askForName = new AlertDialog.Builder(GameActivity.this);
        askForName.setView(nameTextEdit);
        askForName.setMessage("What's your name?");
        askForName.setCancelable(false);
        askForName.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                name[0] = String.valueOf(nameTextEdit.getText());

                // Create an intent to HighscoreActivity and include Highscore object.
                Highscore new_highscore = new Highscore(name[0], totalScore);
                Intent scoreIntent = new Intent(GameActivity.this, HighscoreActivity.class);
                scoreIntent.putExtra("highscore", new_highscore);
                startActivity(scoreIntent);
            }
        });
        askForName.show();
    }
}
