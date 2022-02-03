package com.codenjoy.dojo.snake.client;

public class Grid {
    private final Cell[][] cells;
    private final int[][] structure;

    public final int Rows;
    public final int Columns;

    public Grid(int[][] structure) {
        this.structure = structure;

        Rows = structure.length;
        Columns = structure[0].length;

        cells = new Cell[Rows][];
        for (int i = 0; i < Rows; i++) {
            cells[i] = new Cell[Columns];
            for (int j = 0; j < Columns; j++) {
                //0 = available, 1 = blocked
                cells[i][j] = new Cell(i, j, structure[i][j] == 0);
            }
        }
    }

    public Cell cellAt(int row, int column) {
        if (isInRange(row, column)) {
            return cells[row][column];
        }

        return null;
    }

    public boolean isInRange(int row, int column) {
        return row >= 0 && row < Rows && column >= 0 && column < Columns;
    }
}
