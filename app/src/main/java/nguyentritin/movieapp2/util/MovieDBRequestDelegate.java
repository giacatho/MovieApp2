package nguyentritin.movieapp2.util;

/**
 * Created by nguyentritin on 22/9/16.
 */

public interface MovieDBRequestDelegate {
    public void onMovieDBRequestCompleted(MovieDBRequest request, int status, String content);
}
