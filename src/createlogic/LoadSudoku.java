package createlogic;

import sudokulogic.SudokuBoard;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Class to load sudoku from .txt file or internal resource. Sudokus must be represented as single string.
public class LoadSudoku {
    private ArrayList<String> sudokuAsString;
    private Scanner scanner;

    public LoadSudoku() {
    this.sudokuAsString = new ArrayList<>();
    }

    /*
     reads sudokus from a file. Filename passed as parameter from calling method. Returns true if file was loaded successfully and contains
     sudokus. Sudokus are added to arrayList this.sudokuAsString.
    */
    public boolean readFile(String filename) {
        try {
            File file = new File(filename);
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String sudoku = scanner.nextLine();
                if (sudoku.matches("^\\d{81}$")) {
                    this.sudokuAsString.add(sudoku);
                }
            }
            scanner.close();
            if (this.sudokuAsString.isEmpty()) {
                System.out.println("A sudoku was not found in file");
                return false;
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /*
     reads sudokus from a resource. class with access to resource passed as parameter from calling class.
     Returns true if file was loaded successfully.
     Sudokus are added to arrayList this.sudokuAsString.
    */
    public boolean readResource(String name, Class<?> classWithResource) {

        try {
            InputStream s = classWithResource.getResourceAsStream(name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(s));

            String line;
            while ((line = reader.readLine()) != null) {
                this.sudokuAsString.add(line);
            }
            s.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    // Creates and returns list containing loaded sudokus. Sudokus are
    public List<SudokuBoard> getSudokuList() {
        List<SudokuBoard> boards = new ArrayList<>();
        for (String s : this.sudokuAsString) {
            boards.add(fromStringToSudokuBoard(s));
        }
        return boards;
    }

    // converts a sudoku in string format to a SudokuBoard format.
    private SudokuBoard fromStringToSudokuBoard(String sudokuString) {
        SudokuBoard board = new SudokuBoard();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                int charIndex = (row * 9) + column;
                char charValue = sudokuString.charAt(charIndex);
                int value = Integer.valueOf(String.valueOf(charValue));
                board.setCell(value, row, column);
            }
        }
        return board;
    }
}
