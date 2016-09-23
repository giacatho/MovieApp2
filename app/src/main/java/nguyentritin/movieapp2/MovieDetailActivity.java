package nguyentritin.movieapp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.widget.TextView;

import java.util.Map;

import nguyentritin.movieapp2.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int position = getIntent().getExtras().getInt(EXTRA_MOVIE_POSITION);
        Map<String, String> movie = ListMovieActivity.movieList.get(position);

        TextView titleView = (TextView) findViewById(R.id.detail_title);
        titleView.setText(movie.get("title"));

        TextView overviewView = (TextView) findViewById(R.id.detail_overview);
        overviewView.setText(movie.get("overview"));
    }
}
