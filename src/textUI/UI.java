package textUI;

import java.util.Scanner;

public abstract class UI {

    public Scanner s;

    public UI(Scanner scan) {
        this.s = scan;
    }

    public abstract void printMenu();

    public abstract void run();

    public abstract void processInput(int input);

    public int getInputWithinLimits(int low, int high) {
        int input = getInteger();
        while (input < low || input > high) {
            System.out.println("Command not found");
            input = getInteger();
        }
        return input;
    }

    private int getInteger() {
        Integer input = null;
        while (input == null) {
            System.out.print("Enter command: ");
            try {
                input = Integer.valueOf(this.s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Must be integer.");
                input = null;
            }
        }
        return input;
    }
}
