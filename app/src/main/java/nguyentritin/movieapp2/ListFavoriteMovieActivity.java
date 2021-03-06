package nguyentritin.movieapp2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import nguyentritin.movieapp2.adapter.MovieGridItemCursorAdapter;
import nguyentritin.movieapp2.adapter.MovieListItemCursorAdapter;
import nguyentritin.movieapp2.util.Consts;
import nguyentritin.movieapp2.util.MovieDatabaseHelper;

/**
 * Created by giacatho on 10/8/16.
 * Concept learned from: Head First Android books and multiple places
 * http://www.tutorialsbuzz.com/2013/11/android-sqlite-database-with.html
 * Menu Group: http://stackoverflow.com/questions/15851920/how-do-i-add-a-title-to-my-menu-group
 */
public class ListFavoriteMovieActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor favoriteCursor;

    private TextView statusTextView;
    private ListView listView;
    private GridView gridView;
    private CursorAdapter listCursorAdapter;
    private CursorAdapter gridCursorAdapter;

    private int orderBy = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favorite_movies);

        statusTextView = (TextView) findViewById(R.id.status);
        listView = (ListView) findViewById(R.id.list_movies);
        gridView = (GridView) findViewById(R.id.grid_movides);

        // When clicking the row, call MovieDetailActivity with the Movie info.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = ((CursorAdapter)listView.getAdapter()).getCursor();
                cursor.moveToPosition(position);

                callMovieDetail(cursor);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = ((CursorAdapter)gridView.getAdapter()).getCursor();
                cursor.moveToPosition(position);

                callMovieDetail(cursor);
            }
        });

        // When long pressing the row, call for deletion
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = ((CursorAdapter)listView.getAdapter()).getCursor();
                cursor.moveToPosition(position);

                confirmDeleteFavorite(cursor);

                return true;
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = ((CursorAdapter)gridView.getAdapter()).getCursor();
                cursor.moveToPosition(position);

                confirmDeleteFavorite(cursor);

                return true;
            }
        });

        try {
            SQLiteOpenHelper dbHelper = new MovieDatabaseHelper(this);
            db = dbHelper.getReadableDatabase();

            favoriteCursor = db.query(Consts.DB_TBL_NAME,
                    getQueryColumns(),
                    null,
                    null, null, null, null);

            listCursorAdapter = new MovieListItemCursorAdapter(this, favoriteCursor, 0);
            listView.setAdapter(listCursorAdapter);

            gridCursorAdapter = new MovieGridItemCursorAdapter(this, favoriteCursor, 0);
            gridView.setAdapter(gridCursorAdapter);

            if (favoriteCursor.getCount() == 0) {
                listView.setVisibility(View.GONE);
                statusTextView.setVisibility(View.VISIBLE);
            } else {
                if (isDisplayAsGrid()) {
                    gridView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                else {
                    gridView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
                statusTextView.setVisibility(View.GONE);

            }
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void callMovieDetail(Cursor cursor) {
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

    private void confirmDeleteFavorite(Cursor cursor) {
        final String movieId = cursor.getString(cursor.getColumnIndex(Consts.DB_COL_MOVIE_ID));

        // http://stackoverflow.com/a/5344958
        // Prompt user
        AlertDialog.Builder alert = new AlertDialog.Builder(ListFavoriteMovieActivity.this);

        alert.setTitle("Warning");
        alert.setMessage("Do you want to remove the selected movie from your favorite list?");

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing
            }
        });

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Remove movie from favorite list
                db.delete(Consts.DB_TBL_NAME, Consts.DB_COL_MOVIE_ID + " = ?",
                        new String[] { String.valueOf(movieId) });

                updateQueryCursor();
            }
        });

        alert.show();
    }

    // Trigger a new query and update cursor
    public void updateQueryCursor() {
        switch (this.orderBy)
        {
            case R.id.menu_order_by_title:
                favoriteCursor = db.query(Consts.DB_TBL_NAME,
                        getQueryColumns(),
                        null,
                        null, null, null, Consts.DB_COL_TITLE);
                break;

            case R.id.menu_order_by_favorite_date:
                favoriteCursor = db.query(Consts.DB_TBL_NAME,
                        getQueryColumns(),
                        null,
                        null, null, null, Consts.DB_COL_FAVORITE_TIMESTAMP + " DESC");
                break;

            default:
                favoriteCursor = db.query(Consts.DB_TBL_NAME,
                        getQueryColumns(),
                        null,
                        null, null, null, null);
                break;

        }

        if (favoriteCursor.getCount() == 0) {
            listView.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            statusTextView.setVisibility(View.VISIBLE);
        } else {
            if (isDisplayAsGrid()) {
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
            else {
                gridView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            statusTextView.setVisibility(View.GONE);

            listCursorAdapter.changeCursor(favoriteCursor);
            gridCursorAdapter.changeCursor(favoriteCursor);
        }

    }

    private String[] getQueryColumns() {
        return new String[] {"_id",
                Consts.DB_COL_MOVIE_ID,
                Consts.DB_COL_TITLE,
                Consts.DB_COL_OVERVIEW,
                Consts.DB_COL_POSTER_PATH,
                Consts.DB_COL_FAVORITE_TIMESTAMP};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_order_by_title:
                item.setChecked(true);
                this.orderBy = R.id.menu_order_by_title;
                updateQueryCursor();
                return true;

            case R.id.menu_order_by_favorite_date:
                item.setChecked(true);
                this.orderBy = R.id.menu_order_by_favorite_date;
                updateQueryCursor();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isDisplayAsGrid() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        return sharedPrefs.getBoolean("prefDisplayMoviesAsGrid", false);
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
