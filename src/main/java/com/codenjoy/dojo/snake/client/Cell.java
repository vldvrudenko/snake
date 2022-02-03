package com.codenjoy.dojo.snake.client;

import java.util.Collections;
import java.util.LinkedList;

class Cell implements Comparable<Cell> {
    private double h = 0;
    private double g = -1;
    private Cell parent;

    public final int Row;
    public final int Column;
    public final boolean IsAvailable;

    public double getF() {
        return h + g;
    }

    public double getH() {
        return h;
    }

    public double getG() {
        return g;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public Cell getParent() {
        return parent;
    }

    Cell(int row, int column, boolean available) {
        Row = row;
        Column = column;
        IsAvailable = available;
    }

    public double distanceTo(Cell other) {
        return Math.sqrt(Math.pow(this.Row - other.Row, 2) + Math.pow(this.Column - other.Column, 2));
    }

    public LinkedList parentPath() {
        LinkedList<Cell> path = new LinkedList<Cell>();

        Cell current = this;
        while (current != null) {
            path.push(current);
            current = current.getParent();
        }

        return path;
    }

    @Override
    public int compareTo(Cell other) {
        return this.getF() > other.getF() ? 1 : -1;
    }
}

