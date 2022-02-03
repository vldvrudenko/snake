package com.codenjoy.dojo.snake.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.joystick.DirectionActJoystick;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private AStarAlgorithm search = new AStarAlgorithm();

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        System.out.println(board.toString());

        var source = board.getHead();

        if (source == null) {
            System.out.println("Snake is not present");
            return Direction.RIGHT.toString();
        }

        Point destination = null;

        if (!board.getApples().stream().findAny().isPresent()) {
            System.out.println("Apples are not present");
            destination = findFirstAvailable(board);
        } else {
            destination = board.getApples().stream().findFirst().get();
        }

        var grid = getGrid(board);
        var startCell = grid.cellAt(board.size() - 1 - source.getY(), source.getX());
        var endCell = grid.cellAt(board.size() - 1 - destination.getY(), destination.getX());

        var path = search.findPath(startCell, endCell, grid);

        if (path.stream().count() == 0) {
            return Direction.RIGHT.toString();
        }

        var nextCell = path.get(1);

        return moveTo(startCell, nextCell).toString();
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://164.90.213.43/codenjoy-contest/board/player/ftl765i0jr62wxxvqwzi?code=4979800189999756193",
                new YourSolver(new RandomDice()),
                new Board());
    }

    private static Grid getGrid(Board board) {
        var obstacles = board.getBarriers();

        int[][] structure = new int[board.size()][];
        for (int i = 0; i < board.size(); i++) {
            structure[i] = new int[board.size()];
        }

        for (var obst : obstacles) {
            var row = board.size() - obst.getY() - 1;
            var column = obst.getX();

            structure[row][column] = 1;
        }

        return new Grid(structure);
    }

    private static Point findFirstAvailable(Board board) {
        var obstacles = board.getBarriers();

        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                int finalX = x;
                int finalY = y;

                if (obstacles.stream().anyMatch(obs -> obs.getX() == finalX && obs.getY() == finalY)) {
                    continue;
                }

                return new PointImpl(x, y);
            }
        }

        return null;
    }

    private static Direction moveTo(Cell source, Cell dest) {
        if (source.Column > dest.Column) {
            return Direction.LEFT;
        }
        if (source.Column < dest.Column) {
            return Direction.RIGHT;
        }
        if (source.Row > dest.Row) {
            return Direction.UP;
        }
        if (source.Row < dest.Row) {
            return Direction.DOWN;
        }

        return Direction.RIGHT;
    }
}