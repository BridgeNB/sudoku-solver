public class Sudoku {

/******************************************************
                      Variables
*******************************************************/

    final int BOARD_ROOT = 3;
    final int BOARD_ROWS = BOARD_ROOT * BOARD_ROOT;
    final int BOARD_COLS = BOARD_ROOT * BOARD_ROOT;
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
        this.board(parseString(lineBoard));
    }

    // Return the current board
    public int[][] board() {
        return this.board;
    }

    // Set the board by given value
    public void board(int[][] board) {
        if(!validateBoard(board))
            throw new RuntimeException("Invalid board");
        this.board = board;
    }

    // Returns the list of candidates for the specified
    // cell. The array contains true at index i if i is
    // a candidate for the cell at row and column, so
    // long as the cell is not already set (non-zero),
    // in which case there are no candidates.
    public boolean[] candidates(int row, int column) {
        // TODO
        return new boolean[]{true};
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
        while (!isSolved() &&
              (nakedSingles() || hiddenSingles()));
    }

    public String toString() {
        String ret = "";
        for(int row = 0; row < BOARD_ROWS; row++)
            for(int col = 0; col < BOARD_COLS; col++)
                ret += this.board[row][col];
        return ret;
    }

    public void print() {
        String firstLine = " ";
        String lineBreak = "  +";
        for(int colGroup = 0; colGroup < BOARD_COLS / BOARD_ROOT; colGroup++) {
            firstLine += "  ";
            lineBreak += "-";
            for(int i = 0; i < BOARD_ROOT; i++)
                lineBreak += "--";
            lineBreak += "+";
            for(int col = colGroup * BOARD_ROOT; col < (colGroup + 1) * BOARD_ROOT; col++)
                firstLine += " " + (col + 1);
        }
        System.out.println(firstLine);
        System.out.println(lineBreak);
        for(int rowGroup = 0; rowGroup < BOARD_ROWS / BOARD_ROOT; rowGroup++) {
            for(int row = rowGroup * BOARD_ROOT; row < (rowGroup + 1) * BOARD_ROOT; row++) {
                String line = ((char)(65 + row)) + " |";
                for(int colGroup = 0; colGroup < BOARD_COLS / BOARD_ROOT; colGroup++) {
                    for(int col = colGroup * BOARD_ROOT; col < (colGroup + 1) * BOARD_ROOT; col++) {
                        int cell = this.board[row][col];
                        line += " " + (cell > 0 ? cell : " ");
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

    private boolean validateBoard(int[][] board) {
        if(board.length != BOARD_ROWS)
            return false;
        for(int i = 0; i < BOARD_ROWS; i++)
            if(board[i].length != BOARD_COLS)
                return false;
        return true;
    }

    private int[][] parseString(String lineBoard) {
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

}
