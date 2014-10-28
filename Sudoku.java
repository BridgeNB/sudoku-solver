import java.util.*;

public class Sudoku {

    /**************************************************
                        Variables
    ***************************************************/

    final int BOARD_ROOT = 3;
    final int BOARD_SQUR = BOARD_ROOT * BOARD_ROOT;
    final int BOARD_ROWS = BOARD_SQUR;
    final int BOARD_COLS = BOARD_SQUR;
    final int BOARD_CELS = BOARD_ROWS * BOARD_COLS;

    private int[][] board;

    /**************************************************
                      Public Methods
    ***************************************************/

    public Sudoku() {
        // Generate empty board
        int[][] board = new int[BOARD_ROWS][BOARD_COLS];
        for (int i = 0; i < BOARD_ROWS; i++)
            for (int j = 0; j < BOARD_ROWS; j++)
                board[i][j] = 0;
        this.board(board);
    }

    public Sudoku(int[][] board) {
        this.board(board);
    }

    public Sudoku(String lineBoard) {
        this.board(this.parseString(lineBoard));
    }

    // Return a copy of the current board
    public int[][] board() {
        int[][] ret = new int[BOARD_ROWS][BOARD_COLS];
        for (int row = 0; row < BOARD_ROWS; row++)
            for (int col = 0; col < BOARD_COLS; col++)
                ret[row][col] = this.board[row][col];
        return ret;
    }

    // Set the board by given value
    public void board(int[][] board) {
        if (!this.isValidateBoard(board))
            throw new RuntimeException("Invalid board");
        this.board = board;
    }

    // Returns the list of candidates for the specified
    // cell. The array contains true at index i if i is
    // a candidate for the cell at row and column, so
    // long as the cell is not already set (non-zero),
    // in which case there are no candidates.

    public boolean[] candidates(int row, int col) {
        int bits = this.candidatesBits(row, col);
        boolean[] ret = new boolean[BOARD_SQUR];
        for (int i = BOARD_SQUR - 1; i > 0; i--)
            ret[i] = (bits & (1 << i)) != 0;
        return ret;
    }

    public int candidatesBits(int row, int col) {
        int[][] board = this.board;
        if (board[row][col] != 0)
            return 0;
        int ret = (2 << BOARD_SQUR) - 1;
        // Check row
        for (int c = 0; c < BOARD_COLS; c++)
            ret &= ~(1 << board[row][c]);
        // Check column
        for (int r = 0; r < BOARD_ROWS; r++)
            ret &= ~(1 << board[r][col]);
        // Check sub-block
        int rowBoxOffset = (row / BOARD_ROOT) * BOARD_ROOT;
        int colBoxOffset = (col / BOARD_ROOT) * BOARD_ROOT;
        for (int i = 0; i < BOARD_ROOT; ++i)
            for (int j = 0; j < BOARD_ROOT; ++j)
                ret &= ~(1 << board[rowBoxOffset + i][colBoxOffset + j]);
        return ret >> 1;
    }

    // Returns true if made any changes
    public boolean nakedSingles() {
        boolean ret = false;
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                int candidates = this.candidatesBits(row, col);
                if (this.countBits(candidates) == 1) {
                    int val = this.bitLength(candidates);
                    this.board[row][col] = val;
                    System.out.format("Naked single %d at (%s, %d)%n", val, ((char)(65 + row)), col + 1);
                    ret = true;
                }
            }
        }
        return ret;
    }

    // Returns true if made any changes
    public boolean hiddenSingles() {
        boolean ret = false;
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                int candidates = this.candidatesBits(row, col);
                if (candidates == 0)
                    continue;
                // Check sub-row
                for (int c = 0; c < BOARD_COLS; c++)
                    if (c != col)
                        candidates &= ~this.candidatesBits(row, c);
                if (checkRemainingHiddenSingels(candidates, row, col)) {
                    ret = true;
                    continue;
                }
                // Check sub-column
                for (int r = 0; r < BOARD_ROWS; r++)
                    if (r != row)
                        candidates &= ~this.candidatesBits(r, col);
                if (checkRemainingHiddenSingels(candidates, row, col)) {
                    ret = true;
                    continue;
                }
                // Check sub-block
                int rowBoxOffset = (row / BOARD_ROOT) * BOARD_ROOT;
                int colBoxOffset = (col / BOARD_ROOT) * BOARD_ROOT;
                for (int i = 0; i < BOARD_ROOT; ++i) {
                    for (int j = 0; j < BOARD_ROOT; ++j) {
                        int r = rowBoxOffset + i;
                        int c = colBoxOffset + j;
                        if (!(r == row && c == col))
                            candidates &= ~this.candidatesBits(r, c);
                    }
                }
                if (checkRemainingHiddenSingels(candidates, row, col)) {
                    ret = true;
                    continue;
                }
            }
        }
        return ret;
    }

    // A universal algorithm solving Sudoku
    public void backtrack() {
        int[][] board = this.board();
        this.backtrack(0, 0, board);
        this.board(board);
    }

    // Recursive loop to brute-force the puzzle
    public boolean backtrack(int row, int col, int[][] board) {
        if (row == BOARD_ROWS) {
            row = 0;
            if (++col == BOARD_COLS) {
                return true;
            }
        }
        if (board[row][col] != 0)
            return backtrack(row + 1, col, board);
        for (int val = 1; val <= BOARD_SQUR; ++val) {
            if (this.isLegal(row, col, val, board)) {
                board[row][col] = val;
                if (backtrack(row + 1, col, board))
                    return true;
            }
        }
        board[row][col] = 0;
        return false;
    }

    // Returns true if the board is in a solved state
    public boolean isSolved() {
        for (int row = 0; row < BOARD_SQUR; row++) {
            for (int col = 0; col < BOARD_SQUR; col++) {
                if (this.board[row][col] == 0)
                    return false;
            }
        }
        return true;
    }

    // Attempts to solve the board. Exits when board
    // is solved or no updates were made to the board
    public void solve() {
        while (!this.isSolved() &&
                (this.nakedSingles() || this.hiddenSingles()));
        this.backtrack();
    }

    public String toString() {
        String ret = "";
        for (int row = 0; row < BOARD_ROWS; row++)
            for (int col = 0; col < BOARD_COLS; col++)
                ret += this.board[row][col];
        return ret;
    }

    public void print() {
        String firstLine = "//";
        String lineBreak = "  +";
        for (int colGroup = 0; colGroup < BOARD_COLS / BOARD_ROOT; colGroup++) {
            firstLine += "  ";
            lineBreak += "-";
            for (int i = 0; i < BOARD_ROOT; i++)
                lineBreak += "--";
            lineBreak += "+";
            for (int col = colGroup * BOARD_ROOT; col < (colGroup + 1) * BOARD_ROOT; col++)
                firstLine += (col + 1) + " ";
        }
        firstLine = firstLine.replace("\\s*$", "");
        System.out.println(firstLine);
        System.out.println(lineBreak);
        for (int rowGroup = 0; rowGroup < BOARD_ROWS / BOARD_ROOT; rowGroup++) {
            for (int row = rowGroup * BOARD_ROOT; row < (rowGroup + 1) * BOARD_ROOT; row++) {
                String line = ((char)(65 + row)) + " |";
                for (int colGroup = 0; colGroup < BOARD_COLS / BOARD_ROOT; colGroup++) {
                    for (int col = colGroup * BOARD_ROOT; col < (colGroup + 1) * BOARD_ROOT; col++) {
                        int cell = this.board[row][col];
                        line += " " + (cell > 0 ? cell : ".");
                    }
                    line += " |";
                }
                System.out.println(line);
            }
            System.out.println(lineBreak);
        }
    }

    /**************************************************
                      Private Methods
    ***************************************************/

    // Check the subunits of a certain cell to see if the number
    // filled in the cell is legal.
    private boolean isLegal(int row, int col, int val, int[][] board) {
        if (val == 0)
            return true;
        // Check sub-row
        for (int c = 0; c < BOARD_COLS; c++)
            if (c != col && val == board[row][c]) {
                return false;
            }
        // Check sub-column
        for (int r = 0; r < BOARD_ROWS; r++)
            if (r != row && val == board[r][col]) {
                return false;
            }
        // Check sub-block
        int rowBoxOffset = (row / BOARD_ROOT) * BOARD_ROOT;
        int colBoxOffset = (col / BOARD_ROOT) * BOARD_ROOT;
        for (int i = 0; i < BOARD_ROOT; ++i) {
            for (int j = 0; j < BOARD_ROOT; ++j) {
                int r = rowBoxOffset + i;
                int c = colBoxOffset + j;
                if (!(r == row && c == col) && val == board[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkRemainingHiddenSingels(int candidates, int row, int col) {
        if (this.countBits(candidates) == 1) {
            int val = nextSetBit(candidates, 0) + 1;
            this.board[row][col] = val;
            System.out.format("Hidden single %d at (%s, %d)%n", val, ((char)(65 + row)), col + 1);
            return true;
        }
        return false;
    }

    private boolean isValidateBoard(int[][] board) {
        if (board.length != BOARD_ROWS)
            return false;
        for (int row = 0; row < BOARD_ROWS; row++) {
            if (board[row].length != BOARD_COLS)
                return false;
            for (int col = 0; col < BOARD_COLS; col++)
                if (!this.isLegal(row, col, board[row][col], board))
                    return false;
        }
        return true;
    }

    private int[][] parseString(String lineBoard) {
        lineBoard = lineBoard.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "")
                    .replaceAll("\\.", "0")
                    .replaceAll("[^\\d]", "");
        if (lineBoard.length() != BOARD_CELS)
            throw new RuntimeException("Invalid cell number");
        int[][] board = new int[BOARD_ROWS][BOARD_COLS];
        for (int row = 0; row < BOARD_ROWS; row++)
            for (int col = 0; col < BOARD_COLS; col++)
                board[row][col] = Integer.parseInt(
                                      String.valueOf(
                                          lineBoard.charAt(row * BOARD_COLS + col)));
        return board;
    }

    // Counts number of usable bits
    private int bitLength(int x) {
        return Integer.SIZE - Integer.numberOfLeadingZeros(x);
    }

    // Counts number of non-zero bits
    private int countBits(int x) {
        x = (x >>> 1 & 0x55555555) + (x & 0x55555555);
        x = (x >>> 2 & 0x33333333) + (x & 0x33333333);
        x = (x >>> 4 & 0x0f0f0f0f) + (x & 0x0f0f0f0f);
        x = (x >>> 8 & 0x00ff00ff) + (x & 0x00ff00ff);
        return (x >>> 16) + (x & 0x0000ffff);
    }

    // return the index of the next non-zero bit
    private int nextSetBit(int x, int fromIndex) {
        int length = this.bitLength(x);
        for (int i = length - 1 - fromIndex; i > 0; i--)
            if ((x & (1 << i)) != 0)
                return i;
        return -1;
    }

}
