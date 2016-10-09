package nguyentritin.movieapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nguyentritin.movieapp2.adapter.MovieGridAdapter;
import nguyentritin.movieapp2.adapter.MovieItemCursorAdapter;
import nguyentritin.movieapp2.adapter.MovieListAdapter;
import nguyentritin.movieapp2.model.Movie;
import nguyentritin.movieapp2.util.Consts;
import nguyentritin.movieapp2.util.GetMovieDetail;
import nguyentritin.movieapp2.util.GetMovieDetailDelegate;
import nguyentritin.movieapp2.util.GetMoviesDelegate;
import nguyentritin.movieapp2.util.HttpsHandler;
import nguyentritin.movieapp2.util.MovieDatabaseHelper;

public class MovieDetailActivity extends AppCompatActivity implements GetMovieDetailDelegate {
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

        // Movie information
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(movie.get(Consts.DB_COL_TITLE));

        TextView overviewView = (TextView) findViewById(R.id.overview);
        overviewView.setText(movie.get(Consts.DB_COL_OVERVIEW));

        // Favorite button
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

        // Search button
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

        // Start to call for more movie detail information
        GetMovieDetail getMovieDetail = new GetMovieDetail();
        getMovieDetail.setDelegate(this);
        getMovieDetail.setUrl(String.format(Consts.URL_MOVIE_DETAIL, movie.get(Consts.DB_COL_MOVIE_ID)));
        getMovieDetail.execute();
    }

    private Map<String, String> getMovieFromBundle(Bundle bundle) {
        Map<String, String> movie = new HashMap<>();

        Set<String> keys = bundle.keySet();
        for (String key: keys) {
            movie.put(key, bundle.getString(key));
        }

        return movie;
    }

    @Override
    public void onGetMovieDetailPreExecute() {
        // Work under silence
    }

    @Override
    public void onGetMovieDetailError(String errStr) {
        Toast.makeText(this,
                errStr,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetMovieDetailSuccess(Map<String, String> movie) {
        // More movie information
        TextView taglineView = (TextView) findViewById(R.id.tagline);
        taglineView.setVisibility(View.VISIBLE);
        taglineView.setText(movie.get(Consts.K_TAGLINE));

        View releaseDateGroup = findViewById(R.id.release_date_group);
        releaseDateGroup.setVisibility(View.VISIBLE);
        TextView releaseDateView = (TextView) findViewById(R.id.release_date);
        releaseDateView.setText(movie.get(Consts.K_RELEASE_DATE));

        View ratingGroup = findViewById(R.id.rating_group);
        ratingGroup.setVisibility(View.VISIBLE);
        TextView ratingView = (TextView) findViewById(R.id.rating);
        ratingView.setText(movie.get(Consts.K_RATING));

        View genresGroup = findViewById(R.id.genres_group);
        genresGroup.setVisibility(View.VISIBLE);
        TextView genresView = (TextView) findViewById(R.id.genres);
        genresView.setText(movie.get(Consts.K_GENRES));

        View productCountriesGroup = findViewById(R.id.production_countries_group);
        productCountriesGroup.setVisibility(View.VISIBLE);
        TextView productionCountriesView = (TextView) findViewById(R.id.production_countries);
        productionCountriesView.setText(movie.get(Consts.K_PRODUCTION_COUNTRIES));
    }
}
