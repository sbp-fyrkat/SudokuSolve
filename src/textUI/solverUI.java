package textUI;

import solvinglogic.SimulatedAnnealingSolver;
import sudokulogic.SudokuBoard;

import java.util.Scanner;

public class solverUI extends UI{
    private SudokuBoard sudoku;

    public solverUI(Scanner scan, SudokuBoard s) {
        super(scan);
        this.sudoku = new SudokuBoard();
        this.sudoku.setSudokuBoard(s.getSudokuBoard());
    }

    @Override
    public void printMenu() {
        System.out.println("--------Sudoku---------\n" +
                "1: print sudoku\n" +
                "2: solve using simulated annealing\n" +
                "0: exit");


    }

    @Override
    public void run() {
        this.sudoku.print();
        System.out.println(" ");
        while (true) {
            printMenu();

            int input = super.getInputWithinLimits(0, 2);
            if (input == 0) {
                System.out.println("returning to loading menu");
                break;
            }

            processInput(input);
        }
    }

    @Override
    public void processInput(int input) {
        if (input == 1) {
            this.sudoku.print();
        } else if (input == 2) {
            solveWithAnnealing();
        }
    }

    private void solveWithAnnealing() {
        SimulatedAnnealingSolver solver = new SimulatedAnnealingSolver(this.sudoku);
        final long startTime = System.nanoTime();
        boolean successfullySolved = solver.solve();
        final long endTime = System.nanoTime();

        if (successfullySolved) {
            this.sudoku.setSudokuBoard(solver.getCurrentSudoku().getSudokuBoard());
            double solvedIn = (double) (endTime - startTime)/1_000_000_000;
            System.out.println("Sudoku was solved in " + solvedIn + " seconds, and " + solver.getIteration() + " iterations." + " Solution is:");
            this.sudoku.print();
        } else {
            System.out.println("sudoku was not solved. Try again.");
        }
    }
}
