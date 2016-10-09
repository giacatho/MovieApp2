package nguyentritin.movieapp2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import nguyentritin.movieapp2.adapter.MovieItemCursorAdapter;
import nguyentritin.movieapp2.util.Consts;
import nguyentritin.movieapp2.util.MovieDatabaseHelper;

// http://www.tutorialsbuzz.com/2013/11/android-sqlite-database-with.html
public class ListFavoriteMovieActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor favoriteCursor;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favorite_movies);

        listView = (ListView) findViewById(R.id.list_movies);

        // When clicking the row, call MovieDetailActivity with the Movie info.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = ((CursorAdapter)listView.getAdapter()).getCursor();
                cursor.moveToPosition(position);

                String movieId = cursor.getString(cursor.getColumnIndex(Consts.DB_COL_MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(Consts.DB_COL_TITLE));
                String overview = cursor.getString(cursor.getColumnIndex(Consts.DB_COL_OVERVIEW));
                String posterPath = cursor.getString(cursor.getColumnIndex(Consts.DB_COL_POSTER_PATH));

                Bundle movie = new Bundle();
                movie.putString(Consts.DB_COL_MOVIE_ID, movieId);
                movie.putString(Consts.DB_COL_TITLE, title);
                movie.putString(Consts.DB_COL_OVERVIEW, overview);
                movie.putString(Consts.DB_COL_POSTER_PATH, posterPath);

                Intent intent = new Intent(ListFavoriteMovieActivity.this, MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_FROM_ACTIVITY, ListFavoriteMovieActivity.class.getSimpleName());
                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
                startActivity(intent);
            }
        });

        try {
            SQLiteOpenHelper dbHelper = new MovieDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();
            favoriteCursor = db.query(Consts.DB_TBL_NAME,
                    new String[] {"_id", Consts.DB_COL_MOVIE_ID, Consts.DB_COL_TITLE, Consts.DB_COL_OVERVIEW, Consts.DB_COL_POSTER_PATH},
                    null,
                    null, null, null, null);

            CursorAdapter favoriteAdapter = new MovieItemCursorAdapter(this, favoriteCursor, 0);

            listView.setAdapter(favoriteAdapter);

        } catch (SQLiteException e) {
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (favoriteCursor != null)
            favoriteCursor.close();
        if (db != null)
            db.close();
    }
}
