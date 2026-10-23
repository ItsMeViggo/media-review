package se.ofors.viggo.mediareview.service;

import se.ofors.viggo.mediareview.model.Movie;
import se.ofors.viggo.mediareview.repository.Library;
import se.ofors.viggo.mediareview.storage.Storage;
import se.ofors.viggo.mediareview.ui.GraphicsBuilder;

import java.util.List;
import java.util.stream.Stream;

public class AppManager {

    private final GraphicsBuilder graphicsBuilder = new GraphicsBuilder();
    private Storage storage;
    private final Library library = new Library();

    private boolean isRunning = true;
    private Object[] menuOptions = {"Browse", "Top Rated", "Search", "Add Movie", "Exit"};

    private String welcomeMessage = """
            Welcome to Movie Ratings!
            - This program is made by Viggo Öfors
            - 2 months in to the Software Engineering Program
            - Version 1.0.0
            - Using Java, Maven and JOptionPane for GUI
            """;

    public void start() {
        while (isRunning) {
            int menuChoice = graphicsBuilder.multiquestion(welcomeMessage, "Movie Ratings", menuOptions);

            // TODO: Make the cases more readable.
            switch (menuChoice) {
                case 0 -> listMovies();
                case 1 -> IO.println("Top Rated List");
                case 2 -> searchMovie();
                case 3 -> addMovie();
                default -> isRunning = false;
            }

        }
    }

    private void addMovie() {
        String title = graphicsBuilder.input("Enter the title");
        String producer = graphicsBuilder.input("Who is the producer?");
        Movie movie = new Movie(title, producer);

        library.addMovie(movie);
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

        // TODO: Instead of equals, do contains, and see if there's more movies
        // TODO: Then give a list, and choose from there.
        movieStream
                .filter(m -> m.getTitle().equalsIgnoreCase(search))
                .forEach(graphicsBuilder::moviePage);
    }
}
