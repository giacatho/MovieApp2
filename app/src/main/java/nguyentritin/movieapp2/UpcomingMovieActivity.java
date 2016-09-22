package nguyentritin.movieapp2;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import nguyentritin.movieapp2.model.Movie;
import nguyentritin.movieapp2.util.Consts;
import nguyentritin.movieapp2.util.MovieDBRequest;
import nguyentritin.movieapp2.util.MovieDBRequestDelegate;


public class UpcomingMovieActivity extends ListActivity implements MovieDBRequestDelegate {
    public static final String TAG = UpcomingMovieActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<Movie> listAdapter = new ArrayAdapter<Movie>(
                this,
                android.R.layout.simple_list_item_1,
                Movie.movies
        );

        ListView listView = getListView();
        listView.setAdapter(listAdapter);

        MovieDBRequest request = new MovieDBRequest();
        request.setUrlString(Consts.UPCOMING_MOVIE_URL);
        request.setDelegate(this);

        request.execute();
    }

    @Override
    public void onMovieDBRequestCompleted(MovieDBRequest request, int status, String content) {
        Log.i(TAG, "Status: " + status + ". " + content);
    }
}
