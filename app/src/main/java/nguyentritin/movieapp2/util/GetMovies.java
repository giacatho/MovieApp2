package nguyentritin.movieapp2.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nguyentritin.movieapp2.ListMovieActivity;
import nguyentritin.movieapp2.adapter.MovieListAdapter;
import nguyentritin.movieapp2.model.Movie;

/**
 * Created by giacatho on 10/8/16.
 */
public class GetMovies extends AsyncTask<Void, Void, Void> {
    private final String TAG = "GetMovies";

    private String url;
    private GetMoviesDelegate delegate;

    private List<Map<String, String>> movieList = new ArrayList<>();
    private String errStr;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        delegate.onGetMoviesPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        HttpsHandler hander = new HttpsHandler();

        String jsonStr = hander.makeServiceCall(this.url);

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON array node
                JSONArray movies = jsonObj.getJSONArray("results");

                for (int i = 0; i < movies.length(); i++) {
                    JSONObject mv = movies.getJSONObject(i);

                    String id = mv.getString("id");
                    String title = mv.getString("title");
                    String originalTitle = mv.getString("original_title");
                    String overview = mv.getString("overview");
                    String posterPath = mv.getString("poster_path");

                    HashMap<String, String> movie = new HashMap<>();
                    movie.put("id", id);
                    movie.put("title", title);
                    movie.put("original_title", originalTitle);
                    movie.put("overview", overview);
                    movie.put("poster_path", posterPath);

                    movieList.add(movie);
                }
            } catch (final JSONException e) {
                errStr = "Json parsing error " + e.getMessage();
                Log.e(TAG, errStr);
            }
        } else {
            errStr = "Couldn't get json from server.";
            Log.e(TAG, errStr);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (errStr != null) {
            delegate.onGetMoviesError(errStr);
        } else {
            delegate.onGetMoviesSuccess(movieList);
        }
    }


    // Setters
    public void setUrl(String url) {
        this.url = url;
    }

    public void setDelegate(GetMoviesDelegate delegate) {
        this.delegate = delegate;
    }
}