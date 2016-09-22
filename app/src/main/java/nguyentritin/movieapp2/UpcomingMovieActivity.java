package nguyentritin.movieapp2;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import nguyentritin.movieapp2.model.Movie;


public class UpcomingMovieActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<Movie> listAdapter = new ArrayAdapter<Movie>(
                this,
                android.R.layout.simple_list_item_1,
                Movie.movies
        );

        ListView listView = getListView();
        listView.setAdapter(listAdapter);
    }
}
