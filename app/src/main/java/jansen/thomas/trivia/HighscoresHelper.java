package jansen.thomas.trivia;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

// HighscoreHelper posts high scores to the Firebase database and requests high scores.
public class HighscoresHelper implements ValueEventListener {

    private DatabaseReference databaseReference;
    private Callback activityHighscore;

    // Callback to HighscoreActivity
    public interface Callback {
        void gotHighscores(ArrayList<Highscore> highscores);
        void gotError(String message);
    }

    // Constructor.
    public HighscoresHelper(Callback activity) {
        activityHighscore = activity;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    // When received the high scores get name and score per child.
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        ArrayList<Highscore> scoresArray = new ArrayList<>();
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            HashMap hashMap = (HashMap) singleSnapshot.getValue();
            String name = (String) hashMap.get("name");
            Long score = (Long) hashMap.get("score");
            Highscore highscore = new Highscore(name, score);
            scoresArray.add(highscore);
        }

        // Reverse the order of Highscores to show highest score first.
        Collections.reverse(scoresArray);
        activityHighscore.gotHighscores(scoresArray);
    }

    // When received an error, send message to HighscoreActivity.
    @Override
    public void onCancelled(DatabaseError databaseError) {
        activityHighscore.gotError(databaseError.getMessage());
    }

    // Request high scores from the database, ordered by score.
    public void getHihgscores() {
        DatabaseReference ref = databaseReference.child("highscores");
        Query scoreQuery = ref.orderByChild("score");
        scoreQuery.addValueEventListener(this);
    }

    // Post new high score to the database. Use current dateTime to make an unique entry.
    public void postNewHighScore(Highscore highscore) {
        String currentTime = String.valueOf(Calendar.getInstance().getTime());
        databaseReference.child("highscores").child(currentTime).setValue(highscore);
    }
}
