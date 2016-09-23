package nguyentritin.movieapp2.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidhive.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nguyentritin.movieapp2.R;
import nguyentritin.movieapp2.util.Consts;

/**
 * Created by giacatho on 9/24/16.
 * Credit: http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/
 */

public class MovieListAdapter extends BaseAdapter {
    private static final String TAG = MovieListAdapter.class.getSimpleName();

    private Activity activity;
    private static LayoutInflater inflater = null;
    private List<HashMap<String, String>> movieList;
    public ImageLoader imageLoader;

    public MovieListAdapter(Activity activity, List<HashMap<String, String>> movieList) {
        this.activity = activity;
        this.movieList = movieList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
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
        View view;
        if (convertView == null)
            view = inflater.inflate(R.layout.list_movie_item, null);
        else
            view = convertView;

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView overview = (TextView) view.findViewById(R.id.overview);
        TextView rating = (TextView) view.findViewById(R.id.rating);
        ImageView posterImage = (ImageView) view.findViewById(R.id.poster_image);

        Map<String, String> movie = movieList.get(position);

        title.setText(movie.get("title"));
        overview.setText(movie.get("overview"));
        // TODO
        rating.setText("Temporary");

        if (movie.get("poster_path") == null || movie.get("poster_path").equals("null")) {
            posterImage.setImageResource(R.mipmap.default_poster);
        } else {
            imageLoader.DisplayImage(Consts.POSTER_ROOT + movie.get("poster_path"),
                    R.mipmap.ic_launcher, posterImage);
        }

        return view;
    }


}
