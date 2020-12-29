package sudokulogic;

public class SudokuBoard {

    private Integer[][] sudokuBoard;

    public SudokuBoard() {
        this.sudokuBoard = new Integer[9][9];
        fillWithZero();
    }

    public void print() {
        System.out.print("   ");
        for (int i = 0; i < 9; i++) {
            System.out.print(i + " ");
            if ((i + 1) % 3 == 0 && !(i == 0)) {
                System.out.print("  ");
            }
        }
        System.out.println(" ");
        System.out.println("------------------------");
        for (int r = 0; r < this.sudokuBoard.length; r++) {
            System.out.print(r + " |");
            for (int c = 0; c < this.sudokuBoard.length; c++) {
                // writes value in cell
                System.out.print(this.sudokuBoard[r][c] + " ");

                //separates sudoku boxes
                if ((c + 1) % 3 == 0 && !((c + 1) == 9) ) {
                    System.out.print("| ");
                }
            }

            System.out.println();
            if ((r + 1) % 3 == 0 && !(r+1 == 9)) {
                System.out.println("------------------------");
            }


        }
    }

    public void fillWithZero() {
        for (int r = 0; r < this.sudokuBoard.length; r++) {
            for (int c = 0; c < this.sudokuBoard.length; c++) {
                this.sudokuBoard[r][c] = 0;
            }
        }
    }

    public void setCell(int value, int row, int column) {
        if (value < 0 || value > 9) {
            System.out.println("Invalid integer. Must be between 0 - 9. Value set as 0.");
            this.sudokuBoard[row][column] = 0;
            return;
        }
        this.sudokuBoard[row][column] = value;
    }

    public int getCell(int r, int c) {
        return this.sudokuBoard[r][c];
    }

    public Integer[][] getSudokuBoard() {
        return sudokuBoard;
    }

    public void setSudokuBoard(Integer[][] newValues) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (newValues[r][c] < 0 || newValues[r][c] > 9) {
                    System.out.println("Invalid integer. Must be between 0 - 9. Value at row " + r + " column " + c + " set to 0.");
                    this.sudokuBoard[r][c] = 0;
                    continue;
                }
                this.sudokuBoard[r][c] = newValues[r][c];
            }
        }
    }

}
