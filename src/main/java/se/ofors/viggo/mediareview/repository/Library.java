package se.ofors.viggo.mediareview.repository;

import se.ofors.viggo.mediareview.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private final List<Movie> movieList = new ArrayList<>();

    public void addMovie(Movie movie) {
        movieList.add(movie);
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}
