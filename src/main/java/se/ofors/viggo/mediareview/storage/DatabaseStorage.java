package se.ofors.viggo.mediareview.storage;

import com.google.gson.Gson;
import se.ofors.viggo.mediareview.model.Movie;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class DatabaseStorage implements Storage {

    private final String filePath = "movies.json";
    private final Path path = Path.of(filePath);
    private final Gson gson = new Gson();

    @Override
    public void saveToStorage(List<Movie> movies) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            IO.println("File not found.");
        }

        for (Movie movie : movies) {
            String movieJson = gson.toJson(movie);
            try {
                Files.writeString(path, movieJson + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                IO.println("File could not be created.");
            }
        }
    }

    @Override
    public List<Movie> addFromStorage() {
        List<String> moviesJson = new ArrayList<>();
        List<Movie> movies = new ArrayList<>();

        try {
            moviesJson = Files.readAllLines(path);
        } catch (IOException e) {
            IO.println("File was not found.");
        }

        IO.println(moviesJson);


        for (String movie : moviesJson) {
            movies.add(gson.fromJson(movie, Movie.class));
        }

        IO.println(movies);


        return movies;
    }
}
