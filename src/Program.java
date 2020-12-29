import textUI.MainMenuUI;

import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        MainMenuUI ui = new MainMenuUI(s);
        ui.run();

    }
}
