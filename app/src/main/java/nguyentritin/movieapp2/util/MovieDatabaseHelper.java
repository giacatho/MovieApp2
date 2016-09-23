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
    private static final String DB_NAME = "movie";
    private static final int DB_VERSION = 2;

    public MovieDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE movies(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "mv_id INTEGER, "
                + "title TEXT, "
                + "overview TEXT);";
        Log.i(TAG, query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS movies");
        onCreate(db);
    }


    public void addMovie(String movieId, String title, String overview) {
        ContentValues values = new ContentValues();
        values.put("mv_id", movieId);
        values.put("title", title);
        values.put("overview", overview);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("movies", null, values);
        db.close();
    }

}
