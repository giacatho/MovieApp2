package nguyentritin.movieapp2.model;

/**
 * Created by nguyentritin on 9/21/16.
 */

public class Movie {
    public static Movie[] movies = {
            new Movie("Movie 1"),
            new Movie("Movie 2")
    };

    public Movie() {

    }

    public Movie(String name) {
        this.setName(name);
    }

    private String name;
    private String title;
    private String overview;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview() {
        this.overview = overview;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
