package se.ofors.viggo.mediareview;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.ofors.viggo.mediareview.model.Movie;
import se.ofors.viggo.mediareview.repository.Library;
import se.ofors.viggo.mediareview.storage.DatabaseStorage;
import se.ofors.viggo.mediareview.storage.Storage;

import java.util.List;
import java.util.Optional;

public class MovieTest {

    private static Storage storage;
    private static Library library;

    @BeforeAll
    static void startup() {
        storage = new DatabaseStorage("moviestest.json");
        library = new Library(storage);
    }

    @Test
    @DisplayName("Add movie to library")
    void addMovieToLibrary() {
        Movie movie = new Movie("NCIS", "Donald P. Bellisario");
        library.addMovie(movie);

        Movie lastMovieInList = library.getMovieList().getLast();

        Assertions.assertEquals("NCIS", lastMovieInList.getTitle());
    }

    @Test
    @DisplayName("Correct average rating should be 9")
    void correctAverageRating() {
        Movie movie = new Movie("NCIS", "Donald P. Bellisario", List.of(10f, 9f, 10f, 7f));
        Optional<Float> averageRating = movie.getAverageRating();

        Assertions.assertEquals(9, averageRating.orElse(0f));
    }

    @Test
    @DisplayName("Correct average rating should be an empty optional")
    void correctAverageRatingOptionalEmpty() {
        Movie movie = new Movie("NCIS", "Donald P. Bellisario");
        Optional<Float> averageRating = movie.getAverageRating();

        Assertions.assertEquals(Optional.empty(), averageRating);
    }
}
