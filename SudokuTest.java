import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class SudokuTest extends TestCase {
  
  /**
   * A test method.
   * (Replace "X" with a name describing the test.  You may write as
   * many "testSomething" methods in this class as you wish, and each
   * one will be called when running JUnit over this class.)
   */

  public void testboard() {
    Sudoku puzzle = new Sudoku("028007000016083070000020851137290000000730000000046307290070000000860140000300700");
    puzzle.board();
  }
  
  public void testcandidates(){
    Sudoku hiddenSingles = new Sudoku("412736589000000106568010370000850210100000008087090000030070865800000000000908401");
  // Bring up a candidate example and make the comparison
  }
  
  public void testisSolved(){
    Sudoku puzzle = new Sudoku("028007000016083070000020851137290000000730000000046307290070000000860140000300700");
    puzzle.isSolved();
  }ass
  
  public void testsolve(){
    Sudoku puzzle = new Sudoku("028007000016083070000020851137290000000730000000046307290070000000860140000300700");
    puzzle.solve();
  }
  
  public void testnakedSingles(){
    Sudoku hiddenSingles = new Sudoku("412736589000000106568010370000850210100000008087090000030070865800000000000908401");
  // Naked single 6 at (F,7)
    
  }
  
  public void testhiddenSingles(){
    Sudoku hiddenSingles = new Sudoku("412736589000000106568010370000850210100000008087090000030070865800000000000908401");
  // Hidden single 8 at (B,5)
  // Hidden single 9 at (C,6)
  // Hidden single 5 at (F,8)
  // Hidden single 3 at (I,8)
   
  }
}
