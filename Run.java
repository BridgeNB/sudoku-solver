import java.io.*;
import java.util.*;

public class Run {
    public static void main(String[] args) {
        Sudoku puzzle = new Sudoku(readFile("board.txt"));
        puzzle.print();
        System.out.println(puzzle.toString());
    }

    public static String readFile(String path) {
        String ret = "";
        try {
            Scanner in = new Scanner(new FileReader(path));
            while (in.hasNext()) {
                ret += in.next();
            }
        } catch(FileNotFoundException fnfe) { 
            System.out.println(fnfe.getMessage());
        }
        return ret;
    }
}