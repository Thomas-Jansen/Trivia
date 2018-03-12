package jansen.thomas.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class HighscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        Intent scoreIntent = getIntent();
        String score = String.valueOf(scoreIntent.getIntExtra("totalScore", 0));
        String name = scoreIntent.getStringExtra("name");
        TextView scoreView = findViewById(R.id.textViewTotalScore);
        scoreView.setText(score);
        TextView nameView = findViewById(R.id.textViewName);
        nameView.setText(name);
    }
}
