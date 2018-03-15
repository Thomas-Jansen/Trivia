package jansen.thomas.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity implements HighscoresHelper.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        Intent scoreIntent = getIntent();
        Highscore new_highscore = (Highscore) scoreIntent.getSerializableExtra("highscore");

        new HighscoresHelper(getApplicationContext()).postNewHighScore(new_highscore);
    }

    @Override
    public void gotHighscores(ArrayList<Highscore> highscores) {

    }

    @Override
    public void gotError(String message) {

    }
}
