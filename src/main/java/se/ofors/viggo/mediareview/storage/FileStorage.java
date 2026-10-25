package se.ofors.viggo.mediareview.storage;

import se.ofors.viggo.mediareview.model.Movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileStorage implements Storage {

    String file = "movies.txt";
    Path path = Path.of(file);

    @Override
    public void saveToStorage(List<Movie> movies) {
        // To override the new files
        try {
            Files.delete(path);
        } catch (IOException e) {
            IO.println("file was not found.");
        }

        for (Movie movie : movies) {
            // Correctly formats the movie, "title;author;[reviews]"
            String formattedMovie = formatMovie(movie);
            try {
                Files.writeString(path, formattedMovie + System.lineSeparator(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                IO.println("File not found.");
            }
        }
    }

    @Override
    public List<Movie> addFromStorage() {
        List<String> formattedMovies;

        try {
            formattedMovies = Files.readAllLines(path);
        } catch (IOException e) {
            return new ArrayList<>();
        }

        List<Movie> movies = new ArrayList<>();

        for (String movie : formattedMovies) {
            movies.add(convertFormattedMovie(movie));
        }

        return movies;
    }

    private String formatMovie(Movie movie) {
        return movie.getTitle().strip() + ";" + movie.getProducer().strip() + ";" + movie.getRatings();
    }

    private Movie convertFormattedMovie(String formattedMovie) {
        String[] movieFields = formattedMovie.split(";");

        String title = movieFields[0];
        String producer = movieFields[1];

        // Make an array of strings with all the reviews
        String[] ratingsArray = movieFields[2]
                .replace("[", "")
                .replace("]", "")
                .split(",");

        if (ratingsArray[0].isBlank()) {
            return new Movie(title, producer);
        }

        List<Float> ratings = new ArrayList<>();

        for (String rating : ratingsArray) {
            ratings.add(Float.parseFloat(rating));
        }

        return new Movie(title, producer, ratings);
    }
}
