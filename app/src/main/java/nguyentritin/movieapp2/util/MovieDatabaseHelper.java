package nguyentritin.movieapp2.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nguyentritin on 23/9/16.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {
    public static String TAG = MovieDatabaseHelper.class.getSimpleName();

    private static final int DB_VERSION = 5;

    public MovieDatabaseHelper(Context context) {
        super(context, Consts.DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + Consts.DB_TBL_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Consts.DB_COL_MOVIE_ID + " TEXT, "
                + Consts.DB_COL_TITLE + " TEXT, "
                + Consts.DB_COL_OVERVIEW + " TEXT, "
                + Consts.DB_COL_POSTER_PATH + " TEXT, "
                + Consts.DB_COL_FAVORITE_TIMESTAMP + " INTEGER);";
        Log.i(TAG, query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Consts.DB_TBL_NAME);
        onCreate(db);
    }

    public void addMovie(String movieId, String title, String overview, String posterPath) {
        ContentValues values = new ContentValues();
        values.put(Consts.DB_COL_MOVIE_ID, movieId);
        values.put(Consts.DB_COL_TITLE, title);
        values.put(Consts.DB_COL_OVERVIEW, overview);
        values.put(Consts.DB_COL_POSTER_PATH, posterPath);
        values.put(Consts.DB_COL_FAVORITE_TIMESTAMP, System.currentTimeMillis() / 1000L);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Consts.DB_TBL_NAME, null, values);
        db.close();
    }

}
