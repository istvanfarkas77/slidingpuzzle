package com.foo;

import java.util.*;

/**
 * Quzzle is a helper class in order to execute an iterative search from an initial board state till an expected board state.
 */
public class Quzzle {

    final static int ROW = 5;
    final static int COL = 4;
    final static int HARD_STOP_THRESHOLD = 1000;

    Board initialBoard;
    Board expectedBoard;

    List<Board> solutions;

    public Quzzle(Board initialBoard, Board expectedBoard) {
        solutions = new ArrayList<>();
        this.expectedBoard = expectedBoard;
        this.initialBoard = initialBoard;
    }

    /**
     * Discovers all possible board variations started from the initial one.
     *
     * @param withAStarSearch Define the next graph nodes by A* search, or simply with brute force.
     * @return List of result boards.
     */
    public List<Board> buildSolutionGraph(boolean withAStarSearch) {

        // Building graph with A * Search
        if (withAStarSearch)
            solve(initialBoard, 1, HARD_STOP_THRESHOLD);
        // Building graph with brute force
        else
            solveWithoutAStarSearch(initialBoard, 1, HARD_STOP_THRESHOLD);

        return solutions;
    }

    private void solve(Board board, int level, int hardStopThreshold) {

        if (hardStopThreshold == 0)
            throw new RuntimeException("Failed to find solution withing the given boundaries.");

        --hardStopThreshold;

        if (board.equals(expectedBoard)) {
            solutions.add(board);
        } else {
            board.alreadyVisitedBoards.add(board);

            List<Move> validMoves = board.validMoves();

            if (!validMoves.isEmpty()) {
                int gScore = level + 1;

                boolean hasOriginalPrinted = false;

                List<Board> nextBoards = new ArrayList<>();
                // Collect all the possible moves on this level
                for (Move nextMove : validMoves) {

                    Board nextBoard = board.applyMove(nextMove);

                    if (nextBoard != null) {
                        if (!board.alreadyVisitedBoards.contains(nextBoard)) {

                            if (!hasOriginalPrinted) {
                                System.out.println("==> ==> ==> ==> ==> ==> ==> ==> ==> ==> ==>");
                                System.out.println(board.toString());

                                hasOriginalPrinted = true;
                            }

                            nextBoard.hScore = nextBoard.hScore(expectedBoard);
                            nextBoard.gScore = gScore;
                            nextBoard.fScore = gScore + nextBoard.hScore;

                            nextBoards.add(nextBoard);

                            System.out.println("Move: "+nextMove);
                            System.out.println(nextBoard.toString());
                        }
                    }
                }

                if (hasOriginalPrinted) {
                    System.out.println("<== <== <== <== <== <== <== <== <== <== <==\n\n");
                }

                // Sorting the possible moves by f-score
                Collections.sort(nextBoards);

                // Continue recursion only on boards which has the greates f-score
                for (int i=0; i<nextBoards.size(); i++) {
                    Board nextBoard = nextBoards.get(i);
                    if (i == 0) {
                        board.succeedingBoards.add(nextBoard);
                        nextBoard.alreadyVisitedBoards = new HashSet<>(board.alreadyVisitedBoards);
                        solve(nextBoard, level + 1, hardStopThreshold);
                    } else if (nextBoards.get(0).fScore == nextBoards.get(i).fScore) {
                        board.succeedingBoards.add(nextBoard);
                        nextBoard.alreadyVisitedBoards = new HashSet<>(board.alreadyVisitedBoards);
                        solve(nextBoard, level + 1, hardStopThreshold);
                    }
                }
            }
        }
    }

    private void solveWithoutAStarSearch(Board board, int level, int hardStopThreshold) {

        if (hardStopThreshold == 0)
            throw new RuntimeException("Failed to find solution withing the given boundaries.");

        --hardStopThreshold;

        if (board.equals(expectedBoard)) {
            solutions.add(board);
        } else {
            board.alreadyVisitedBoards.add(board);

            List<Move> validMoves = board.validMoves();

            if (!validMoves.isEmpty()) {
                int gScore = level + 1;

                boolean hasOriginalPrinted = false;

                List<Board> nextBoards = new ArrayList<>();
                // Collect all the possible moves on this level
                for (Move nextMove : validMoves) {

                    Board nextBoard = board.applyMove(nextMove);

                    if (nextBoard != null) {

                        // Instead of pre-validating the result of the moves, the recurrence of boards are avoided.
                        if (!board.alreadyVisitedBoards.contains(nextBoard)) {

                            if (!hasOriginalPrinted) {
                                System.out.println("==> ==> ==> ==> ==> ==> ==> ==> ==> ==> ==>");
                                System.out.println(board.toString());

                                hasOriginalPrinted = true;
                            }

                            board.succeedingBoards.add(nextBoard);

                            nextBoard.hScore = nextBoard.hScore(expectedBoard);
                            nextBoard.gScore = gScore;
                            nextBoard.fScore = gScore + nextBoard.hScore;
                            nextBoard.alreadyVisitedBoards = board.alreadyVisitedBoards;

                            nextBoards.add(nextBoard);

                            System.out.println("Move: "+nextMove);
                            System.out.println(nextBoard.toString());
                        }
                    }
                }

                if (hasOriginalPrinted) {
                    System.out.println("<== <== <== <== <== <== <== <== <== <== <==\n\n");
                }

                for (Board nextBoard : nextBoards) {
                    solveWithoutAStarSearch(nextBoard, level + 1, hardStopThreshold);
                }
            }
        }
    }

    /**
     * Prints out the all the boards in the order of walk-through.
     *
     * @param solutions List of solutions.
     */
    public void printSolution(List<Board> solutions) {
        for (Board board : solutions) {
            System.out.print("\n\nTo reach a possible terminal state");

            // reversing the sequence of boards the lead to the expected state
            LinkedList<Board> list = new LinkedList<>();

            for (Board b = board; b != null; b = b.origin) {
                list.push(b);
            }

            System.out.print(String.format(" %d iterations are needed without applying any search strategy.\n\n", list.size()));
            list.forEach(System.out::println);

            System.out.println("\n\n");
        }
    }

    /*public void findBestSolution(Board board) {

        List<List<Board>> solutionChains = new ArrayList<List<Board>>();

        List<Board> solutionChain = new LinkedList<Board>();

        solutionChains.add(solutionChain);

        findBestSolution(initialBoard, 0, solutionChains, solutionChain);

        solutionChain.stream().forEach(System.out::println);
    }*/

    /*private void findBestSolution(Board board, int iteration, List<List<Board>> solutionChains, List<Board> solutionChain) {

        if (board.succeedingBoards != null && board.succeedingBoards.size() > 0) {
            Collections.sort(board.succeedingBoards);

            // FIXME there are not only one succeeding boards with the same fScore
            for (int i=0; i<board.succeedingBoards.size(); i++) {
                if (i == 0) {
                    solutionChain.add(board.succeedingBoards.get(i));
                    findBestSolution(board.succeedingBoards.get(i), iteration +1, solutionChains, solutionChain);
                } else if (i > 0 && board.succeedingBoards.get(0).fScore == board.succeedingBoards.get(i).fScore) {
                    List<Board> clonedSolutionChain = new LinkedList<Board>(solutionChain);
                    solutionChains.add(clonedSolutionChain);
                    findBestSolution(board.succeedingBoards.get(i), iteration +1, solutionChains, clonedSolutionChain);
                }
            }
        }
    }*/

    /**
     * Prints out the optimal solution considering the minimal f-score in each step.
     * @param board
     * @param iteration
     */
    /*public void printBestSolution(Board board, int iteration) {

        boolean hasOriginalPrinted = false;

        for (Board nextBoard : board.succeedingBoards) {

            if (!hasOriginalPrinted) {
                System.out.println("==> ==> ==> ==> ==> ==> ==> ==> ==> ==> ==>");
                System.out.println(board.toString());

                hasOriginalPrinted = true;
            }

            System.out.println("Move: "+nextMove);
            System.out.println(nextBoard.toString());

        if (board.succeedingBoards != null && board.succeedingBoards.size() > 0) {
            Collections.sort(board.succeedingBoards);

            // FIXME there are not only one succeeding boards exist with the same fScore

            Board closestBoard = board.succeedingBoards.get(0);
            System.out.println(iteration+"\n"+closestBoard);

            printBestSolution(closestBoard, ++iteration);
        }
    }*/
}

/**
 * A board of sliding puzzle containing MxN pieces with different dimensions, and two 1x1 empty cells.
 * The board serves as a tree, referencing to all possible succeeding boards and the moves that lead to them.
 */
class Board implements Comparable<Board> {

    /** The pair of empty cells helps to examine the possible moves relative to the empty cells. */
    EmptyPair empties;
    /** The MxN dimension grid of pieces. Each grid position refers to the piece instance located on that coordinate. */
    Piece[][] grid = new Piece[Quzzle.ROW][Quzzle.COL];

    /** Refers to the board from where this one is derived by a move. */
    Board origin;
    /** A move that was applied on the original board previously. */
    Move previousMove;
    /** Collection of all succeeding boards. */
    List<Board> succeedingBoards = new ArrayList<>();

    Set<Board> alreadyVisitedBoards = new HashSet<>();

    int fScore;
    int gScore;
    int hScore;

    public Board(EmptyPair empties, Piece ... pieces) {
        this.empties = new EmptyPair(empties);

        for (Piece piece : pieces)
            placePiece(new Piece(piece));

        for (Empty empty : this.empties.empties)
            placePiece(empty);
    }

    private Board() {}

    public Board(Board board) {
        this();

        for (int row = 0; row<board.grid.length; row++)
            for (int col = 0; col<board.grid[row].length; col++)
                this.grid[row][col] = new Piece(board.grid[row][col]);

        empties = new EmptyPair(board.empties);

        for (Empty empty : empties.empties)
            placePiece(empty);
    }

    Board applyMove(Move move) {

        // FIXME Possible check of unnecessary move if it would be the opposite of the previous one. Hard to carry out due to double empty cells.
        /*if (this.previousMove != null && this.previousMove.opposite().equals(move)) {
            System.out.println(String.format("Skipping move because it is the opposite of the previous one! Move: [%s]", move));

            return null;
        }*/

        // Copying and linking two subsequent boards before making the move.
        Board succeedingBoard = new Board(this);

        succeedingBoard.origin = this;
        succeedingBoard.previousMove = move;

        if (move instanceof SingleMove) {
            SingleMove singleMove = (SingleMove)move;

            Piece piece = succeedingBoard.grid[singleMove.piece.row][singleMove.piece.col];
            Empty empty = (Empty)succeedingBoard.grid[singleMove.empty.row][singleMove.empty.col];

            piece.move(move);
            succeedingBoard.placePiece(piece);

            empty.move(move);
            succeedingBoard.placePiece(empty);
        } else {
            DoubleMove doubleMove = (DoubleMove) move;

            Piece piece = succeedingBoard.grid[doubleMove.piece.row][doubleMove.piece.col];
            piece.move(move);
            succeedingBoard.placePiece(piece);

            int i = 0;
            for (Empty empty : succeedingBoard.empties.empties) {
                empty.move(move);
                succeedingBoard.placePiece(empty);
                succeedingBoard.empties.empties[i++] = empty;
            }
        }

        // The array of empty cells must be ordered by the coordinates in order to enable correct comparision.
        Arrays.sort(succeedingBoard.empties.empties);

//        System.out.println("==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>");
//        System.out.println(this.toString());
//        System.out.println("move: "+move);
//        System.out.println(succeedingBoard.toString());
//        System.out.println("<==<==<==<==<==<==<==<==<==<==<==<==<==<==<==\n\n");

        // discarding in case the succeeding would be the predecessor of the original board
        if (this.origin != null && succeedingBoard.equals(this.origin.origin))
            return null;

        //this.succeedingBoards.add(succeedingBoard);

        return succeedingBoard;
    }

    /**
     * Distributes the piece on the grid.
     * @param piece
     */
    private void placePiece(Piece piece) {
        for (int row = piece.row; row<piece.row + piece.h; row++)
            for (int col = piece.col; col<piece.col + piece.w; col++)
                this.grid[row][col] = piece;
    }

    public List<Move> validMoves() {
        List<Move> validSingleMoves = validDoubleMoves();
        validSingleMoves.addAll(validSingleMoves());
        return validSingleMoves;
    }

    /**
     * Collects all the possible moves that affect adjacent empty cells. These are considered special because both empty cells are shifted together.
     */
    private List<Move> validDoubleMoves() {
        List<Move> validDoubleMoves = new ArrayList<>();

        if (empties.areHorizontallyAdjacent()) {
            for(Direction direction : Arrays.asList(Direction.UP, Direction.DOWN)) {
                Direction opposite = direction.opposite();
                if (empties.empties[0].row + direction.row >= 0
                 && empties.empties[0].row + direction.row < Quzzle.ROW // empty stays on the board
                 && grid[empties.empties[0].row + direction.row][empties.empties[0].col + direction.col].w == 2 // the nearby piece has proper width or height
                 && grid[empties.empties[0].row + direction.row][empties.empties[0].col + direction.col].col == empties.empties[0].col) { // the nearby piece is in line with the empty cells
                    Piece piece = grid[empties.empties[0].row + direction.row][empties.empties[0].col + direction.col];

                    Move proposedMove = new DoubleMove(piece, empties, opposite);
                    // FIXME Possible check of unnecessary move if it would be the opposite of the previous one.
                    //if (this.previousMove != null && this.previousMove.opposite().equals(proposedMove))
                    //    System.out.println(String.format("Skip adding move because it is the opposite of the previous one! Move: [%s]", proposedMove));
                    //else
                        validDoubleMoves.add(proposedMove);
                }
            }
        } else if (empties.areVerticallyAdjacent()) {
            for(Direction direction : Arrays.asList(Direction.LEFT, Direction.RIGHT)) {
                Direction opposite = direction.opposite();
                if (empties.empties[0].col + direction.col >= 0
                 && empties.empties[0].col + direction.col < Quzzle.COL
                 && grid[empties.empties[0].row + direction.row][empties.empties[0].col + direction.col].h == 2
                 && grid[empties.empties[0].row + direction.row][empties.empties[0].col + direction.col].row == empties.empties[0].row) {
                    Piece piece = grid[empties.empties[0].row + direction.row][empties.empties[0].col + direction.col];

                    Move proposedMove = new DoubleMove(piece, empties, opposite);
                    // FIXME Possible check of unnecessary move if it would be the opposite of the previous one.
                    //if (this.previousMove != null && this.previousMove.opposite().equals(proposedMove))
                    //    System.out.println(String.format("Skip adding move because it is the opposite of the previous one! Move: [%s]", proposedMove));
                    //else
                        validDoubleMoves.add(proposedMove);
                }
            }
        }

        return validDoubleMoves;
    }

    private List<Move> validSingleMoves() {
        List<Move> validSingleSingleMoves = new ArrayList<>();

        for (Empty empty : empties.empties) {
            for (Direction direction : Direction.values()) {
                Direction opposite = direction.opposite();
                // horizontal
                if (direction.row == 0
                 && empty.col + direction.col >= 0
                 && empty.col + direction.col < Quzzle.COL
                 && grid[empty.row + direction.row][empty.col + direction.col].h == 1
                 && !(grid[empty.row + direction.row][empty.col + direction.col] instanceof Empty)) {
                    Piece piece = grid[empty.row + direction.row][empty.col + direction.col];

                    Move proposedMove = new SingleMove(piece, empty, opposite);
                    // FIXME Possible check of unnecessary move if it would be the opposite of the previous one.
                    //if (this.previousMove != null && this.previousMove.opposite().equals(proposedMove))
                    //    System.out.println(String.format("Skip adding move because it is the opposite of the previous one! Move: [%s]", proposedMove));
                    //else
                    validSingleSingleMoves.add(proposedMove);
                }
                // vertical
                else if (direction.col == 0
                 && empty.row + direction.row >= 0
                 && empty.row + direction.row < Quzzle.ROW
                 && grid[empty.row + direction.row][empty.col + direction.col].w == 1
                 && !(grid[empty.row + direction.row][empty.col + direction.col] instanceof Empty)) {
                    Piece piece = grid[empty.row + direction.row][empty.col + direction.col];

                    Move proposedMove = new SingleMove(piece, empty, opposite);
                    // FIXME Possible check of unnecessary move if it would be the opposite of the previous one.
                    //if (this.previousMove != null && this.previousMove.opposite().equals(proposedMove))
                    //    System.out.println(String.format("Skip adding move because it is the opposite of the previous one! Move: [%s]", proposedMove));
                    //else
                    validSingleSingleMoves.add(proposedMove);
                }
            }
        }

        return validSingleSingleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        boolean b = Arrays.deepEquals(grid, board.grid);
        b &= empties.equals(board.empties);

        return b;
    }

    @Override
    public int hashCode() {
        int result = empties.hashCode();
        for (Piece[] pieces : grid)
            for (Piece piece : pieces)
                result = 31 * result + piece.hashCode();

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("f-score: %d, g-score: %d, h-score: %d\n", fScore, gScore, hScore));
        sb.append(empties.toString()).append("\n");

        for (int row=0; row<grid.length; row++) {
            sb.append("[ ");

            for (int col = 0; col < grid[row].length; col++) {
                Piece piece = grid[row][col];
                sb.append(piece.value).append(piece.discriminator).append(" ");
            }

            sb.append("]");

            // for logging position information of the pieces
            /*sb.append(" [ ");
            for (int col = 0; col < grid[row].length; col++) {
                Piece piece = grid[row][col];
                sb.append(String.format("{%d,%d}", piece.row, piece.col)).append(" ");
            }
            sb.append("]");*/

            if (row != grid.length -1)
                sb.append("\n");
        }

        return sb.toString();
    }

    public int hScore(Board board) {
        int h = 0;
        for (int row = 0; row<grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Piece p1 = this.grid[row][col];
                Piece p2 = board.grid[row][col];

                if (p1.row != p2.row)
                    h++;
                else if (p1.col != p2.col)
                    h++;
            }
        }

        return h;
    }

    @Override
    public int compareTo(Board board) {
        return this.fScore == board.fScore ? 0 : (this.fScore < board.fScore ? -1 : 1);
    }
}

/** Interface of any kind of move on the board. */
interface Move {
    Move opposite();
}

class SingleMove implements Move {
    Piece piece;
    Empty empty;
    Direction pieceDirection;

    public SingleMove(Piece piece, Empty empty, Direction pieceDirection) {
        this.piece = piece;
        this.empty = empty;
        this.pieceDirection = pieceDirection;
    }

    @Override
    public Move opposite() {
        return new SingleMove(this.piece, this.empty, this.pieceDirection.opposite());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleMove that = (SingleMove) o;
        return Objects.equals(piece, that.piece) &&
                Objects.equals(empty, that.empty) &&
                pieceDirection == that.pieceDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, empty, pieceDirection);
    }

    @Override
    public String toString() {
        return "SingleMove{ " +
                pieceDirection + " " +
                piece + " " +
                empty +
                " }";
    }
}

class DoubleMove implements Move {
    Piece piece;
    EmptyPair emptyPair;
    Direction pieceDirection;

    public DoubleMove(Piece piece, EmptyPair emptyPair, Direction pieceDirection) {
        this.piece = piece;
        this.emptyPair = emptyPair;
        this.pieceDirection = pieceDirection;
    }

    @Override
    public Move opposite() {
        return new DoubleMove(this.piece, this.emptyPair, this.pieceDirection.opposite());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMove that = (DoubleMove) o;
        return Objects.equals(piece, that.piece) &&
                Objects.equals(emptyPair, that.emptyPair) &&
                pieceDirection == that.pieceDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, emptyPair, pieceDirection);
    }

    @Override
    public String toString() {
        return "DoubleMove{ " +
                pieceDirection + " " +
                piece + " " +
                emptyPair +
                " }";
    }
}

enum Direction {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    int col, row;

    Direction(int col, int row) {
        this.col = col;
        this.row = row;
    }

    Direction opposite() {
        return Direction.values()[(this.ordinal() + 2) % 4];
    }
}

/**
 * A multidimensional piece on the board.
 */
class Piece {
    int row, col, w, h;
    /** In basic case the value represents color information, e.g. 'd' as 'dark gray', 'g' as 'gray', 'E' as 'empty'. */
    char value;
    /** Additional information to enable distinguishing similar pieces. */
    char discriminator;

    Piece() {
    }

    public Piece(int row, int col, int h, int w, char value, char discriminator) {
        this.row = row;
        this.col = col;
        this.w = w;
        this.h = h;
        this.value = value;
        this.discriminator = discriminator;
    }

    public Piece(Piece piece) {
        this(piece.row, piece.col, piece.h, piece.w, piece.value, piece.discriminator);
    }

    public void move(Move move) {
        if (move instanceof SingleMove) {
            SingleMove singleMove = (SingleMove)move;
            this.row = this.row + singleMove.pieceDirection.row;
            this.col = this.col + singleMove.pieceDirection.col;
        } else {
            DoubleMove doubleMove = (DoubleMove)move;
            this.row = this.row + doubleMove.pieceDirection.row;
            this.col = this.col + doubleMove.pieceDirection.col;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        boolean b = row == piece.row &&
                col == piece.col &&
                w == piece.w &&
                h == piece.h &&
                value == piece.value
                // The discriminator is irrelevant from the equality point of view.
                //&& discriminator = piece.discriminator
                ;
        return b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, w, h, value);
    }

    @Override
    public String toString() {
        return "Piece{" +
                "row=" + row +
                ", col=" + col +
                ", w=" + w +
                ", h=" + h +
                ", value=" + value +
                ", discriminator=" + discriminator +
                '}';
    }
}

class Empty extends Piece implements Comparable {

    public Empty(int row, int col, char value, char discriminator) {
        this.row = row;
        this.col = col;
        this.w = 1;
        this.h = 1;
        this.value = value;
        this.discriminator = discriminator;
    }

    public Empty(Empty empty) {
        this(empty.row, empty.col, empty.value, empty.discriminator);
    }

    public void move(Move move) {
        if (move instanceof SingleMove) {
            SingleMove singleMove = (SingleMove)move;

            Direction emptyDirection = singleMove.pieceDirection.opposite();

            this.row = this.row + emptyDirection.row * singleMove.piece.h;
            this.col = this.col + emptyDirection.col * singleMove.piece.w;
        } else {
            DoubleMove doubleMove = (DoubleMove)move;

            Direction emptyDirection = doubleMove.pieceDirection.opposite();

            this.row = this.row + emptyDirection.row * doubleMove.piece.h;
            this.col = this.col + emptyDirection.col * doubleMove.piece.w;
        }
    }

    @Override
    public String toString() {
        return "Empty{" +
                "[" + row +
                "," + col +
//                ", w=" + w +
//                ", h=" + h +
//                ", value=" + value +
//                ", discriminator=" + discriminator +
                "]}";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Comparing two empty cells is inevitable because they are handled as a two-dimensional ordered array for proper comparision.
     */
    @Override
    public int compareTo(Object o) {
        if (null == o)
            throw new IllegalArgumentException();
        if (getClass() != o.getClass())
            throw new IllegalArgumentException();

        Empty other = (Empty) o;

        if (this.col == other.col)
            return this.row < other.row ? -1 : (this.row > other.row ? 1 : 0);
        else if (this.row == other.row)
            return this.col < other.col ? -1 : (this.col > other.col ? 1 : 0);

        return 0;
    }
}

/**
 * A pair of empty cells.
 */
class EmptyPair {

    Empty[] empties;

    public EmptyPair(Empty a, Empty b) {
        this.empties = new Empty[]{a, b};

        Arrays.sort(this.empties);
    }

    public EmptyPair(EmptyPair emptyPair) {
        this(new Empty(emptyPair.empties[0]), new Empty(emptyPair.empties[1]));
    }

    boolean areVerticallyAdjacent() {
        return empties[0].col == empties[1].col && Math.abs(empties[0].row - empties[1].row) == 1;
    }

    boolean areHorizontallyAdjacent() {
        return empties[0].row == empties[1].row && Math.abs(empties[0].col - empties[1].col) == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmptyPair emptyPair = (EmptyPair) o;
        return Arrays.equals(empties, emptyPair.empties);
    }

    @Override
    public int hashCode() {
        int result = 1;

        for (Empty empty : empties)
            result = 31 * result + empty.hashCode();

        return result;
    }

    @Override
    public String toString() {
        return "EmptyPair{[" +empties[0]+", "+empties[1]+"]}";
    }
}
