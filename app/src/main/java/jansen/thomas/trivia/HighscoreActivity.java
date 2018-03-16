package jansen.thomas.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity implements HighscoresHelper.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        Intent scoreIntent = getIntent();
        Highscore new_highscore = (Highscore) scoreIntent.getSerializableExtra("highscore");
        new HighscoresHelper(this, this).postNewHighScore(new_highscore);
        new HighscoresHelper(this, this).getHihgscores();
    }

    @Override
    public void gotHighscores(ArrayList<Highscore> highscores) {
        ListView highscoreView = findViewById(R.id.ListViewHighscores);
        HighscoresAdapter adapter = new HighscoresAdapter(this, R.layout.highscore_item, highscores);
        highscoreView.setAdapter(adapter);
    }

    @Override
    public void gotError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
