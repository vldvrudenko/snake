package com.codenjoy.dojo.snake.client;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarAlgorithm
{
    private boolean _allowDiagonals = false;
    private LinkedList<Cell> _openCells;
    private boolean[][] _closedCells;

    public List<Cell> findPath(Cell startCell, Cell endCell, Grid grid)
    {
        if(startCell == null || endCell == null){
            return new LinkedList();
        }

        if (isDestination(startCell, endCell))
        {
            return startCell.parentPath();
        }

        init(startCell, grid);

        while (_openCells.stream().count() != 0)
        {
            Cell current = _openCells.poll();

            if(current == null) {
                return new LinkedList<>();
            }

            _closedCells[current.Row][current.Column] = true;

            for (int row = current.Row - 1; row <= current.Row + 1; row++)
            {
                for (int column = current.Column - 1; column <= current.Column + 1; column++)
                {
                    if (allowDiagonal(row - current.Row, column - current.Column) && grid.isInRange(row, column))
                    {
                        Cell successor = grid.cellAt(row, column);

                        if (isDestination(successor, endCell))
                        {
                            endCell.setParent(current);
                            return endCell.parentPath();
                        }

                        if (!_closedCells[successor.Row][successor.Column] && successor.IsAvailable)
                        {
                            double newF, newG, newH;
                            newG = current.getG() + 1;
                            newH = successor.distanceTo(endCell);
                            newF = newG + newH;

                            if (successor.getF() == -1 || successor.getF() > newF)
                            {
                                if (!_openCells.contains(successor))
                                {
                                    _openCells.offer(successor);
                                }

                                successor.setH(newH);
                                successor.setG(newG);
                                successor.setParent(current);
                            }
                        }
                    }
                }
            }
        }

        return new LinkedList<>();
    }

    private boolean isDestination(Cell current, Cell destination)
    {
        return current.Row == destination.Row && current.Column == destination.Column;
    }

    private void init(Cell startCell, Grid grid)
    {
        startCell.setG(0);
        startCell.setH(0);

        _openCells = new LinkedList<>();
        _openCells.add(startCell);
        _closedCells = new boolean[grid.Rows][];

        for (int i = 0; i < grid.Rows; i++)
        {
            _closedCells[i] = new boolean[grid.Columns];
        }
    }

    private boolean allowDiagonal(int i, int j)
    {
        boolean check = Math.abs(i + j) == 1;

        return _allowDiagonals ? true : check;
    }
}
