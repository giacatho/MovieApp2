package nguyentritin.movieapp2.adapter;

import android.app.Activity;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import nguyentritin.movieapp2.R;
import nguyentritin.movieapp2.util.Consts;

/**
 * Created by giacatho on 10/8/16.
 */

public class MovieGridAdapter extends BaseAdapter {
//    https://developer.android.com/guide/topics/ui/layout/gridview.html

    private static final String TAG = MovieGridAdapter.class.getSimpleName();

    private Activity activity;
    private List<Map<String, String>> movieList;

    public MovieGridAdapter(Activity activity, List<Map<String, String>> movieList) {
        this.activity = activity;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView posterImageView;

        if (convertView == null) {
            posterImageView = new ImageView(activity);
            posterImageView.setLayoutParams(new GridView.LayoutParams(400, 400));
        } else {
            posterImageView = (ImageView) convertView;
        }

        posterImageView.setImageResource(R.mipmap.default_poster);

        Map<String, String> movie = movieList.get(position);
        if (movie.get("poster_path") != null && !movie.get("poster_path").equals("null")) {
            Glide.with(activity).load(Consts.POSTER_ROOT + movie.get("poster_path")).into(posterImageView);
        }

        return posterImageView;
    }
}
