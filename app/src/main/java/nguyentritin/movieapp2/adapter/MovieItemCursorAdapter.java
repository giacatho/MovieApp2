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

import java.util.Map;

import nguyentritin.movieapp2.R;
import nguyentritin.movieapp2.util.Consts;
import nguyentritin.movieapp2.util.Utils;

/**
 * Created by giacatho on 10/9/16.
 * https://coderwall.com/p/fmavhg/android-cursoradapter-with-custom-layout-and-how-to-use-it
 */

public class MovieItemCursorAdapter extends CursorAdapter {
    private LayoutInflater layoutInflater;

    public MovieItemCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return layoutInflater.inflate(R.layout.list_movie_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView overviewView = (TextView) view.findViewById(R.id.overview);
        TextView favoriteDateView = (TextView) view.findViewById(R.id.favorite_date);
        ImageView posterImageView = (ImageView) view.findViewById(R.id.poster_image);

        String title = cursor.getString(cursor.getColumnIndex(Consts.DB_COL_TITLE));
        String overview = cursor.getString(cursor.getColumnIndex(Consts.DB_COL_OVERVIEW));
        String posterPath = cursor.getString(cursor.getColumnIndex(Consts.DB_COL_POSTER_PATH));
        Long favoriteTS = cursor.getLong(cursor.getColumnIndex(Consts.DB_COL_FAVORITE_TIMESTAMP));

        titleView.setText(title);
        overviewView.setText(Utils.getShortOverviewStr(overview));
        favoriteDateView.setVisibility(View.VISIBLE);
        favoriteDateView.setText(Utils.getDateFromUnixTS(favoriteTS));

        if (posterPath == null || posterPath.equals("null")) {
            posterImageView.setImageResource(R.mipmap.default_poster);
        } else {
            Glide.with(context).load(Consts.POSTER_ROOT + posterPath).into(posterImageView);
        }
    }


}
