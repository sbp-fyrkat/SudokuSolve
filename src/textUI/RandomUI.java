package textUI;

import createlogic.LoadSudoku;
import sudokulogic.SudokuBoard;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

// Class that gets a random sudoku from a list of either easy, medium or hard difficulty.
public class RandomUI extends UI {

    private final LoadSudoku loader;

    public RandomUI(Scanner scan) {
        super(scan);
        this.loader = new LoadSudoku();
    }

    @Override
    public void run() {
        System.out.println("\nProgram contains 30.000 sudoku divided in 3 difficulty levels\n");

        while (true) {
            printMenu();
            int input = super.getInputWithinLimits(0, 3);

            if (input == 0) {
                System.out.println("Returning");
                break;
            }

            processInput(input);
        }
    }

    // Goes to solverUI to solve sudoku. Retrieves sudoku from resource contained in textUI package with getSudoku class.
    @Override
    public void processInput(int input) {
        if (input == 1) {
            UI solver = new solverUI(super.s, getSudoku("easy"));
            solver.run();
        } else if (input == 2) {
            UI solver = new solverUI(super.s, getSudoku("medium"));
            solver.run();
        } else if (input == 3) {
            UI solver = new solverUI(super.s, getSudoku("hard"));
            solver.run();
        }
    }


    @Override
    public void printMenu() {
        System.out.println("1: Get easy sudoku\n" +
                "2: Get medium sudoku\n" +
                "3: Get hard sudoku\n" +
                "0: exit menu");
    }

    /*
     Retrieves a sudoku from the three possible list of sudokus contained in the package
     passes this class to loadSudoku class so resources are available
    */
    private SudokuBoard getSudoku(String difficulty) {
        Random r = new Random();
        try {
            Class cls = this.getClass();
            this.loader.readResource(difficulty + ".txt", cls);
            List<SudokuBoard> easyList = this.loader.getSudokuList();
            return easyList.get(r.nextInt(easyList.size() + 1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }


    }
}
