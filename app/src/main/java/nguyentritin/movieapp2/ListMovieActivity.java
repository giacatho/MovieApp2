package nguyentritin.movieapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nguyentritin.movieapp2.adapter.MovieListAdapter;
import nguyentritin.movieapp2.util.GetMovies;
import nguyentritin.movieapp2.util.GetMoviesDelegate;

public class ListMovieActivity extends AppCompatActivity implements GetMoviesDelegate {
    private String TAG = ListMovieActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView listView;

    public static List<Map<String, String>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

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
        getMovies.setDelegate(this);
        getMovies.execute();
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

        this.movieList = movies;
        // Update ListView
        ListAdapter adapter = new MovieListAdapter(this, movieList);
        listView.setAdapter(adapter);
    }


}
