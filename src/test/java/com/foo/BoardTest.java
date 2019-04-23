package com.foo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BoardTest {

    public Board testable;

    @Before
    public void setUp() {
        testable =
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
    }

    @Test
    public void testEquals() {
        Board board =
            new Board(
                    new EmptyPair(
                            new Empty(2, 2, 'E', '0'),
                            new Empty(2, 3, 'E', '0'))

                    , new Piece(0, 2, 2, 2, 'd', '1')

                    , new Piece(0, 0, 1, 2, 'g', '7')
                    , new Piece(3, 1, 1, 2, 'g', '8')
                    , new Piece(4, 1, 1, 2, 'g', '2')
                    , new Piece(1, 0, 2, 1, 'g', '4')
                    , new Piece(1, 1, 2, 1, 'g', '9')
                    , new Piece(3, 3, 2, 1, 'g', '3')
                    , new Piece(3, 0, 1, 1, 'g', '6')
                    , new Piece(4, 0, 1, 1, 'g', '5')
            );


        System.out.println("testable\n" + testable.toString());

        System.out.println("expected\n" + board.toString());

        assertTrue(testable.equals(board));
    }

    @Test
    public void testCopyConstructor() {
        Board board = new Board(testable);

        System.out.println("testable\n" + testable.toString());

        System.out.println("copied\n" + board.toString());

        assertTrue(testable.equals(board));
    }
}
