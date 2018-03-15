package jansen.thomas.trivia;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HighscoresHelper implements ValueEventListener {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Context contextHighscore;
    Activity activityHighscore;

    public interface Callback {
        void gotHighscores(ArrayList<Highscore> highscores);
        void gotError(String message);
    }

    public HighscoresHelper(Context context) {
        contextHighscore = context;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
//        activityHighscore.gotError(databaseError.getMessage());
    }

    public void getHihgscores() {
//        database.

    }

    public void postNewHighScore(Highscore highscore) {
        String name = highscore.getName();
        int score = highscore.getScore();
        databaseReference.child(name).child("score").setValue(score);
    }
}
