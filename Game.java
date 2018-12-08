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
  boolean validDirection = true;


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
    //gameBoard.printBoard();
    //
    // int placeholder = getUserInput();
    //
    // int startX = 0;
    // int startY = 0;
    // int endX = 0;
    // int endY = 0;
    // // gameBoard.remove(10, 10, 19, 10);
    // // gameBoard.printBoard();
    // // gameBoard.remove(2, 3, 2, 18);
    // // gameBoard.printBoard();
    //
    //
    JFrame frame = new JFrame("WordSearch");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new Game());
    frame.pack();
    frame.setVisible(true);
  }



// public void play() {
//   ArrayList<String> wordList = readInText("AnimalsCategory.txt");
//
//   // gameBoard = new Board();
//
//   int numberOfWords = 6;
//   String[] currentWords = getWords(numberOfWords, wordList);
//
//   this.gameBoard.fillBoard(currentWords);
//   this.gameBoard.printBoard();
//
//   int placeholder = getUserInput();
//
//   int startX = 0;
//   int startY = 0;
//   int endX = 0;
//   int endY = 0;
//   // gameBoard.remove(10, 10, 19, 10);
//   // gameBoard.printBoard();
//   // gameBoard.remove(2, 3, 2, 18);
//   // gameBoard.printBoard();
//
//
//   JFrame frame = new JFrame("WordSearch");
//   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//   frame.setContentPane(new Game());
//   frame.pack();
//   frame.setVisible(true);
// }
// public void fillGraphicsBoard(String[] currentWords){
//   gameBoard = gameBoard.fillBoard(currentWords);
// }

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
                  gameBoard.userInputLetters[gameBoard.userIndexX][gameBoard.userIndexY] = gameBoard.currentX;
                  gameBoard.userIndexY += 1;
                  gameBoard.userInputLetters[gameBoard.userIndexX][gameBoard.userIndexY] = gameBoard.currentY;
                  gameBoard.userIndexX += 1;

                  while(gameBoard.userInputLetters[1][0] != null){
                  for(int i = 0; i < 20; i++){
                      if(gameBoard.userInputLetters[i][0] == gameBoard.userInputLetters[i+1][0] &&
                      gameBoard.userInputLetters[i][1] != gameBoard.userInputLetters[i+1][1]){
                        if(gameBoard.userInputLetters[i+1][1] == gameBoard.userInputLetters[i][1] + 50){
                        validDirection = true;
                      }
                      }
                      else if(gameBoard.userInputLetters[i][0] != gameBoard.userInputLetters[i+1][0] &&
                        gameBoard.userInputLetters[i][1] == gameBoard.userInputLetters[i+1][1]){
                          if(gameBoard.userInputLetters[i+1][0] == gameBoard.userInputLetters[i][0] + 50){
                            validDirection = true;
                          }
                        }
                  }
                }
              }
                else {
                  validDirection = false;
                }
                  System.out.println(validDirection);
                }
                if(code == '\n'){
                select = false;
                for(int i = 0; i < gameBoard.userIndexX; i++ ){
                gameBoard.userInputLetters[i][0] = 0;
                gameBoard.userInputLetters[i][1] = 0;
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
  g.setColor(Color.GRAY);
  g.fillRect(0, 0, WIDTH, HEIGHT);
  g.setColor(Color.WHITE);
  for(int i = 0; i < HEIGHT; i = i + 50){
    for(int j = 0; j < WIDTH; j = j + 50){
      g.drawLine(i, j, i, HEIGHT);
      g.drawLine(i, j, WIDTH, j);
    }
  }
  gameBoard.drawPointer(g);

if(select == true){
  for(int i = 0; i < gameBoard.userIndexX; i++ ){
    // for (int j = 0; j <  gameBoard.userIndexY ; j++){
      g.setColor(Color.GREEN);
      g.fillRect(gameBoard.userInputLetters[i][0], gameBoard.userInputLetters[i][1], 50, 50);

    //}
    }
    gameBoard.userIndexY = 0;
  //  gameBoard.selectLetter(g);
}
  g.setColor(Color.WHITE);
  g.setFont(new Font("TimesRoman", Font.BOLD, 35));
  for(int i = 0; i < 20; i++){
    for(int j = 0; j < 20; j++){
      // for(int k = 0; k < currentWords.length - 1; k++){
      // for(int l = 0; l < currentWords[l].length() - 1; l++)
     g.drawString(String.valueOf(gameBoard.board[i][j].letter), i * 50 + 5, j * 50 + 40);
    }
  }
}



} // Game class
