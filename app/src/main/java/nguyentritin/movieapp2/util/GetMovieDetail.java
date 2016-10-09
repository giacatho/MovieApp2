package nguyentritin.movieapp2.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giacatho on 10/8/16.
 */
public class GetMovieDetail extends AsyncTask<Void, Void, Void> {
    private final String TAG = "GetMovieDetail";

    private String url;
    private GetMovieDetailDelegate delegate;

    private Map<String, String> movie;
    private String errStr;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        delegate.onGetMovieDetailPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        HttpsHandler hander = new HttpsHandler();

        String jsonStr = hander.makeServiceCall(this.url);

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject mv = new JSONObject(jsonStr);
                String tagline = mv.getString(Consts.K_TAGLINE);
                String releaseDate = mv.getString(Consts.K_RELEASE_DATE);
                String rating = mv.getString(Consts.K_RATING);
                String productionCountries = getString(mv.getJSONArray(Consts.K_PRODUCTION_COUNTRIES));
                String genres = getString(mv.getJSONArray(Consts.K_GENRES));

                movie = new HashMap<>();
                movie.put(Consts.K_TAGLINE, tagline);
                movie.put(Consts.K_RELEASE_DATE, releaseDate);
                movie.put(Consts.K_RATING, rating);
                movie.put(Consts.K_PRODUCTION_COUNTRIES, productionCountries);
                movie.put(Consts.K_GENRES, genres);
            } catch (final JSONException e) {
                errStr = "Json parsing error " + e.getMessage();
                Log.e(TAG, errStr);
            }
        } else {
            errStr = "Couldn't get json from server.";
            Log.e(TAG, errStr);
        }

        return null;
    }

    private String getString(JSONArray jsonArray) {
        JSONObject jsonObject;

        StringBuilder stringBuilder = new StringBuilder();

        if (jsonArray.length() == 0) {
            return "";
        }

        try {
            for (int i = 0; i < jsonArray.length() - 1; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                stringBuilder.append(jsonObject.getString("name") + ", ");
            }

            jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
            stringBuilder.append(jsonObject.getString("name"));
        } catch (Exception e) {
            return "";
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (errStr != null) {
            delegate.onGetMovieDetailError(errStr);
        } else {
            delegate.onGetMovieDetailSuccess(movie);
        }
    }


    // Setters
    public void setUrl(String url) {
        this.url = url;
    }

    public void setDelegate(GetMovieDetailDelegate delegate) {
        this.delegate = delegate;
    }
}