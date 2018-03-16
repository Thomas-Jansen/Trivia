package jansen.thomas.trivia;

import android.app.Activity;
import android.content.Context;

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

public class HighscoresHelper implements ValueEventListener {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private Context contextHighscore;
    private Activity activityHighscore;

    public interface Callback {
        void gotHighscores(ArrayList<Highscore> highscores);
        void gotError(String message);
    }

    public HighscoresHelper(Context context, Activity activity) {
        contextHighscore = context;
        activityHighscore = activity;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        ArrayList<Highscore> scoresArray = null;
        System.out.println("Data is aangekomen");
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            HashMap hashMap = (HashMap) singleSnapshot.getValue();
            String name = (String) hashMap.get("name");
            int score = (int) hashMap.get("score");
            Highscore highscore = new Highscore(name, score);
            scoresArray.add(highscore);
        }
        Collections.reverse(scoresArray);
//        activityHighscore.gotHighscores(ArrayList<Highscore> scoresArray);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        System.out.println("Er is een error");
//        activityHighscore.gotError(databaseError.getMessage());
    }

    public void getHihgscores() {
        DatabaseReference ref = databaseReference.child("highscores");
        Query scoreQuery = ref.orderByChild("score");
        scoreQuery.addValueEventListener(this);
    }

    public void postNewHighScore(Highscore highscore) {
        String currentTime = String.valueOf(Calendar.getInstance().getTime());
        databaseReference.child("highscores").child(currentTime).setValue(highscore);
    }
}
