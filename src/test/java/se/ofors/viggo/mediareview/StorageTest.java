package se.ofors.viggo.mediareview;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.ofors.viggo.mediareview.model.Movie;
import se.ofors.viggo.mediareview.storage.DatabaseStorage;
import se.ofors.viggo.mediareview.storage.Storage;

import java.util.List;

public class StorageTest {

    private static Storage storage;

    @BeforeAll
    static void setup() {
        storage = new DatabaseStorage("moviestest.json");
    }

    @Test
    @DisplayName("Add movie to Database and get it from storage")
    void saveAndGetMovieFromDataBase() {
        Movie movie = new Movie("NCIS", "Donald P. Bellisario", List.of(10f, 9f, 10f, 7f));
        storage.saveToStorage(List.of(movie));

        List<Movie> movies = storage.addFromStorage();

        Assertions.assertEquals(1, movies.size());
    }
}
