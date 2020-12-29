package solvinglogic;

import sudokulogic.SudokuBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class SimulatedAnnealingSolver {
    private boolean[][] canSwitch;
    private SudokuBoard currentSudoku;
    public SudokuBoard tempSudoku;
    private final Random r;
    private final double initTemperature;
    private double temperature;
    private int iteration;


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

    private void createMask() {
        this.canSwitch = new boolean[9][9];
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                this.canSwitch[row][column] = this.currentSudoku.getCell(row, column) == 0;
            }
        }

    }

    public void fillInitSudoku() {
        for (int i = 0; i < 9; i = i + 3) {
            for (int j = 0; j < 9; j = j + 3) {
                fillBox(i, j);
            }
        }
    }

    public void fillBox(int r, int c) {
        ArrayList<Integer> values = createArray();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int m = (r/3) * 3 + (i % 3);
                int n = (c/3) * 3 + (j % 3);
                values.remove(Integer.valueOf(this.currentSudoku.getCell(m, n)));

            }
        }

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

    private ArrayList<Integer> createArray() {
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            array.add(i);
        }
        Collections.shuffle(array);
        return array;
    }

    public boolean solve() {
        this.iteration = 0;
        while (iteration < 800000) {
            if (calculateEnergy(this.currentSudoku.getSudokuBoard()) < -161) {
                return true;
                }

            if (iteration % 5000 == 0) {
                reheatTemperature();
            }

            iteration++;
            randomMoveInRandomBox();
            if (iteration % 500 == 0) {
                updateTemperature();
            }

            if (calculateEnergy(this.tempSudoku.getSudokuBoard()) < calculateEnergy(this.currentSudoku.getSudokuBoard())) {
                this.currentSudoku.setSudokuBoard(this.tempSudoku.getSudokuBoard());
            } else if (Math.random() <= probability()) {
                this.currentSudoku.setSudokuBoard(this.tempSudoku.getSudokuBoard());
            }

        }
        System.out.println(iteration);
        return false;

    }

    private void updateTemperature() {
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
