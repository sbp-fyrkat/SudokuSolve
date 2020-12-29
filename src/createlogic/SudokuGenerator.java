package createlogic;

import sudokulogic.SudokuBoard;

import java.util.Random;

public class SudokuGenerator {
    private SudokuBoard board;
    private final int AMOUNT_TO_REMOVE;

    public SudokuGenerator(SudokuBoard board, int amountToRemove) {

        this.board = board;
        this.AMOUNT_TO_REMOVE = amountToRemove;
    }

    public void generate() {
        fillDiagonal();
        fillRemaining(0, 3);
        removeDigits();
    }

    public void fillDiagonal() {

        for (int i = 0; i < 9; i = i + 3) {
            fillBox(i, i);
        }
    }

    public boolean fillRemaining(int i, int j) {
        if (j >= 9 && i < 8) {
            i = i + 1;
            j = 0;
        }

        if (i >= 9 && j >= 9) {
            return true;
        }

        if (i < 3) {
            if (j < 3) {
                j = 3;
            }
        } else if (i < 6) {
            if (j == (int) (i/3) * 3) {
                j = j + 3;
            }
        } else {
            if (j == 6) {
                i = i + 1;
                j = 0;
                if (i >= 9) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= 9; num++) {
            if (checkIfSafe(i, j, num)) {
                this.board.setCell(num, i, j);
                if (fillRemaining(i, j + 1)) {
                    return true;
                }
                this.board.setCell(0,i,j);
            }
        }
        return false;
    }

    private void fillBox(int row, int column) {
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = new Random().nextInt(10);
                } while (!unUsedInBox(row, column, num));
                this.board.setCell(num, row + i, column + j);
            }
        }
    }

    private boolean unUsedInBox(int row, int column, int value) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int m = (row/3) * 3 + (i % 3);
                int n = (column/3) * 3 + (j % 3);
                if (this.board.getSudokuBoard()[m][n] == value) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean unUsedInRow(int row, int value) {
        for (int column = 0; column < 9; column++) {
            if (this.board.getSudokuBoard()[row][column] == value) {
                return false;
            }
        }
        return true;
    }

    private boolean unUsedInColumn(int column, int value) {
        for (int row = 0; row < 9; row++) {
            if (this.board.getSudokuBoard()[row][column] == value) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfSafe(int row, int column, int value) {
        return (unUsedInRow(row, value) && unUsedInColumn(column, value) && unUsedInBox(row, column, value));
    }

    public void removeDigits() {
        if (AMOUNT_TO_REMOVE > 81) {
            System.out.println("There's only 81 cells in a normal sudoku. No cells removed");
            return;
        }
        int count = AMOUNT_TO_REMOVE;

        while (count != 0) {
            int cell = new Random().nextInt(81);
            int row = cell/9;
            int column = cell % 9;


            if (this.board.getCell(row, column) != 0) {
                count--;
                this.board.setCell(0, row, column);
            }
        }
    }


}
