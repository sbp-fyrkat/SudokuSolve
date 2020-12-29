package createlogic;

import sudokulogic.SudokuBoard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadSudoku {
    private ArrayList<String> sudokuAsString;
    private Scanner scanner;

    public LoadSudoku() {
    this.sudokuAsString = new ArrayList<>();

    }

    public boolean readFile(String filename) {
        try {
            File file = new File(filename);
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String sudoku = scanner.nextLine();
                this.sudokuAsString.add(sudoku);
            }
            scanner.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

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

    public List<SudokuBoard> createSudokuBoardFromFile() {
        List<SudokuBoard> boards = new ArrayList<>();
        for (String s : this.sudokuAsString) {
            SudokuBoard board = new SudokuBoard();
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    int charIndex = (row * 9) + column;
                    char charValue = s.charAt(charIndex);
                    int value = Integer.valueOf(String.valueOf(charValue));
                    board.setCell(value, row, column);
                }
            }
            boards.add(board);
        }
        return boards;
    }
}
