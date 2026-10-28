package se.ofors.viggo.mediareview.service;

import se.ofors.viggo.mediareview.model.Movie;
import se.ofors.viggo.mediareview.repository.Library;
import se.ofors.viggo.mediareview.storage.FileStorage;
import se.ofors.viggo.mediareview.storage.Storage;
import se.ofors.viggo.mediareview.ui.GraphicsBuilder;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AppManager {

    private final GraphicsBuilder graphicsBuilder = new GraphicsBuilder();
    private final Storage storage = new FileStorage();
    private final Library library = new Library(storage);

    private final Object[] menuOptions = {"Browse", "Top Rated", "Search", "Add Movie", "Exit"};

    private final String welcomeMessage = """
            Welcome to Movie Ratings!
            - This program is made by Viggo Öfors
            - 2 months in to the Software Engineering Program
            - Version 1.0.0
            - Using Java, Maven and JOptionPane for GUI
            """;

    public void start() {
        while (true) {
            int menuChoice = graphicsBuilder.multiquestion(welcomeMessage, "Movie Ratings", menuOptions);

            switch (menuChoice) {
                case 0 -> listMovies();
                case 1 -> topRated();
                case 2 -> searchMovie();
                case 3 -> addMovie();
                default -> exit();
            }

        }
    }

    private void addMovie() {
        String title = graphicsBuilder.input("Enter the title");
        String producer = graphicsBuilder.input("Who is the producer?");

        if (title != null && producer != null && !title.isBlank() && !producer.isBlank()) {
            Movie movie = new Movie(title, producer);
            library.addMovie(movie);
        }
    }

    private void addReview(Movie movie) {
        Object[] choices = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Back to movie"};
        int rating = graphicsBuilder.multiquestion("How much would you rate " + movie.getTitle() + "?", movie.getTitle(), choices) + 1;
        int backToMovie = choices.length;

        if (rating == 0) {
            exit();
        }

        if (rating != backToMovie) {
            movie.addRating(rating);
        }

    }

    private void listMovies() {
        List<Movie> movieList = library.getMovieList();
        List<String> movieTitles = new ArrayList<>();
        String movies = "";

        if (movieList.isEmpty()) {
            graphicsBuilder.previousNextPage("No movies yet.", "Browse Movies", new Object[]{"Back to menu"});
        } else {
            for (int i = 0; i < movieList.size(); i++) {
                movies += (i+1) + ". " + movieList.get(i).getTitle() + System.lineSeparator();

                if ((i + 1) % 20 == 0) {
                    movieTitles.add(movies);
                    movies = "";
                }
            }

            if (!movieTitles.getLast().equals(movies)) {
                movieTitles.add(movies);
            }

            int page = 0;
            boolean browsing = true;
            while (browsing) {
                int choice = graphicsBuilder.previousNextPage(movieTitles.get(page), "Browse Movies", new Object[]{"Go to previous", "Go to next", "Back to menu"});

                switch (choice) {
                    case 0 -> {
                        if (page != 0) {
                            page--;
                        }
                    }
                    case 1 -> {
                        if (page != (movieTitles.size() - 1)) {
                            page++;
                        }
                    }
                    case 2 -> browsing = false;
                    default -> exit();
                }
            }
        }
    }

    private void searchMovie() {
        List<Movie> movieList = library.getMovieList();
        Stream<Movie> movieStream = movieList.stream();

        String search = graphicsBuilder.input("Search for a movie");

        if (search != null) {
            List<Movie> result = new ArrayList<>();

            movieStream
                    .filter(m -> m.getTitle().toLowerCase().contains(search.toLowerCase()))
                    .forEach(result::add);

            searchResult(result);
        }
    }

    private void searchResult(List<Movie> movies) {
        int page = 0;

        while (page < movies.size()) {
            Movie currentMovie = movies.get(page);
            int choice = graphicsBuilder.previousNextPage(currentMovie.getInfo(), currentMovie.getTitle(), new Object[]{"Go to previous", "Go to next", "Add review", "Back to menu"});

            switch (choice) {
                // PREVIOUS PAGE
                case 0 -> {
                    if (page > 0) {
                        page--;
                    }
                }
                // NEXT PAGE
                case 1 -> {
                    if (page < movies.size() - 1) {
                        page++;
                    }
                }
                // ADD REVIEW
                case 2 -> addReview(currentMovie);
                // BACK TO MENU
                case 3 -> page = movies.size();
                default -> exit();
            }
        }
    }

    private void topRated() {
        List<Movie> movies = library.getMovieList();
        movies.sort((m1, m2) -> Float.compare(
                m2.getAverageRating().orElse(0f),
                m1.getAverageRating().orElse(0f)
        ));

        String topRatedMovies = "";
        int index = 0;

        for (Movie movie : movies) {
            topRatedMovies += index + 1 + ". " + movie.getTitle() + " (" + movie.getAverageRating().orElse(0f) + "/10)" + System.lineSeparator();
            index++;

            if (index == 10) {
                break;
            }
        }

        int choice = graphicsBuilder.output(topRatedMovies, "Top 10 Rated Movies", new Object[]{"OK"});

        if (choice == JOptionPane.DEFAULT_OPTION) {
            exit();
        }
    }

    private void exit() {
        storage.saveToStorage(library.getMovieList());
        System.exit(0);
    }
}
