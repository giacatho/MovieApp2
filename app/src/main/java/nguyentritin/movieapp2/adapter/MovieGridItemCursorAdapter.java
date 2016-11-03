package nguyentritin.movieapp2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import nguyentritin.movieapp2.R;
import nguyentritin.movieapp2.util.Consts;
import nguyentritin.movieapp2.util.Utils;

/**
 * Created by nguyentritin on 3/11/16.
 */

public class MovieGridItemCursorAdapter extends CursorAdapter {
    private LayoutInflater layoutInflater;

    public MovieGridItemCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return layoutInflater.inflate(R.layout.grid_movie_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView posterImageView = (ImageView) view.findViewById(R.id.poster_image);

        String posterPath = cursor.getString(cursor.getColumnIndex(Consts.DB_COL_POSTER_PATH));

        if (posterPath == null || posterPath.equals("null")) {
            posterImageView.setImageResource(R.mipmap.default_poster);
        } else {
            Glide.with(context).load(Consts.POSTER_ROOT + posterPath).into(posterImageView);
        }
    }
}
