package jansen.thomas.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

// Starting activity, choose to start a new game, or view leader board.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buttons and their onClickListeners.
        Button buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonNewGame.setOnClickListener(new onNewGameClick());
        Button buttonLeaderBoard = findViewById(R.id.buttonLeaderBoard);
        buttonLeaderBoard.setOnClickListener(new onLeaderBoardClick());
    }

    // Start a new game by sending an intent to GameActivity.
    public class onNewGameClick implements View.OnClickListener {
        public void onClick(View view) {
            Intent intentNewGame = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intentNewGame);
        }
    }

    // Show loader board with high scores by sending an intent to HighscoreActivity
    public class onLeaderBoardClick implements View.OnClickListener {
        public void onClick(View view) {
            Intent intentLeaderBoard = new Intent(MainActivity.this, HighscoreActivity.class);
            startActivity(intentLeaderBoard);
        }
    }
}
