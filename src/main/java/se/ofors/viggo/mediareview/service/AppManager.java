package se.ofors.viggo.mediareview.service;

import se.ofors.viggo.mediareview.model.Movie;
import se.ofors.viggo.mediareview.repository.Library;
import se.ofors.viggo.mediareview.storage.FileStorage;
import se.ofors.viggo.mediareview.storage.Storage;
import se.ofors.viggo.mediareview.ui.GraphicsBuilder;

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

            // TODO: Make the cases more readable.
            switch (menuChoice) {
                case 0 -> listMovies();
                case 1 -> IO.println("Top Rated List");
                case 2 -> searchMovie();
                case 3 -> addMovie();
                default -> exit();
            }

        }
    }

    private void addMovie() {
        // TODO: when cancel in the producer, don't create a new movie.
        String title = graphicsBuilder.input("Enter the title");
        String producer = graphicsBuilder.input("Who is the producer?");
        Movie movie = new Movie(title, producer);

        library.addMovie(movie);
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

    private void searchMovie() {
        List<Movie> movieList = library.getMovieList();
        Stream<Movie> movieStream = movieList.stream();

        String search = graphicsBuilder.input("Search for a movie");
        List<Movie> result = new ArrayList<>();

        // TODO: Then give a list, and choose from there.
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

    private void exit() {
        storage.saveToStorage(library.getMovieList());
        System.exit(0);
    }
}
