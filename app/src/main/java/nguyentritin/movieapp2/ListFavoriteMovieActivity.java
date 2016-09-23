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

import nguyentritin.movieapp2.util.MovieDatabaseHelper;

public class ListFavoriteMovieActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor favoriteCursor;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

        listView = (ListView) findViewById(R.id.list_movies);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ListFavoriteMovieActivity.this, MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_POSITION, (int) id);
                startActivity(intent);
            }
        });

        try {
            SQLiteOpenHelper dbHelper = new MovieDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();
            favoriteCursor = db.query("movies",
                    new String[] {"_id", "title", "overview"},
                    null,
                    null, null, null, null);
            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(ListFavoriteMovieActivity.this,
                    R.layout.list_movie_item,
                    favoriteCursor,
                    new String[]{"title", "overview"},
                    new int[] {R.id.title, R.id.overview}
            );

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
