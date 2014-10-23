public class Run {
	public static void main(String[] args) {
		Sudoku puzzle = new Sudoku(
			"000041000"+
			"060000200"+
			"000000000"+
			"320600000"+
			"000050040"+
			"700000000"+
			"000200300"+
			"480000000"+
			"501000000");
		puzzle.print();
		System.out.println(puzzle.toString());
	}
}