package se.ofors.viggo.mediareview.ui;

public record MenuOption(String text, Runnable select) {
    public static MenuOption of(String text, Runnable select) {
        return new MenuOption(text, select);
    }
}
