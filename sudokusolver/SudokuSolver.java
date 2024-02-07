// TODO: THERE ARE SO MANY BUGS

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class SudokuSolver {
    private boolean impossible;
    private SudokuTile[][] sudoku;
    private int solvedTiles;
    private List<SudokuTile> tilesToCheck;

    public static void main(String[] args) {
        final String INPUT_FILE = "InputNums.txt";
        Scanner scan = null;
        try {
            scan = new Scanner(new BufferedReader(new FileReader(INPUT_FILE)));
        } catch (IOException e) {
            System.out.println(e);
        }
        int[][] vals = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                vals[j][i] = scan.nextInt();
            }
        }
        if (scan != null) {
            scan.close();
        }

        SudokuSolver solver = new SudokuSolver(vals);
        solver.solve();
    }

    public SudokuSolver(int[][] vals) {
        impossible = false;
        solvedTiles = 0;
        sudoku = new SudokuTile[9][9];
        tilesToCheck = new ArrayList<SudokuTile>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (vals[j][i] == 0) {
                    sudoku[j][i] = new SudokuTile(j + 1, i + 1);
                } else {
                    sudoku[j][i] = new SudokuTile(j + 1, i + 1, vals[j][i]);
                    tilesToCheck.add(sudoku[j][i]);
                }
            }
        }
    }

    public void solve() {
        for (SudokuTile s : tilesToCheck) {
            s.removeNumber(s.getValue());
        }
        while (solvedTiles < 81 && !impossible) {
            for (int i = 0; i < 9; i++) {
                checkForRowSingletons(i);
                checkForColumnSingletons(i);
                checkForBlockSingletons(i + 1);
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku[j][i].getValue() + " ");
            }
            System.out.println();
        }
        if (impossible) {
            System.out.println("Impossible to solve!");
        }
        System.out.println("SOLVED TILES: " + solvedTiles);
    }

    // Checks if any tile in a column has a unique possible value.
    public void checkForColumnSingletons(int col) {
        for (int i = 1; i <= 9; i++) {
            int count = 0;
            int found = -1;
            for (int j = 0; j < 9; j++) {
                SudokuTile s = sudoku[col][j];
                if (s.getPossibleValues() != null && s.getPossibleValues().contains(i)) {
                    count++;
                    found = j;
                }
            }
            if (count == 1) {
                sudoku[col][found].setValue(i);
                sudoku[col][found].removeNumber(i);
            }
        }
    }

    // Checks if any tile in a row has a unique possible value.
    public void checkForRowSingletons(int row) {
        for (int i = 1; i <= 9; i++) {
            int count = 0;
            int found = -1;
            for (int j = 0; j < 9; j++) {
                SudokuTile s = sudoku[j][row];
                if (s.getPossibleValues() != null && s.getPossibleValues().contains(i)) {
                    count++;
                    found = j;
                }
            }
            if (count == 1) {
                sudoku[found][row].setValue(i);
                sudoku[found][row].removeNumber(i);
            }
        }
    }

    public void checkForBlockSingletons(int block) {
        for (int i = 1; i <= 9; i++) {
            int count = 0;
            int foundCol = -1;
            int foundRow = -1;
            for (int j = (block / 3) * 3; i < (block / 3) * 3 + 3; i++) {
                for (int k = ((block - 1) % 3) * 3; j < ((block - 1) % 3) * 3 + 3; j++) {
                    SudokuTile s = sudoku[j][k];
                    if (s.getPossibleValues() != null && s.getPossibleValues().contains(i)) {
                        count++;
                        foundCol = j;
                        foundRow = k;
                    }
                }
                if (count == 1) {
                    sudoku[foundCol][foundRow].setValue(i);
                    sudoku[foundCol][foundRow].removeNumber(i);
                }
            }
        }
    }

    private class SudokuTile {
        private int value;
        private List<Integer> possibleValues = new ArrayList<Integer>();
        private final int row;
        private final int col;

        public SudokuTile(int row, int col) {
            this.row = row;
            this.col = col;
            value = 0;
            for (int i = 1; i <= 9; i++) {
                possibleValues.add(i);
            }
        }

        public SudokuTile(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
            solvedTiles++;
            possibleValues = null;
        }

        public int getValue() {
            return value;
        }

        public List<Integer> getPossibleValues() {
            return possibleValues;
        }

        public void setValue(int num) {
            value = num;
            solvedTiles++;
            possibleValues = null;
        }

        public void removePossibleValue(int num) {
            if (possibleValues != null && possibleValues.contains(num)) {
                possibleValues.remove(possibleValues.indexOf(num));
                if (possibleValues.size() == 1) {
                    value = possibleValues.get(0);
                    solvedTiles++;
                    possibleValues = null;
                    removeNumber(value);
                }
            }
            if (possibleValues != null && possibleValues.size() == 0) {
                impossible = true;
            }
        }

        public void removeNumber(int num) {
            if (num == 0) {
                return;
            }
            for (int i = 0; i < 9; i++) {
                SudokuTile s = sudoku[col - 1][i];
                s.removePossibleValue(num);
                s = sudoku[i][row - 1];
                s.removePossibleValue(num);
            }
            for (int i = ((col - 1) / 3) * 3; i < ((col - 1) / 3) * 3 + 3; i++) {
                for (int j = ((row - 1) / 3) * 3; j < ((row - 1) / 3) * 3 + 3; j++) {
                    SudokuTile s = sudoku[i][j];
                    s.removePossibleValue(num);
                }
            }
        }
    }
}
