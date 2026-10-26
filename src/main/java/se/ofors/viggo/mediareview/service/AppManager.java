package se.ofors.viggo.mediareview.service;

import se.ofors.viggo.mediareview.model.Movie;
import se.ofors.viggo.mediareview.repository.Library;
import se.ofors.viggo.mediareview.storage.FileStorage;
import se.ofors.viggo.mediareview.storage.Storage;
import se.ofors.viggo.mediareview.ui.GraphicsBuilder;
import se.ofors.viggo.mediareview.ui.MenuOption;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AppManager {

    private final GraphicsBuilder graphicsBuilder = new GraphicsBuilder();
    private final Storage storage = new FileStorage();
    private final Library library = new Library(storage);

    private final String welcomeMessage = """
            Welcome to Movie Ratings!
            - This program is made by Viggo Öfors
            - 2 months in to the Software Engineering Program
            - Version 1.0.0
            - Using Java, Maven and JOptionPane for GUI
            """;

    public void start() {
        while (true) {
            graphicsBuilder.multiQuestion(welcomeMessage, "Movie Ratings", List.of(
                    MenuOption.of("Browse", this::listMoviesTest),
                    MenuOption.of("Top Rated", this::topRated),
                    MenuOption.of("Search", this::searchMovie),
                    MenuOption.of("Add Movie", this::addMovie),
                    MenuOption.of("Exit", this::exit)
            ));
        }
    }

    private void addMovie() {
        String title = graphicsBuilder.input("Enter the title");
        String producer = graphicsBuilder.input("Who is the producer?");

        if (title != null && producer != null) {
            Movie movie = new Movie(title, producer);
            library.addMovie(movie);
        }
    }

    private void addReview(Movie movie) {
        ArrayList<MenuOption> options = new ArrayList<>(IntStream.range(0, 10)
                .mapToObj(reviewMovieOption(movie))
                .toList());
        options.add(new MenuOption("Back to Movie", () -> {}));

        graphicsBuilder.multiQuestion(welcomeMessage, "Movie Ratings", options);
    }

    private static IntFunction<MenuOption> reviewMovieOption(Movie movie) {
        return i -> new MenuOption(Integer.toString(i), () -> movie.addRating(i));
    }

    private void listMovies() {
        List<Movie> movieList = library.getMovieList();

        if (movieList.isEmpty()) {
            graphicsBuilder.output("List is empty.", "Movies");
        } else {
            String movies = "";
            for (int i = 0; i < movieList.size(); i++) {
                int index = i + 1;
                movies += index + ". " + movieList.get(i).getTitle() + System.lineSeparator();
            }
            graphicsBuilder.output(movies, "Movies");
        }
    }

    private void listMoviesTest() {
        List<Movie> movieList = library.getMovieList();

        if (movieList.isEmpty()) {
            graphicsBuilder.output("List is empty.", "Movies");
        } else {
            String movies = "";

            for (int i = 0; i < movieList.size(); i++) {
                movies += i + 1 + ". " + movieList.get(i).getTitle() + System.lineSeparator();
                if ((i + 1) % 20 == 0) {
                    graphicsBuilder.previousNextPage(movies, "Movies");
                    movies = ""; // Reset list
                }
            }
        }
    }

    private void searchMovie() {
        List<Movie> movieList = library.getMovieList();
        Stream<Movie> movieStream = movieList.stream();

        String search = graphicsBuilder.input("Search for a movie");
        List<Movie> result = new ArrayList<>();

        movieStream
                .filter(m -> m.getTitle().contains(search))
                .forEach(result::add);

        searchResult(result);
    }

    private void searchResult(List<Movie> movies) {
        int page = 0;

        while (page < movies.size()) {
            Movie currentMovie = movies.get(page);
            int choice = graphicsBuilder.previousNextPage(currentMovie.getInfo(), currentMovie.getTitle());

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

        graphicsBuilder.output(topRatedMovies, "Top 10 Rated Movies");
    }

    private void exit() {
        storage.saveToStorage(library.getMovieList());
        System.exit(0);
    }
}
