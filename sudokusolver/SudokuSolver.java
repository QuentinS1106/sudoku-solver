// TODO: Add checkForBlockSingletons(int block)

import java.util.List;
import java.util.ArrayList;
public class SudokuSolver {
  private boolean impossible;
  private SudokuTile[][] sudoku;
  private int solvedTiles; 

  public SudokuSolver(int[][] vals) {
    impossible = false;
    solvedTiles = 0;
    sudoku = new SudokuTile[9][9];
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (vals[i][j] == 0) {
          sudoku[i][j] = new SudokuTile(i+1, j+1);
        } else {
          sudoku[i][j] = new SudokuTile(i+1, j+1, vals[i][j]);
        }
      }
    }
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
        sudoku[col][j].setValue(i);
        sudoku[col][j].removeNumber(i);
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
        sudoku[j][row].setValue(i);
        sudoku[j][row].removeNumber(i);
      }
    }
  }

  private class SudokuTile {
    private int value;
    private List<Integer> possibleValues = new ArrayList<Integer>(9);
    private final int row;
    private final int col;

    public SudokuTile(int row, int col) {
      this.row = row;
      this.col = col;
      value = 0;
      for (int i = 0; i < 9; i++) {
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
