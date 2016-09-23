package nguyentritin.movieapp2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import nguyentritin.movieapp2.util.Consts;
import nguyentritin.movieapp2.util.HttpsHandler;

public class ListMovieActivity extends AppCompatActivity {
    private String TAG = ListMovieActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;


    ArrayList<HashMap<String, String>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        movieList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
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

            String jsonStr = hander.makeServiceCall(Consts.UPCOMING_MOVIE_URL);

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

                        HashMap<String, String> movie = new HashMap<>();
                        movie.put("id", id);
                        movie.put("title", title);
                        movie.put("original_title", originalTitle);
                        movie.put("overview", overview);

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
            ListAdapter adapter = new SimpleAdapter(
                    ListMovieActivity.this,
                    movieList,
                    R.layout.list_movie_item,
                    new String[]{"title", "overview"},
                    new int[] {R.id.title, R.id.overview}
            );
            lv.setAdapter(adapter);

        }
    }
}
