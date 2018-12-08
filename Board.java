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

public class Board {
  public static int N = 20; // length of square board
  Letter[][] board = new Letter[N][N];
  int currentX = 0;
  int currentY = 0;
  int trackX = 0;
  int trackY = 0;
  int userIndexX = 0;
  int userIndexY = 0 ;
  int[][] userInputLetters = new int[20][2];
  int inputLettersIndex = -1;
  String userInputString = null;
  // public static boolean select = false;

  public Board() {
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        board[i][j] = new Letter(' ', i, j, 0, "RED");
      }
    }
  }

  public void printBoard() {
    System.out.println();
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        System.out.print(board[i][j].letter + " ");
      }
      System.out.println();
    }
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
    // g.setColor(Color.GREEN);
    //
    // g.fillRect(currentX, currentY, 50, 50);
  //  userInputLetters[][] = board[trackX][trackY];
  //  userInputString = String.valueOf(userInputLetters[inputLettersIndex]);
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

  // public String getChar(String[] words){
  //   for(int k = 0; k < words.length - 1; k++){
  //   for(int l = 0; l < words[l].length() - 1; l++)
  //         test[row][col] = words[i].charAt(z);
  // }


}
