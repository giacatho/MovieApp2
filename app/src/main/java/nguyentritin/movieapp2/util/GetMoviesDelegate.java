package nguyentritin.movieapp2.util;

import java.util.List;
import java.util.Map;

/**
 * Created by giacatho on 10/8/16.
 */

public interface GetMoviesDelegate {
    public void onGetMoviesPreExecute();

    public void onGetMoviesError(String errStr);

    public void onGetMoviesSuccess(List<Map<String, String>> movies);
}
