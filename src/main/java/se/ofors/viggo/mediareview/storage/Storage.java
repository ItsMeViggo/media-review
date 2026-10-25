package se.ofors.viggo.mediareview.storage;

import se.ofors.viggo.mediareview.model.Movie;

import java.util.List;

public interface Storage {

    void saveToStorage(List<Movie> movies);

    List<Movie> addFromStorage();
}
