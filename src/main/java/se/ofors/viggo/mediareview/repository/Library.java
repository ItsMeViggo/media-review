package se.ofors.viggo.mediareview.repository;

import se.ofors.viggo.mediareview.model.Movie;
import se.ofors.viggo.mediareview.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private final Storage storage;
    private List<Movie> movieList = new ArrayList<>();

    public Library(Storage storage) {
        this.storage = storage;
        movieList = storage.addFromStorage();
    }

    public void addMovie(Movie movie) {
        movieList.add(movie);
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}
