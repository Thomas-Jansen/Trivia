package jansen.thomas.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HighscoresAdapter extends ArrayAdapter<Highscore>  {

    ArrayList<Highscore> highscoreArrayList;
    Context contextHighscore;

    public HighscoresAdapter(@NonNull Context context, int resource, ArrayList<Highscore> objects ) {
        super(context, resource, objects);
        highscoreArrayList = objects;
        contextHighscore = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.highscore_item, parent, false);
        }

        Highscore highscore = highscoreArrayList.get(position);

        String name = highscore.getName();
        int score = highscore.getScore();

        TextView nameView = convertView.findViewById(R.id.textViewName);
        TextView scoreView = convertView.findViewById(R.id.textViewScore);
        TextView numberView = convertView.findViewById(R.id.textViewNumber);

        nameView.setText(name);
        scoreView.setText(score);
        numberView.setText(position+1);

        return convertView;
    }
}
