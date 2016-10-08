package nguyentritin.movieapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import nguyentritin.movieapp2.adapter.MovieGridAdapter;
import nguyentritin.movieapp2.adapter.MovieListAdapter;
import nguyentritin.movieapp2.util.GetMovies;
import nguyentritin.movieapp2.util.GetMoviesDelegate;

public class GridMovieActivity extends AppCompatActivity implements GetMoviesDelegate {
    private ProgressDialog pDialog;
    private GridView gridView;

    public static List<Map<String, String>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_movies);

        gridView = (GridView) findViewById(R.id.gridView);

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
        ListAdapter adapter = new MovieGridAdapter(this, movieList);
        gridView.setAdapter(adapter);
    }
}
