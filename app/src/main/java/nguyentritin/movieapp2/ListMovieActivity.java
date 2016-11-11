package nguyentritin.movieapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Map;

import nguyentritin.movieapp2.adapter.MovieGridAdapter;
import nguyentritin.movieapp2.adapter.MovieListAdapter;
import nguyentritin.movieapp2.util.GetMovies;
import nguyentritin.movieapp2.util.GetMoviesDelegate;
import nguyentritin.movieapp2.util.Utils;

/**
 * Created by giacatho on 10/1/16.
 * Concept learned from: Head First Android books and multiple places
 */
public class ListMovieActivity extends AppCompatActivity implements GetMoviesDelegate {
    private String TAG = ListMovieActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private TextView statusTextView;
    private ListView listView;
    private GridView gridView;

    public static List<Map<String, String>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

        statusTextView = (TextView) findViewById(R.id.status);
        gridView = (GridView) findViewById(R.id.gridView);
        listView = (ListView) findViewById(R.id.list_movies);

        if (!Utils.isOnline(this)) {
            statusTextView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);

            return;
        }

        statusTextView.setVisibility(View.GONE);
        if (isDisplayAsGrid()) {
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    callMovieDetailActivity(id);
                }
            });

            listView.setVisibility(View.GONE);
        } else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    callMovieDetailActivity(id);
                }
            });

            gridView.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        GetMovies getMovies = new GetMovies();
        getMovies.setUrl(intent.getStringExtra("url"));
        getMovies.setDelegate(this);
        getMovies.execute();
    }

    private void callMovieDetailActivity(long id) {
        Intent intent = new Intent(ListMovieActivity.this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_FROM_ACTIVITY, ListMovieActivity.class.getSimpleName());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_POSITION, (int) id);
        startActivity(intent);
    }

    private boolean isDisplayAsGrid() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        return sharedPrefs.getBoolean("prefDisplayMoviesAsGrid", false);
    }

    @Override
    public void onGetMoviesPreExecute() {
        // Showing progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onGetMoviesError(String errStr) {
        if (pDialog.isShowing())
            pDialog.dismiss();

        Toast.makeText(getApplicationContext(),
                errStr,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetMoviesSuccess(List<Map<String, String>> movies) {
        if (pDialog.isShowing())
            pDialog.dismiss();

        movieList = movies;

        // Update ListView or GridView
        if (isDisplayAsGrid()) {
            MovieGridAdapter adapter = new MovieGridAdapter(this, movieList);
            gridView.setAdapter(adapter);
        } else {
            ListAdapter adapter = new MovieListAdapter(this, movieList);
            listView.setAdapter(adapter);
        }
    }


}
