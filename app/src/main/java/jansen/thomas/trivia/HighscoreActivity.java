package jansen.thomas.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity implements HighscoresHelper.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        Intent scoreIntent = getIntent();
        Highscore new_highscore = (Highscore) scoreIntent.getSerializableExtra("highscore");
        new HighscoresHelper(getApplicationContext(), this).postNewHighScore(new_highscore);
        new HighscoresHelper(getApplicationContext(), this).getHihgscores();
    }

    @Override
    public void gotHighscores(ArrayList<Highscore> highscores) {

    }

    @Override
    public void gotError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
