package nguyentritin.movieapp2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
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
    private Map<String, String> movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int position = getIntent().getExtras().getInt(EXTRA_MOVIE_POSITION);
        movie = ListMovieActivity.movieList.get(position);

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(movie.get("title"));

        TextView overviewView = (TextView) findViewById(R.id.overview);
        overviewView.setText(movie.get("overview"));

        Button favorButton = (Button) findViewById(R.id.detail_favor_btn);
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
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.youtube.com/results?search_query=test+me"));
                        startActivity(browserIntent);
                    } catch (Exception e1) {
                        Toast.makeText(MovieDetailActivity.this, "Your phone does not support this feature.", Toast.LENGTH_LONG);
                    }
                }
            }
        });
    }
}
