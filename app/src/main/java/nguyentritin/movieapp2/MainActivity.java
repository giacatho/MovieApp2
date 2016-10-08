package nguyentritin.movieapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import nguyentritin.movieapp2.util.Consts;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url;
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, ListFavoriteMovieActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        url = Consts.UPCOMING_MOVIE_URL;
                        startListMovieActivity(url);
                        break;

                    case 2:
                        url = Consts.NOW_PLAYING_MOVIE_URL;
                        startListMovieActivity(url);
                        break;

                    case 3:
                        url = Consts.TOP_RATED_MOVIE_URL;
                        startListMovieActivity(url);
                        break;

                    case 4:
                        url = Consts.POPULAR_MOVIE_URL;
                        startListMovieActivity(url);
                        break;

                    default:
                        Snackbar.make(view, "Not implemented", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                }


            }
        });
    }

    private void startListMovieActivity(String url) {
        Intent intent = new Intent(this, ListMovieActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;

        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                i = new Intent(this, UserSettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.action_about:
                i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
