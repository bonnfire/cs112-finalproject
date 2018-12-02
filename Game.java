import java.util.*;
import java.io.*;
public class Game {

  public static void main (String[] args) {
    ArrayList<String> wordList = readInText("AnimalsCategory.txt");

    Board gameBoard = new Board();

    int numberOfWords = 6;
    String[] currentWords = getWords(numberOfWords, wordList);

    gameBoard.fillBoard(currentWords);
    gameBoard.printBoard();

    int placeholder = getUserInput();

    int startX = 0;
    int startY = 0;
    int endX = 0;
    int endY = 0;
    gameBoard.remove(10, 10, 19, 10);
    gameBoard.printBoard();
    gameBoard.remove(2, 3, 2, 18);
    gameBoard.printBoard();
  }


  public static ArrayList<String> readInText(String filename) {
    File file = new File(filename);
    ArrayList<String> animals = new ArrayList<String>();
    try {
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        animals.add(sc.nextLine());
      }
      sc.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("File not found.");
    }
    return animals;
  }

  public static String[] getWords(int n, ArrayList<String> words) {
    Random r = new Random();
    String[] toReturn = new String[n];
    for (int i = 0; i < n; i++) {
      int w = r.nextInt(words.size());
      toReturn[i] = words.get(w);
    }
    return toReturn;
  }

  public static int getUserInput() {
    return 0;
  }

}
