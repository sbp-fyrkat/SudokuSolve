package solvinglogic;

import sudokulogic.SudokuBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

// Solves sudokus using simulated annealing.
// uses a slow cooling schedule in order, which is necessary to solve difficult sudokus, but is also time consuming.

public class SimulatedAnnealingSolver {
    private boolean[][] canSwitch;
    private final SudokuBoard currentSudoku;
    private final SudokuBoard tempSudoku;
    private final Random r;
    private final double initTemperature;
    private double temperature;
    private int iteration;

    /*
     Creates a copy of the SudokuBoard passed as parameter to @currentSudoku.
     Creates a mask used to identify which cells can be changed.
     Fills empty cells with random numbers. Each 3 x 3 box only contains 1 - 9.
    */
    public SimulatedAnnealingSolver(SudokuBoard sudoku) {
        this.currentSudoku = new SudokuBoard();
        this.tempSudoku = new SudokuBoard();
        this.currentSudoku.setSudokuBoard(sudoku.getSudokuBoard());
        createMask();
        this.r = new Random();
        this.initTemperature = 150;
        this.temperature = initTemperature;
        fillInitSudoku();
    }

    // creates boolean array. If the cell is true, then that position can be changed in the SudokuBoard.
    private void createMask() {
        this.canSwitch = new boolean[9][9];
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                this.canSwitch[row][column] = this.currentSudoku.getCell(row, column) == 0;
            }
        }

    }

    /*
     Iterates through each 3 x 3 box in the SudokuBoard, and fills empty cells with random numbers between 1 - 9
     ensuring that each number only occurs once.
    */
    public void fillInitSudoku() {
        for (int i = 0; i < 9; i = i + 3) {
            for (int j = 0; j < 9; j = j + 3) {
                fillBox(i, j);
            }
        }
    }

    // Fills a box with numbers 1 - 9 ensuring that each number only occurs once.
    public void fillBox(int r, int c) {
        // creates array with numbers 1 - 9
        ArrayList<Integer> values = createArray();

        // removes numbers already present in the box from the array.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int m = (r/3) * 3 + (i % 3);
                int n = (c/3) * 3 + (j % 3);
                values.remove(Integer.valueOf(this.currentSudoku.getCell(m, n)));

            }
        }

        // adds remaining numbers to the empty cells
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int m = (r/3) * 3 + (i % 3);
                int n = (c/3) * 3 + (j % 3);
                if (this.currentSudoku.getCell(m, n) == 0) {
                    this.currentSudoku.setCell(values.remove(0), m, n);
                }
            }
        }
    }

    // creates array with numbers 1 - 9
    private ArrayList<Integer> createArray() {
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            array.add(i);
        }
        Collections.shuffle(array);
        return array;
    }

    // Attempts to solve sudoku. Returns true if sudoku is successfully solved.
    public boolean solve() {
        this.iteration = 0;

        // Runs a max of 800.000 iterations. Will attempt a single change with each iteration.
        while (iteration < 800000) {

            // Energy of -162 means the solutions has been found.
            if (calculateEnergy(this.currentSudoku.getSudokuBoard()) < -161) {
                return true;
                }

            // Every 5000 iteration, the temperature is reset. This is to avoid being trapped in a local minimum.
            if (iteration % 5000 == 0) {
                reheatTemperature();
            }

            iteration++;

            // makes a random move in a random 3 x 3 box in the board. The move is made on the tempSudoku board.
            randomMoveInRandomBox();

            // Temperature is decreased every 500th iteration
            if (iteration % 500 == 0) {
                updateTemperature();
            }

            /*
             If the energy of the tempSudoku is lower than the currentSudoku, the change is implemented in currentSudoku
             If it is higher, it is accepted based on the probability function.
            */
            if (calculateEnergy(this.tempSudoku.getSudokuBoard()) < calculateEnergy(this.currentSudoku.getSudokuBoard())) {
                this.currentSudoku.setSudokuBoard(this.tempSudoku.getSudokuBoard());
            } else if (Math.random() <= probability()) {
                this.currentSudoku.setSudokuBoard(this.tempSudoku.getSudokuBoard());
            }

        }
        return false;

    }

    private void updateTemperature() {
        /*
         a slow temperature has been chosen. This drastically increased success with difficult sudokus compared to
         linear and geometric cooling schemes.
        */
        this.temperature = (1)/Math.log(1 + iteration);

    }

    private void reheatTemperature() {
        this.temperature = initTemperature;
    }

    public void randomMoveInRandomBox() {
        int row;
        int column;
        int newRow;
        int newColumn;

        // Chooses a random cell, and then a another random cell in the same box. Both cells must be switchable.
        do {
            row = r.nextInt(9);
            column = r.nextInt(9);

            newRow = (row / 3) * 3 + (r.nextInt(3) % 3);
            newColumn = (column / 3) * 3 + (r.nextInt(3) % 3);

        } while (!this.canSwitch[row][column] || !this.canSwitch[newRow][newColumn]);

        this.tempSudoku.setSudokuBoard(this.currentSudoku.getSudokuBoard());

        this.tempSudoku.setCell(this.currentSudoku.getCell(row, column), newRow, newColumn);
        this.tempSudoku.setCell(this.currentSudoku.getCell(newRow, newColumn), row, column);
    }

    public double probability() {
        int energyCurrent = calculateEnergy(this.currentSudoku.getSudokuBoard());
        int energyTemp = calculateEnergy(this.tempSudoku.getSudokuBoard());
        return Math.exp((energyCurrent - energyTemp)/ this.temperature);
    }

    /*
     Calculates energy of an array.
     Lowest possible energy is -162, if both rows
    */
    public int calculateEnergy(Integer[][] intArray) {
        int constraint = 0;
        HashSet<Integer> set = new HashSet<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (intArray[r][c] == 0) {
                    continue;
                }
                set.add(intArray[r][c]);
            }
            constraint += (-1 * set.size());
            set.clear();
        }

        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                if (intArray[r][c] == 0) {
                    continue;
                }
                set.add(intArray[r][c]);
            }

            constraint +=  (-1 * set.size());
            set.clear();
        }
        return constraint;
    }

    public SudokuBoard getCurrentSudoku() {
        return currentSudoku;
    }

    public boolean[][] getCanSwitch() {
        return canSwitch;
    }

    public int getIteration() {
        return this.iteration;
    }
}
