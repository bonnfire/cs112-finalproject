import java.util.*;
import java.io.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.*;
import java.util.Random;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Game extends JPanel implements KeyListener{
  final int FPS = 60;
  final int WIDTH = 1000;
  final int HEIGHT = 1000;
  Board gameBoard = null;
  boolean select = false;


public Game(){
  this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
  addKeyListener(this);
  // gameBoard = new Board();

  gameBoard = new Board();
  ArrayList<String> wordList = readInText("AnimalsCategory.txt");
  int numberOfWords = 6;
  String[] currentWords = getWords(numberOfWords, wordList);
  gameBoard.fillBoard(currentWords);
	Thread mainThread = new Thread(new Runner());
	mainThread.start();
}
  public static void main (String[] args) {
    JFrame frame = new JFrame("WordSearch");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new Game());
    frame.pack();
    frame.setVisible(true);
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

  class Runner implements Runnable{
public void run(){
    while(true){
 repaint();
  try{
      Thread.sleep(1000/FPS);
  }
  catch(InterruptedException e){}
    }
}
  }

  public void keyPressed(KeyEvent e) {
          int code = e.getKeyCode();
              if (code == KeyEvent.VK_DOWN) {
                gameBoard.currentY = gameBoard.currentY + 50;
                gameBoard.trackY = gameBoard.trackY + 1;
              }
              if (code == KeyEvent.VK_UP) {
                gameBoard.currentY = gameBoard.currentY - 50;
                gameBoard.trackY = gameBoard.trackY - 1;

              }
              if (code == KeyEvent.VK_LEFT) {
                gameBoard.currentX = gameBoard.currentX - 50;
                gameBoard.trackX = gameBoard.trackX - 1;
              }
              if (code == KeyEvent.VK_RIGHT) {
                gameBoard.currentX = gameBoard.currentX + 50;
                gameBoard.trackX = gameBoard.trackX + 1;
              }
              if (code == KeyEvent.VK_SPACE) {
                  select = true;
                  gameBoard.userWord[gameBoard.userIndexX][gameBoard.userIndexY] = gameBoard.currentX;
                  gameBoard.userIndexY += 1;
                  gameBoard.userWord[gameBoard.userIndexX][gameBoard.userIndexY] = gameBoard.currentY;
                  gameBoard.userIndexX += 1;

              }
              if(code == '\n'){
                select = false;
                for(int i = 0; i < gameBoard.userIndexX; i++ ){
                gameBoard.userWord[i][0] = 0;
                gameBoard.userWord[i][1] = 0;
              }
              gameBoard.userIndexX = 0;
              gameBoard.userIndexY = 0;
            }
          }

      public void keyTyped(KeyEvent e) {
        int code = e.getKeyCode();
      }

      public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
      }

      public void addNotify() {
          super.addNotify();
          requestFocus();
      }


// generate the board
public void paintComponent(Graphics g) {
  super.paintComponent(g);
  g.printBoard(g);
  gameBoard.drawPointer(g);

if(select == true){
  for(int i = 0; i < gameBoard.userIndexX; i++ ){
      g.setColor(Color.GREEN);
      g.fillRect(gameBoard.userWord[i][0], gameBoard.userWord[i][1], 50, 50);
    }
    gameBoard.userIndexY = 0;
}
  g.setColor(Color.WHITE);
  g.setFont(new Font("TimesRoman", Font.BOLD, 35));
  for(int i = 0; i < 20; i++){
    for(int j = 0; j < 20; j++){
     g.drawString(String.valueOf(gameBoard.board[i][j].letter), i * 50 + 5, j * 50 + 40);
    }
  }
}

public class Board {
  public static int N = 20; // length of square board
  Letter[][] board = new Letter[N][N];
  int currentX = 0;
  int currentY = 0;
  int trackX = 0;
  int trackY = 0;
  int userIndexX = 0;
  int userIndexY = 0 ;
  int[][] userWord = new int[20][2];
  int inputLettersIndex = -1;
  String userInputString = null;
  // public static boolean select = false;

  public Board() {
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        board[i][j] = new Letter();
      }
    }
  }

  public void printBoard(Graphics g) {
    // System.out.println();
    // for (int i = 0; i < N; i++) {
    //   for (int j = 0; j < N; j++) {
    //     System.out.print(board[i][j].letter + " ");
    //   }
    //   System.out.println();
    // }
    setColor(Color.GRAY);
    g.fillRect(0, 0, WIDTH, HEIGHT);
    g.setColor(Color.WHITE);
    for(int i = 0; i < HEIGHT; i = i + 50){
      for(int j = 0; j < WIDTH; j = j + 50){
        g.drawLine(i, j, i, HEIGHT);
        g.drawLine(i, j, WIDTH, j);
      }
    }
  }

  public void drawLetters(Graphics g){
          for (int i = 0; i < 20; i++){
              for (int j =0; j ,20; j++){
              Letters[i][j].draw(g);
          }
      }
}

public void updateLetters(Board b, Letter[][] userWord){//ADD AND CONFIRM ARGUMENTS
    for (int i = 0; i < 20; i ++){
      for (int j = 0, j <20; i ++){
        Letters[i][j].update(b, userWord);
}

  public void fillBoard(String[] words) {
    char[][] test = new char[N][N];
    for (int a = 0; a < N; a++) {
      for (int b = 0; b < N; b++) {
        test[a][b] = ' ';
      }
    }
    boolean fits = false;
    Random r = new Random();
    boolean down = true; // go down first
    int col = 0;
    int row = 0;
    int wordLength = 0;
    for (int i = 0; i < words.length; i++) {
      fits = false;
      while (fits == false) {
        wordLength = words[i].length();
        if (down == true) { // word going down
          row = r.nextInt(N - wordLength);
          col = r.nextInt(N);
        }
        else { // word going right
          col = r.nextInt(N - wordLength);
          row = r.nextInt(N);
        }
        fits = isValidPlacement(test, row, col, wordLength, down);
      }
      for (int z = 0; z < wordLength; z++) {
        test[row][col] = words[i].charAt(z);
        if (down == true)
          row += 1;
        else
          col += 1;
      }
      if (down == true)
        down = false;
      else
        down = true;
    }
    for (int q = 0; q < N; q++) {
      for (int s = 0; s < N; s++) {
        if (test[q][s] != ' ') {
          board[q][s].letter = test[q][s];
        }
        else
          board[q][s].letter = (char)(r.nextInt(26)+65);
      }
    }
  }

  public static boolean isValidPlacement(char[][] test, int row, int col, int length, boolean down) {
    for (int x = 0; x < length; x++) {
      if (test[row][col] != ' ')
        return false;
      else {
        if (down == true)
          row += 1;
        else
          col += 1;
      }
    }
    return true;
  }

  public void drawPointer(Graphics g){
      g.setColor(Color.BLUE);
      g.fillRect(currentX, currentY, 50, 50);
    }



  public void remove(int startX, int startY, int endX, int endY) {
    if (startX == endX) {
      for (int i = startY; i < endY; i++){
        board[i][endX].letter = ' ';
      }
    }

    else {
      for (int i = startX; i < endX; i++){
        board[endY][i].letter = ' ';
      }
    }
    siftDown();
  }

  public void selectLetter(Graphics g){
// Letter.selected = true;
    makeWord();
  }

  public void makeWord(){

  }

  public void siftDown() {
    for (int i = 0; i < N; i++) {
      for (int j = N-2; j >= 0; j--) {
        for (int k = 0; k < N; k++) {
          if (board[j+1][k].letter == ' ') {
            char hold = board[j][k].letter;
            board[j][k].letter = board[j+1][k].letter;
            board[j+1][k].letter = hold;
          }
        }
      }
    }
  }

  // public String getChar(String[] words){
  //   for(int k = 0; k < words.length - 1; k++){
  //   for(int l = 0; l < words[l].length() - 1; l++)
  //         test[row][col] = words[i].charAt(z);
  // }
} // class Board


///////////////////////////////////////
///////////// CLASS LETTER
//////////////////////////////////////
  public class Letter {
    char letter;
    Pair position;
    Pair velocity;
    final static Pair size;
    String color;
    // Boolean selected = false;

    public Letter() {
      Random r = new Random();
      letter = (char)(r.nextInt(26)+65);
      position = new Pair(0.0, 0.0);
      velocity = new Pair(0.0, 0.0);
      size = new Pair(50.0, 50.0);
      color = new Color(255, 255, 255);
    }

    public void update(Board b, Letters[][] userWord){
for (int i = 0; i )
      position.x = position.add(50 * lettersToRemove);
    }

//ERASEEEEEEEEEEEEEEE//
//
//
//
//

    public void remove(int startX, int startY, int endX, int endY) {
      if (startX == endX) {
        for (int i = startY; i < endY; i++){
          board[i][endX].letter = ' ';
        }
      }

      else {
        for (int i = startX; i < endX; i++){
          board[endY][i].letter = ' ';
        }
      }
      siftDown();
    }

    public void selectLetter(Graphics g){
      // g.setColor(Color.GREEN);
      //
      // g.fillRect(currentX, currentY, 50, 50);
    //  userWord[][] = board[trackX][trackY];
    //  userInputString = String.valueOf(userWord[inputLettersIndex]);
      //System.out.println(userInputString);
    }

    public void saveWord(){

    }

    public void siftDown() {
      for (int i = 0; i < N; i++) {
        for (int j = N-2; j >= 0; j--) {
          for (int k = 0; k < N; k++) {
            if (board[j+1][k].letter == ' ') {
              char hold = board[j][k].letter;
              board[j][k].letter = board[j+1][k].letter;
              board[j+1][k].letter = hold;
            }
          }
        }
      }
    }
    //ERASEEEEEEEEEEEEEEE
//
//
///
//


    public void draw(Graphics g){
      g.setColor(color);
      String s = Character.toString(letter);
      g.drawString(s, position.x, position.y);
    }

} // class letter


class Pair{
  double x;
  double y;
  public Pair(double x, double y){
    this.x = x;
    this.y = y;
  }
  public Pair add(Pair p){
  Pair pair = new Pair(0, 0);
  pair.x = this.x + p.x;
  pair.y = this.y + p.y;
  return pair;
}
  public Pair times(double t){
    Pair pair = new Pair(0, 0);
    pair.x = this.x * t;
    pair.y = this.y * t;
    return pair;
  }
  public void flipX(){
    this.x = - this.x;
    this.y = this.y;
  }
  public void flipY(){
    this.x = this.x;
    this.y = - this.y;
  }
  public Pair divide(double d){
    Pair pair = new Pair(0, 0);
    pair.x = this.x / d;
    pair.y = this.y / d;
    return pair;
  }
}



} // Game class
