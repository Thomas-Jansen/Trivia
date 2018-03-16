package jansen.thomas.trivia;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// A class to get new questions, with response listeners.
public class TriviaHelper implements Response.Listener<JSONArray>, Response.ErrorListener  {

    private Context contextGame;
    private Callback activityGame;

    // Callback interface
    public interface Callback {
        void gotQuestion(ArrayList<Question> categories);
        void gotQuestionError(String message);
    }

    // Receive a question response as a JSONArray.
    @Override
    public void onResponse(JSONArray response) {
        String question = "";
        String answer = "";
        int maxScore = 0;
        JSONObject objectQuestion = null;
        try {
           objectQuestion = response.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
            activityGame.gotQuestionError("Something went wrong");
        }
        try {

            // Get question, answer and maxScore from the object.
            question = objectQuestion.getString("question");
            answer = objectQuestion.getString("answer");
            maxScore = objectQuestion.getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();

            // When an error has occured (one of the values is missing) get a new question.
            activityGame.gotQuestionError("Retry getting question");
            getNextQuestion(activityGame);
            return;
        }

        // Send the question to gotQuestion in GameActivity.
        Question newQuestion = new Question(question, answer, maxScore);
        ArrayList<Question> newQuestionList  = new ArrayList<Question>();
        newQuestionList.add(newQuestion);
        activityGame.gotQuestion(newQuestionList);
    }

    // When received an error, send error to gotError in GameActivity.
    @Override
    public void onErrorResponse(VolleyError error) {
        activityGame.gotQuestionError(error.getMessage());
    }

    // Constructor.
    public TriviaHelper(Context context) {
        contextGame = context;
    }

    // Sends a JsonArrayRequest to the url.
    public void getNextQuestion(Callback activity) {
        activityGame = activity;
        String url = "http://jservice.io/api/random";

        RequestQueue queue = Volley.newRequestQueue(contextGame);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, this, this);
        queue.add(jsonArrayRequest);
    }
}
