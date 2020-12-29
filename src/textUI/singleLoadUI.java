package textUI;

import createlogic.LoadSudoku;
import sudokulogic.SudokuBoard;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class singleLoadUI extends UI {

    private SudokuBoard loadedSudoku;

    public singleLoadUI(Scanner scan) {
        super(scan);
    }

    @Override
    public void printMenu() {
        System.out.println("-----------Menu-------------\n" +
                "1: load file\n" +
                "0: return to main menu\n" +
                "----------------------------");
    }

    @Override
    public void run() {
        System.out.println("\n---------Instruction-------------\n" +
                "Sudoku must be represented as string of digits e.g.\n" +
                "000090210000005006000706000501000700009800005000003160050000604800460000200000078\n" +
                "and stored in a .txt file. If multiple lines are present, only the first line will be read.\n");
        while (true) {
            printMenu();

            int input = super.getInputWithinLimits(0, 2);

            if (input == 0) {
                System.out.println("Returning to main menu\n");
                break;
            }
            processInput(input);
        }
    }

    @Override
    public void processInput(int input) {
        if (input == 1) {
            loadFile();
        }
    }

    private void loadFile() {
        String fileName = getFileName();
        LoadSudoku load = new LoadSudoku();
        boolean successfullyReadFile = load.readFile(fileName);

        if (!successfullyReadFile) {
            System.out.println("file not read.\n");
            return;
        }

        this.loadedSudoku = load.createSudokuBoardFromFile().get(0);
        System.out.println("sudoku loaded from file\n");
        solverUI solve = new solverUI(super.s, this.loadedSudoku);
        solve.run();

    }

    private String getFileName() {
        boolean isFileNameValid = false;
        String fileName = ".";
        while (!isFileNameValid) {
            System.out.print("Enter file name: ");
            fileName = super.s.nextLine();
            isFileNameValid = checkFileName(fileName);
        }
        return fileName;
    }

    private boolean checkFileName(String fileName) {
        File f = new File(fileName);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            System.out.println("Not a valid filename. Try another.");
            return false;
        }
    }
}
