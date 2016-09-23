package nguyentritin.movieapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import nguyentritin.movieapp2.adapter.MovieListAdapter;
import nguyentritin.movieapp2.util.HttpsHandler;

public class ListMovieActivity extends AppCompatActivity {
    private String TAG = ListMovieActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView listView;

    public static ArrayList<HashMap<String, String>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

        movieList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_movies);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ListMovieActivity.this, MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_POSITION, (int) id);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        GetMovies getMovies = new GetMovies();
        getMovies.setUrl(intent.getStringExtra("url"));
        getMovies.execute();
    }

    private class GetMovies extends AsyncTask<Void, Void, Void> {
        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress dialog
            pDialog = new ProgressDialog(ListMovieActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
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
                    Log.e(TAG, "Json parsing error " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            // Update ListView
            ListAdapter adapter = new MovieListAdapter(ListMovieActivity.this, movieList);
            listView.setAdapter(adapter);

        }


        // Setters
        public void setUrl(String url) {
            this.url = url;
        }
    }
}
