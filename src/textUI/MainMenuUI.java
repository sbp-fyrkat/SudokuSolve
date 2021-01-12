package textUI;

import java.util.Scanner;

public class MainMenuUI extends UI {

    public MainMenuUI(Scanner scan) {
        super(scan);
    }

    public void run() {
        while (true) {
            printMenu();
            int input = super.getInputWithinLimits(0, 2);

            if (input == 0) {
                System.out.println("exiting program");
                break;
            }
            processInput(input);
        }
    }

    // goes to UI class of the chosen option
    public void processInput(int input) {
        if (input == 1) {
            UI randomUI = new RandomUI(super.s);
            randomUI.run();
        } else if (input == 2) {
            UI loadSingle = new singleLoadUI(super.s);
            loadSingle.run();
        }
    }

    @Override
    public void printMenu() {
        System.out.println("-------Main Menu-------");
        System.out.println("1: Get sudoku \n" +
                "2: Load single sudoku from file \n" +
                "0: Exit program \n" +
                "-----------------------");
    }

}


