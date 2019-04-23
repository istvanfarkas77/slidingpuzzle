package com.foo;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class QuzzleTest {

    Quzzle testable;

    Board initialBoard;
    Board expectedBoard;

    @Before
    public void setUp() {
        initialBoard =
            new Board(
                new EmptyPair(
                    new Empty(2, 0, 'E', '0'),
                    new Empty(2, 1, 'E', '0'))

                , new Piece(0, 0, 2, 2, 'd', '1')

                , new Piece(0, 2, 1, 2, 'g', '2')
                , new Piece(1, 2, 2, 1, 'g', '3')
                , new Piece(1, 3, 2, 1, 'g', '4')
                , new Piece(3, 0, 2, 1, 'g', '5')
                , new Piece(3, 1, 1, 2, 'g', '6')
                , new Piece(3, 3, 1, 1, 'g', '7')
                , new Piece(4, 1, 1, 2, 'g', '8')
                , new Piece(4, 3, 1, 1, 'g', '9')
            );

        expectedBoard =
            new Board(
                new EmptyPair(
                    new Empty(2, 2, 'E', '0'),
                    new Empty(2, 3, 'E', '0'))

                , new Piece(0, 2, 2, 2, 'd', '1')

                , new Piece(0, 0, 1, 2, 'g', '2')
                , new Piece(1, 0, 2, 1, 'g', '3')
                , new Piece(1, 1, 2, 1, 'g', '4')
                , new Piece(3, 0, 1, 1, 'g', '5')
                , new Piece(4, 0, 1, 1, 'g', '6')
                , new Piece(3, 1, 1, 2, 'g', '7')
                , new Piece(4, 1, 1, 2, 'g', '8')
                , new Piece(3, 3, 2, 1, 'g', '9')
            );

        testable = new Quzzle(initialBoard, expectedBoard);
    }

    @Test
    public void testSolveWithoutAStarSearch() {

        System.out.println("Printing all steps of recursion while building the graph with simply brute force, without applying any decision.");
        List<Board> solutions = testable.buildSolutionGraph(false);

        System.out.println("\n\n\n\nPrinting the path to the solution(s).");

        testable.printSolution(solutions);

        //assertTrue(solutions.size() > 0);
    }

    @Test
    public void testSolveWithAStarSearch() {

        System.out.println("Printing all steps of recursion while building the graph with making decisions by A* search.");
        List<Board> solutions = testable.buildSolutionGraph(true);

        //testable.findBestSolution(initialBoard);

        //testable.printSolution(solutions);

        //testable.printBestSolution(initialBoard, 0);

        //assertTrue(solutions.size() > 0);
    }
}
