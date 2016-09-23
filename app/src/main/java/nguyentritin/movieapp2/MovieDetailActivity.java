package nguyentritin.movieapp2;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import nguyentritin.movieapp2.model.Movie;
import nguyentritin.movieapp2.util.MovieDatabaseHelper;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int position = getIntent().getExtras().getInt(EXTRA_MOVIE_POSITION);
        final Map<String, String> movie = ListMovieActivity.movieList.get(position);

        TextView titleView = (TextView) findViewById(R.id.detail_title);
        titleView.setText(movie.get("title"));

        TextView overviewView = (TextView) findViewById(R.id.detail_overview);
        overviewView.setText(movie.get("overview"));

        Button favorButton = (Button) findViewById(R.id.detail_favor_btn);
        favorButton.setText("Add To My Favorite");
        favorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MovieDatabaseHelper helper = new MovieDatabaseHelper(MovieDetailActivity.this);
                    helper.addMovie(movie.get("id"), movie.get("title"), movie.get("overview"));
                    Toast.makeText(getApplicationContext(), "Movie is added to your favorite list.", Toast.LENGTH_SHORT).show();
                } catch (SQLiteException e) {
                    Toast.makeText(MovieDetailActivity.this, "DB error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
