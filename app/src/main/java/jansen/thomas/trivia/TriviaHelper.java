package jansen.thomas.trivia;

import android.content.Context;
import android.text.Html;

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

public class TriviaHelper implements Response.Listener<JSONArray>, Response.ErrorListener  {

    private Context contextGame;
    private Callback activityGame;

    // Callback interface
    public interface Callback {
        void gotQuestion(ArrayList<Question> categories);
        void gotQuestionError(String message);
    }

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
            question = Html.escapeHtml(objectQuestion.getString("question"));
            answer = Html.escapeHtml(objectQuestion.getString("answer"));
            maxScore = objectQuestion.getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
            activityGame.gotQuestionError("Retry getting question");
            getNextQuestion(activityGame);
            return;
        }
        Question newQuestion = new Question(question, answer, maxScore);
        ArrayList<Question> newQuestionList  = new ArrayList<Question>();
        newQuestionList.add(newQuestion);
        activityGame.gotQuestion(newQuestionList);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        activityGame.gotQuestionError(error.getMessage());
    }

    public TriviaHelper(Context context) {
        contextGame = context;
    }

    public void getNextQuestion(Callback activity) {
        activityGame = activity;
        String url = "http://jservice.io/api/random";

        RequestQueue queue = Volley.newRequestQueue(contextGame);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, this, this);
        queue.add(jsonArrayRequest);
    }
}
