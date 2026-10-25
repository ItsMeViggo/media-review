package se.ofors.viggo.mediareview.ui;

import se.ofors.viggo.mediareview.model.Movie;

import javax.swing.*;

public class GraphicsBuilder {
    public int multiquestion(String question, String title, Object[] choices) {
        return JOptionPane.showOptionDialog(
                null,
                question,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                choices[0]
        );
    }

    public String input(String prompt) {
        return JOptionPane.showInputDialog(prompt);
    }

    public void output(String output) {
        JOptionPane.showMessageDialog(null, output);
    }

    public void output(String output, String pageTitle) {
        JOptionPane.showMessageDialog(null, output, pageTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    public int moviePage(Movie movie) {
        Object[] choices = {"Add Review", "Back"};

        return JOptionPane.showOptionDialog(
                null,
                movie.getInfo(),
                movie.getTitle(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                choices,
                choices[0]
        );
    }

    public int previousNextPage(String info, String title) {
        Object[] choices = {"Go to previous", "Go to next", "Add review", "Back to menu"};

        return JOptionPane.showOptionDialog(
                null,
                info,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                choices,
                choices[2]
        );
    }
}
