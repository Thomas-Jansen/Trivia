package jansen.thomas.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

// Shows the high scores as a leader board in a listview.
public class HighscoreActivity extends AppCompatActivity implements HighscoresHelper.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // Get new highscore from GameActivity and send it to HighscoreHelper to add it to the database.
        Intent scoreIntent = getIntent();
        Highscore new_highscore = (Highscore) scoreIntent.getSerializableExtra("highscore");
        new HighscoresHelper(this).postNewHighScore(new_highscore);

        // Get all high scores to show in the listview.
        new HighscoresHelper(this).getHihgscores();
    }

    // When the high scores have arrived, create a adapter to set them in the listView.
    @Override
    public void gotHighscores(ArrayList<Highscore> highscores) {
        ListView highscoreView = findViewById(R.id.ListViewHighscores);
        HighscoresAdapter adapter = new HighscoresAdapter(this, R.layout.highscore_item, highscores);
        highscoreView.setAdapter(adapter);
    }

    // When received an error, show the message to the user.
    @Override
    public void gotError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
