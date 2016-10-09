package nguyentritin.movieapp2.util;

/**
 * Created by nguyentritin on 22/9/16.
 */

public class Consts {
    public static final String API_KEY = "fb6c0831c33905d075f7da0eae332aa6";
    public static final String UPCOMING_MOVIE_URL = "https://api.themoviedb.org/3/movie/upcoming?language=en-US&api_key=" + API_KEY;
    public static final String LATEST_MOVIE_URL = "https://api.themoviedb.org/3/movie/latest?api_key=language=en-US&api_key=" + API_KEY;
    public static final String NOW_PLAYING_MOVIE_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=language=en-US&api_key=" + API_KEY;
    public static final String TOP_RATED_MOVIE_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=language=en-US&api_key=" + API_KEY;
    public static final String POPULAR_MOVIE_URL = "https://api.themoviedb.org/3/movie/popular?api_key=language=en-US&api_key=" + API_KEY;

    public static final String URL_MOVIE_DETAIL = "https://api.themoviedb.org/3/movie/%s?api_key=fb6c0831c33905d075f7da0eae332aa6&language=en-US";

    public static final String DB_NAME = "movie";
    public static final String DB_TBL_NAME = "movies";
    public static final String DB_COL_MOVIE_ID = "id";
    public static final String DB_COL_TITLE = "title";
    public static final String DB_COL_OVERVIEW = "overview";
    public static final String DB_COL_POSTER_PATH = "poster_path";
    public static final String DB_COL_FAVORITE_TIMESTAMP = "f_timestamp";

    public static final String K_TAGLINE = "tagline";
    public static final String K_RELEASE_DATE = "release_date";
    public static final String K_PRODUCTION_COUNTRIES = "production_countries";
    public static final String K_GENRES = "genres";
    public static final String K_RATING = "vote_average";

    public static final String POSTER_ROOT = "http://image.tmdb.org/t/p/w300_and_h450_bestv2";
}

