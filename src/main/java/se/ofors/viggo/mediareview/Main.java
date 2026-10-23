package se.ofors.viggo.mediareview;

import se.ofors.viggo.mediareview.service.AppManager;

public class Main {
    static void main() {
        AppManager appManager = new AppManager();
        appManager.start();
    }
}
