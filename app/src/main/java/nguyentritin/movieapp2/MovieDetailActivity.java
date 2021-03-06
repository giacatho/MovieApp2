package nguyentritin.movieapp2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nguyentritin.movieapp2.util.Consts;
import nguyentritin.movieapp2.util.GetMovieDetail;
import nguyentritin.movieapp2.util.GetMovieDetailDelegate;
import nguyentritin.movieapp2.util.MovieDatabaseHelper;
import nguyentritin.movieapp2.util.Utils;

/**
 * Created by giacatho on 10/7/16.
 * Concept learned from: Head First Android books and multiple places
 */
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
        if (ListMovieActivity.class.getSimpleName().equals(fromActivity)) {
            int position = getIntent().getExtras().getInt(EXTRA_MOVIE_POSITION);
            movie = ListMovieActivity.movieList.get(position);
        } else if (ListFavoriteMovieActivity.class.getSimpleName().equals(fromActivity)) {
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

        String posterPath = movie.get(Consts.DB_COL_POSTER_PATH);
        ImageView posterImageView = (ImageView) findViewById(R.id.poster);
        if (posterPath == null || posterPath.equals("null")) {
            posterImageView.setImageResource(R.mipmap.default_poster);
        } else {
            Glide.with(this).load(Consts.POSTER_ROOT + posterPath).into(posterImageView);
        }

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

                    // Hide the favorButton, using animation, technique learned from:
                    // https://www.codementor.io/tips/7812274333/android-adding-simple-animations-while-setvisibility-view-gone
                    // http://stackoverflow.com/a/19766034
                    favorButton.animate()
                        .translationY(favorButton.getHeight())
                        .alpha(0.0f)
                        .setDuration(1500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                favorButton.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "This movie has been successfully added to your favorite list.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

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
                // Using object animator to create the bouncing animation
                // Concept learned fromm https://developer.android.com/reference/android/animation/ObjectAnimator.html
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -100f, 0f);
                animator.setDuration(1000);
                animator.setInterpolator(new BounceInterpolator());
                animator.setRepeatCount(2);
                animator.start();

                // Then, at the same time, trigger the trailer search
                String query = movie.get("title") + " trailer official";
                try {
                    // Try open the Youtube app, technique learned from:
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

        if (Utils.isOnline(this) &&
                (isShowReleasedDate() || isShowRating() || isShowGenres() || isShowCountry())) {
            // Start to call for more movie detail information
            GetMovieDetail getMovieDetail = new GetMovieDetail();
            getMovieDetail.setDelegate(this);
            getMovieDetail.setUrl(String.format(Consts.URL_MOVIE_DETAIL, movie.get(Consts.DB_COL_MOVIE_ID)));
            getMovieDetail.execute();
        }
    }

    private Map<String, String> getMovieFromBundle(Bundle bundle) {
        Map<String, String> movie = new HashMap<>();

        Set<String> keys = bundle.keySet();
        for (String key : keys) {
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

        if (isShowReleasedDate()) {
            View releaseDateGroup = findViewById(R.id.release_date_group);
            releaseDateGroup.setVisibility(View.VISIBLE);
            TextView releaseDateView = (TextView) findViewById(R.id.release_date);
            releaseDateView.setText(Utils.getSingaporeDateFormat(movie.get(Consts.K_RELEASE_DATE)));
        }

        if (isShowRating()) {
            View ratingGroup = findViewById(R.id.rating_group);
            ratingGroup.setVisibility(View.VISIBLE);
            TextView ratingView = (TextView) findViewById(R.id.rating);
            ratingView.setText("0.0".equals(movie.get(Consts.K_RATING)) ? "N.A" : movie.get(Consts.K_RATING));
        }

        if (isShowGenres()) {
            View genresGroup = findViewById(R.id.genres_group);
            genresGroup.setVisibility(View.VISIBLE);
            TextView genresView = (TextView) findViewById(R.id.genres);
            genresView.setText(movie.get(Consts.K_GENRES));
        }

        if (isShowCountry()) {
            View productCountriesGroup = findViewById(R.id.production_countries_group);
            productCountriesGroup.setVisibility(View.VISIBLE);
            TextView productionCountriesView = (TextView) findViewById(R.id.production_countries);
            productionCountriesView.setText(movie.get(Consts.K_PRODUCTION_COUNTRIES));
        }
    }


    private boolean isShowReleasedDate() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        return sharedPrefs.getBoolean("prefShowReleasedDate", true);
    }

    private boolean isShowRating() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        return sharedPrefs.getBoolean("prefShowRating", true);
    }

    private boolean isShowGenres() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        return sharedPrefs.getBoolean("prefShowGenres", true);
    }

    private boolean isShowCountry() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        return sharedPrefs.getBoolean("prefShowCountry", true);
    }

}
