package nguyentritin.movieapp2.util;

import java.util.List;
import java.util.Map;

/**
 * Created by giacatho on 10/8/16.
 */

public interface GetMovieDetailDelegate {
    public void onGetMovieDetailPreExecute();

    public void onGetMovieDetailError(String errStr);

    public void onGetMovieDetailSuccess(Map<String, String> movie);
}
