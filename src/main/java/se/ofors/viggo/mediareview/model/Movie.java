package se.ofors.viggo.mediareview.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Movie {

    private final String title;
    private final String producer;
    private final List<Float> ratings;

    public Movie(String title, String producer) {
        this.title = title;
        this.producer = producer;
        ratings = new ArrayList<>();
    }

    public void addRating(float amount) {
        if (amount >= 0 && amount <= 10) {
            ratings.add(amount);
        }
    }

    public Optional<Float> getAverageRating() {
        if (ratings.isEmpty()) {
            return Optional.empty();
        }

        float totalRatings = 0;

        for (float rating : ratings) {
            totalRatings += rating;
        }

        float averageRating = totalRatings / ratings.size();
        return Optional.of(averageRating);
    }

    public String getInfo() {
        Optional<Float> averageRating = getAverageRating();

        if (averageRating.isPresent()) {
            return title + System.lineSeparator() + "produced by: " + producer
                    + System.lineSeparator() + "Rating: " + averageRating + "/10 with "
                    + ratings.size() + " reviews";
        } else {
            return title + System.lineSeparator() + "produced by: " + producer
                    + System.lineSeparator() + "Rating: No ratings yet.";
        }
    }

    public String getTitle() {
        return title;
    }

    public String getProducer() {
        return producer;
    }

    public List<Float> getRatings() {
        return ratings;
    }
}
