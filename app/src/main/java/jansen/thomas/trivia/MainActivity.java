package jansen.thomas.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonNewGame.setOnClickListener(new onNewGameClick());

        Button buttonLeaderBoard = findViewById(R.id.buttonLeaderBoard);
        buttonLeaderBoard.setOnClickListener(new onLeaderBoardClick());
    }

    public class onNewGameClick implements View.OnClickListener {
        public void onClick(View view) {
            Intent intentNewGame = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intentNewGame);
        }
    }

    public class onLeaderBoardClick implements View.OnClickListener {
        public void onClick(View view) {
            Intent intentLeaderBoard = new Intent(MainActivity.this, HighscoreActivity.class);
            startActivity(intentLeaderBoard);
        }
    }
}
