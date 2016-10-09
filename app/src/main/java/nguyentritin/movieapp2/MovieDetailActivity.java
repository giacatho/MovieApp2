package nguyentritin.movieapp2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nguyentritin.movieapp2.adapter.MovieItemCursorAdapter;
import nguyentritin.movieapp2.model.Movie;
import nguyentritin.movieapp2.util.Consts;
import nguyentritin.movieapp2.util.MovieDatabaseHelper;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_FROM_ACTIVITY = "from_activity";
    public static final String EXTRA_MOVIE_POSITION = "position";
    public static final String EXTRA_MOVIE = "movie";

    MovieDatabaseHelper movieDatabaseHelper;
    private Map<String, String> movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieDatabaseHelper = new MovieDatabaseHelper(this);

        String fromActivity = getIntent().getExtras().getString(EXTRA_FROM_ACTIVITY);
        if (ListMovieActivity.class.getSimpleName().equals(fromActivity)){
            int position = getIntent().getExtras().getInt(EXTRA_MOVIE_POSITION);
            movie = ListMovieActivity.movieList.get(position);
        } else if (ListFavoriteMovieActivity.class.getSimpleName().equals(fromActivity)){
            Bundle bundle = getIntent().getExtras().getBundle(EXTRA_MOVIE);
            movie = getMovieFromBundle(bundle);
        } else {
            Toast.makeText(this, "Wrong state. This activity should be called from others.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(movie.get(Consts.DB_COL_TITLE));

        TextView overviewView = (TextView) findViewById(R.id.overview);
        overviewView.setText(movie.get(Consts.DB_COL_OVERVIEW));

        final Button favorButton = (Button) findViewById(R.id.detail_favor_btn);
        favorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    movieDatabaseHelper.addMovie(movie.get(Consts.DB_COL_MOVIE_ID),
                            movie.get(Consts.DB_COL_TITLE),
                            movie.get(Consts.DB_COL_OVERVIEW),
                            movie.get(Consts.DB_COL_POSTER_PATH));
                    favorButton.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "This movie has been successfully added to your favorite list.",
                            Toast.LENGTH_SHORT).show();
                } catch (SQLiteException e) {
                    Toast.makeText(MovieDetailActivity.this, "DB error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Button searchTrailerButton = (Button) findViewById(R.id.search_trailer_btn);
        searchTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = movie.get("title") + " trailer official";
                try {
                    // Try open the Youtube app
                    // http://stackoverflow.com/questions/9860456/search-a-specific-string-in-youtube-application-from-my-app
                    Intent intent = new Intent(Intent.ACTION_SEARCH);
                    intent.setPackage("com.google.android.youtube");
                    intent.putExtra("query", movie.get("title") + " trailer official");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    // Youtube has fail, now try to open a Browser
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "https://m.youtube.com/results?search_query=" +
                                        URLEncoder.encode(query, "utf-8")));
                        startActivity(browserIntent);
                    } catch (Exception e1) {
                        Toast.makeText(MovieDetailActivity.this,
                                "Your phone does not support this feature.", Toast.LENGTH_LONG);
                    }
                }
            }
        });

        // Display Add To Favorite Button if this movie is not in the database
        if (!movieDatabaseHelper.isFavoriteMovie(movie.get(Consts.DB_COL_MOVIE_ID))) {
            favorButton.setVisibility(View.VISIBLE);
        }
    }

    private Map<String, String> getMovieFromBundle(Bundle bundle) {
        Map<String, String> movie = new HashMap<>();

        Set<String> keys = bundle.keySet();
        for (String key: keys) {
            movie.put(key, bundle.getString(key));
        }

        return movie;
    }
}
