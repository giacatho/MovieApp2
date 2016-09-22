package nguyentritin.movieapp2.util;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import nguyentritin.movieapp2.model.Movie;

/**
 * Created by nguyentritin on 22/9/16.
 */

public class MovieDBRequest extends AsyncTask<Void, Void, Void> {
    private static final int RESULT_STATUS_SUCCESS = 1;
    private static final int RESULT_STATUS_FAIL = 2;

    private MovieDBRequestDelegate delegate;
    private String _urlString;
    private int _resultStatus;
    private String _resultString;
    private List<Movie> _resultMovies;

    @Override
    protected Void doInBackground(Void... args) {
        StringBuffer content = new StringBuffer("");
        try{
            URL url = new URL(_urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                content.append(line);
            }

        } catch (IOException e) {
            _resultStatus = RESULT_STATUS_FAIL;
            _resultString = e.getMessage();

            return null;
        }

        _resultStatus = RESULT_STATUS_SUCCESS;
        _resultString = content.toString();

        return null;
    }



    @Override
    protected void onPostExecute(Void json) {
        delegate.onMovieDBRequestCompleted(this, _resultStatus, _resultString);
    }


    // Get and set
    public void setUrlString(String urlString) {
        _urlString = urlString;
    }

    public MovieDBRequestDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(MovieDBRequestDelegate delegate) {
        this.delegate = delegate;
    }

}
