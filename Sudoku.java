import java.util.*;

class BoardValidityException extends RuntimeException {
    public BoardValidityException(int val, int valRow, int valCol, int conRow, int conCol, String checkType) {
        super("Cell with value " + val + " at (" + valRow + ", " + valCol + ") conflicts with (" + conRow + ", " + conCol + ") in " + checkType + " check");
    }
}

public class Sudoku {

/******************************************************
                      Variables
*******************************************************/

    final int BOARD_ROOT = 3;
    final int BOARD_SQUR = BOARD_ROOT * BOARD_ROOT;
    final int BOARD_ROWS = BOARD_SQUR;
    final int BOARD_COLS = BOARD_SQUR;
    final int BOARD_CELS = BOARD_ROWS * BOARD_COLS;

    private int[][] board;

/******************************************************
                    Public Methods
*******************************************************/

    public Sudoku() {
        // Generate empty board
        int[][] board = new int[BOARD_ROWS][BOARD_COLS];
        for(int i = 0; i < BOARD_ROWS; i++)
            for(int j = 0; j < BOARD_ROWS; j++)
                board[i][j] = 0;
        this.board(board);
    }

    public Sudoku(int[][] board) {
        this.board(board);
    }

    public Sudoku(String lineBoard) {
        this.board(this.parseString(lineBoard));
    }

    // Return the current board
    public int[][] board() {
        return this.board;
    }

    // Set the board by given value
    public void board(int[][] board) {
        if(!this.validateBoard(board))
            throw new RuntimeException("Invalid board");
        this.board = board;
    }

    // Returns the list of candidates for the specified
    // cell. The array contains true at index i if i is
    // a candidate for the cell at row and column, so
    // long as the cell is not already set (non-zero),
    // in which case there are no candidates.
    public boolean[] candidates(int row, int col, int[][] board) {
        if(board[row][col] != 0)
            return null;
        boolean[] retSet = new boolean[BOARD_SQUR + 1];
        for(int i = 1; i < BOARD_SQUR; i++)
            retSet[i] = true;
        // Check row
        for(int c = 0; c < BOARD_COLS; c++) {
            int i = board[row][c];
            if(i > 0)
                retSet[i] = false;
        }
        // Check column
        for(int r = 0; r < BOARD_ROWS; r++) {
            int i = board[r][col];
            if(i > 0)
                retSet[i] = false;
        }
        // Check sub-block
        int rowBoxOffset = (row / BOARD_ROOT) * BOARD_ROOT;
        int colBoxOffset = (col / BOARD_ROOT) * BOARD_ROOT;
        for(int i = 0; i < BOARD_ROOT; ++i) {
            for(int j = 0; j < BOARD_ROOT; ++j) {
                int val = board[rowBoxOffset + i][colBoxOffset + j];
                if(val > 0)
                    retSet[val] = false;
            }
        }
        return retSet;
    }

    public boolean[] candidates(int row, int col) {
        return this.candidates(row, col, this.board);
    }

    public int[] candidatesList(int row, int col, int[][] board) {
        boolean[] candidates = this.candidates(row, col, board);
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 1; i <= BOARD_SQUR; i++)
            if(candidates[i])
                list.add(i);
        return toIntArray(list);
    }

    public int[] candidatesList(int row, int col) {
        return this.candidatesList(row, col, this.board);
    }

    // Returns true if made any changes
    public boolean nakedSingles() {
        // TODO
        return true;
    }

    // Returns true if made any changes
    public boolean hiddenSingles() {
        // TODO
        return true;
    }

    // Returns true if the board is in a solved state
    public boolean isSolved() {
        // TODO
        return true;
    }

    // Attempts to solve the board. Exits when board
    // is solved or no updates were made to the board
    public void solve() {
        while (!this.isSolved() &&
              (this.nakedSingles() || this.hiddenSingles()));
    }

    public String toString() {
        String ret = "";
        for(int row = 0; row < BOARD_ROWS; row++)
            for(int col = 0; col < BOARD_COLS; col++)
                ret += this.board[row][col];
        return ret;
    }

    public void print() {
        String firstLine = "//";
        String lineBreak = "  +";
        for(int colGroup = 0; colGroup < BOARD_COLS / BOARD_ROOT; colGroup++) {
            firstLine += "  ";
            lineBreak += "-";
            for(int i = 0; i < BOARD_ROOT; i++)
                lineBreak += "--";
            lineBreak += "+";
            for(int col = colGroup * BOARD_ROOT; col < (colGroup + 1) * BOARD_ROOT; col++)
                firstLine += (col + 1) + " ";
        }
        firstLine = firstLine.replace("\\s*$", "");
        System.out.println(firstLine);
        System.out.println(lineBreak);
        for(int rowGroup = 0; rowGroup < BOARD_ROWS / BOARD_ROOT; rowGroup++) {
            for(int row = rowGroup * BOARD_ROOT; row < (rowGroup + 1) * BOARD_ROOT; row++) {
                String line = ((char)(65 + row)) + " |";
                for(int colGroup = 0; colGroup < BOARD_COLS / BOARD_ROOT; colGroup++) {
                    for(int col = colGroup * BOARD_ROOT; col < (colGroup + 1) * BOARD_ROOT; col++) {
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

/******************************************************
                    Private Methods
*******************************************************/

    // Check the subunits of a certain cell to see if the number
    // filled in the cell is legal.
    private boolean isLegal(int row, int col, int val, int[][] board, boolean throwExcption) {
        if(val == 0)
            return true;
        // Check sub-row
        for(int c = 0; c < BOARD_COLS; c++)
            if(c != col && val == board[row][c]) {
                if(throwExcption)
                    throw new BoardValidityException(val, row, col, row, c, "row");
                return false;
            }
        // Check sub-column
        for(int r = 0; r < BOARD_ROWS; r++)
            if(r != row && val == board[r][col]) {
                if(throwExcption)
                    throw new BoardValidityException(val, row, col, r, col, "col");
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
                    if(throwExcption)
                        throw new BoardValidityException(val, row, col, r, c, "box");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isLegal(int row, int col, int val, int[][] board) {
        return this.isLegal(row, col, val, board, false);
    }

    private boolean isLegal(int row, int col) {
        return this.isLegal(row, col, this.board[row][col], this.board);
    }

    private boolean isLegal(int row, int col, boolean throwExcption) {
        return this.isLegal(row, col, this.board[row][col], this.board, throwExcption);
    }

    private boolean isLegal(int row, int col, int val) {
        return this.isLegal(row, col, val, this.board);
    }

    private boolean isLegal(int row, int col, int val, boolean throwExcption) {
        return this.isLegal(row, col, val, this.board, throwExcption);
    }

    private boolean isLegal(int row, int col, int[][] board) {
        return this.isLegal(row, col, board[row][col], board);
    }

    private boolean isLegal(int row, int col, int[][] board, boolean throwExcption) {
        return this.isLegal(row, col, board[row][col], board, throwExcption);
    }

    private boolean validateBoard(int[][] board) {
        if(board.length != BOARD_ROWS)
            return false;
        for(int row = 0; row < BOARD_ROWS; row++) {
            if(board[row].length != BOARD_COLS)
                return false;
            for(int col = 0; col < BOARD_COLS; col++)
                if(!this.isLegal(row, col, board))
                    return false;
        }
        return true;
    }

    private boolean validateBoard() {
        return this.validateBoard(this.board);
    }

    private int[][] parseString(String lineBoard) {
        lineBoard = lineBoard.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "")
                             .replaceAll("\\.", "0")
                             .replaceAll("[^\\d]", "");
        if(lineBoard.length() != BOARD_CELS)
            throw new RuntimeException("Invalid cell number");
        int[][] board = new int[BOARD_ROWS][BOARD_COLS];
        for(int row = 0; row < BOARD_ROWS; row++)
            for(int col = 0; col < BOARD_COLS; col++)
                board[row][col] = Integer.parseInt(
                    String.valueOf(
                        lineBoard.charAt(row * BOARD_COLS + col)));
        return board;
    }

    private int[] toIntArray(List<Integer> list)  {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)  
            ret[i++] = e.intValue();
        return ret;
    }

}
