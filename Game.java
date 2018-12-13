//////////////////////////////////////////////////
////////////
/////////// FINAL PROJECT
/////////// CS112 WITH SCOTT ALFELD
/////////// MARGARET MEDINA-PENA AND BONNIE LIN
///////////
//////////////////////////////////////////////////


// import java packages
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.Font;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.io.FileNotFoundException;

public class Game extends JPanel implements KeyListener{
  final int FPS = 600; // designate constant frame per second for animation
  final int SCREENWIDTH = 1000; // popup window, wider than the grid to allow for the margin
  final int WIDTH = 800; // allows for 20 20 pixel columns
  final int HEIGHT = 800; // allows for 20 20 pixel rows
  int TIME_GIVEN = 100; // amount of time to be added for countdown
  int numberOfWords = 6; // choose x number of words from category
  ArrayList<String> wordList; // save strings from category into ArrayList
  Board gameBoard = null; // placeholder for the Board object
  boolean validDirection = false; //
  boolean enter = false; //
  boolean choosingWord = false; // to differentiate the space bars function
  long startTime = 0; // start with 0 ms
  String STATE = "menu"; // default state is menu
  String CATEGORY; // use this string for accessing the text file
  String CategoryToPrint; //use this string to display in Game string
  long realScore = 0; // print the score after alterations from time/shuffle
  long timeRemaining; // countdown display
  long endTime; // use System time to determine the endTime
  long extraPTS = 0; // award extra points for finding words exceptionally fast


  // Game constructor
  public Game(){
    this.setPreferredSize(new Dimension(SCREENWIDTH, HEIGHT));
    addKeyListener(this);
    gameBoard = new Board();
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

  // initial creation of the word bank and fill the board
  private void setup(){
    ArrayList<String> wordList = readInText(CATEGORY);
    String[] currentWords = getWords(numberOfWords, wordList);
    gameBoard.fillBoard(currentWords);
  }

  // subsequent filling up of the board with shuffle call
  private void setup(int countOfValidWords){
    ArrayList<String> wordList = readInText(CATEGORY);
    String[] currentWords = getWords(numberOfWords - countOfValidWords, wordList);
    gameBoard.fillBoard(currentWords);
  }

  // given the text file name, create the ArrayList
  public static ArrayList<String> readInText(String filename) {
    File file = new File(filename);
    ArrayList<String> wordBank = new ArrayList<String>();
    try {
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        wordBank.add(sc.nextLine());
      }
      sc.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("File not found.");
    }
    return wordBank;
  }

  // randomly extract the specified number of words from the ArrayList
  public static String[] getWords(int n, ArrayList<String> words) {
    Random r = new Random();
    String[] toReturn = new String[n+1];
    for (int i = 0; i < n; i++) {
      int w = r.nextInt(words.size());
      toReturn[i] = words.get(w);
    }
    toReturn[n] = "ALFELD";
    return toReturn;
  }

  // animation magic
  class Runner implements Runnable{
    public void run(){
      while(true){
        repaint();
        try{
          Thread.sleep(800/FPS);
        }
        catch(InterruptedException e){}
        }
      }
    }

    // activates filling in the userWords array with pointer
    public void trackOfKeys(){
      if(gameBoard.userIndexX == 20){
        gameBoard.resetUserWords(gameBoard.userWords);
        choosingWord = false;
      }
      else{
        // for(int i = 0; i < gameBoard.userIndexX; i++ ){
        //      g.setColor(Color.GREEN);
        //      g.fillRect(gameBoard.userWords[i][0], gameBoard.userWords[i][1], 40, 40);}
        gameBoard.board[(gameBoard.currentY/40)][(gameBoard.currentX/40)].selected = new Color(0,0,0);
        gameBoard.userWords[gameBoard.userIndexX][0] = gameBoard.currentX;
        gameBoard.userWords[gameBoard.userIndexX][1] = gameBoard.currentY;
        gameBoard.userIndexX += 1;
      }
    }

    // dependent on the STATE of the program
    // takes in all of the user input from the keyboard to redirect the game
    public void keyPressed(KeyEvent e) {
      int code = e.getKeyCode();
      if(STATE.equals("quit") || STATE.equals("ranOutofTime")){
        if(code == 'm' || code == 'M'){
          extraPTS = 0;
          realScore = 0;
          gameBoard.score = 0;
          gameBoard.countOfValidWords = 0;
          gameBoard = new Board();
          STATE = "menu";
          setup();
        }
        if(code == 'r' || code == 'R'){
          extraPTS = 0;
          realScore = 0;
          gameBoard.score = 0;
          gameBoard.countOfValidWords = 0;
          gameBoard = new Board();
          startTime = System.currentTimeMillis();
          STATE = "game";
          setup();
        }
      }
      else if(STATE.equals("game")){
        if (code == KeyEvent.VK_DOWN) {
          if(gameBoard.currentY >= 760){
          }
          else if(gameBoard.currentY < 800){
            gameBoard.currentY = gameBoard.currentY + 40;
          }
          if (choosingWord == true){
            trackOfKeys();
          }
        }
        else if (code == KeyEvent.VK_UP) {
          if(gameBoard.currentY <= 0){
          }
          else if(gameBoard.currentY > 0){
            gameBoard.currentY = gameBoard.currentY - 40;
          }
          if (choosingWord == true){
            trackOfKeys();
          }
        }
        else if (code == KeyEvent.VK_LEFT) {
          if(gameBoard.currentX <= 0){
          }
          else if(gameBoard.currentX > 0){
            gameBoard.currentX = gameBoard.currentX - 40;
          }
          if (choosingWord == true){
            trackOfKeys();
          }
        }
        else if (code == KeyEvent.VK_RIGHT) {
          if(gameBoard.currentX >= 760){
          }
          else if(gameBoard.currentX < 760){
            gameBoard.currentX = gameBoard.currentX + 40;
          }
          if (choosingWord == true){
            trackOfKeys();
          }
        }
        else if (code == KeyEvent.VK_SPACE && choosingWord == true) {
          enter = true;
          //trackOfKeys();
          for(int i = 0; i < gameBoard.userIndexX-1; i++){
            if(gameBoard.userWords[i][0] == gameBoard.userWords[i+1][0] &&
            gameBoard.userWords[i][1] == (gameBoard.userWords[i+1][1])-40) {
              //  if(gameBoard.userWords[i+1][1] == gameBoard.userWords[i][1] + 40){
              validDirection = true;
            }
            // checking valid right direction
            else if(gameBoard.userWords[i][0] == (gameBoard.userWords[i+1][0])-40 &&
            gameBoard.userWords[i][1] == gameBoard.userWords[i+1][1]){
              validDirection = true;
            }
            else {
              gameBoard.resetUserWords(gameBoard.userWords);
              gameBoard.userWords = new int[20][2];
              validDirection = false;
              choosingWord = false;
            }
          }
        }
        else if (code == KeyEvent.VK_SPACE && choosingWord == false) {
          gameBoard.board[(gameBoard.currentY/40)][(gameBoard.currentX/40)].selected = new Color(0,0,0);
          choosingWord = true;
          gameBoard.userIndexX = 0;
          gameBoard.userWords[gameBoard.userIndexX][0] = gameBoard.currentX;
          gameBoard.userWords[gameBoard.userIndexX][1] = gameBoard.currentY;
          gameBoard.userIndexX += 1;
        }
        else if(code == 's' || code == 'S'){
          gameBoard.TIME_GIVEN_board -= 15;
          setup(gameBoard.countOfValidWords);
        }

        else if(code == 'q' || code == 'Q'){
          STATE = "quit";
         }
      }

      if(STATE.equals("menu")){
        if(code == '1'){
          startTime = System.currentTimeMillis();
          CATEGORY = "AnimalsCategory.txt";
          CategoryToPrint = "Category: Animals";
          STATE = "game";
          setup();
        }
        else if(code == '2'){
          startTime = System.currentTimeMillis();
          CATEGORY = "ColorsCategory.txt";
          CategoryToPrint = "Category: Colors";
          STATE = "game";
          setup();
        }
        else if(code == '3'){
          startTime = System.currentTimeMillis();
          CATEGORY = "StateCapitalsCategory.txt";
          CategoryToPrint = "Category: Capitals";
          STATE = "game";
          setup();
        }
        else if(code == '4'){
          startTime = System.currentTimeMillis();
          CATEGORY = "BodyPartsCategory.txt";
          CategoryToPrint = "Category: Body Parts";
          STATE = "game";
          setup();
        }
        else if(code == '5'){
          startTime = System.currentTimeMillis();
          CATEGORY = "LocksCategory.txt";
          CategoryToPrint = "Category: Locks";
          STATE = "game";
          setup();
        }
      }

      else if( STATE.equals("gameComplete")){
        if(code == 'm' || code == 'M'){
          gameBoard.countOfValidWords = 0;
          STATE = "menu";
          gameBoard = new Board();
          setup();
        }
      }

    }

    // ensure that the keyPressed method works
    public void keyTyped(KeyEvent e) {
      int code = e.getKeyCode();
    }

    // ensure that the keyPressed method works
    public void keyReleased(KeyEvent e) {
      int code = e.getKeyCode();
    }

    public void addNotify() {
      super.addNotify();
      requestFocus();
    }

    // dependent on STATE
    // generate the board
    public void paintComponent(Graphics g) {

      if (STATE.equals("menu")){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREENWIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.BOLD, 45));
        String a = "Welcome to MegaWordSearch!";
        g.drawString(a, (SCREENWIDTH/10) +25, HEIGHT/5);

        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        String b = "To play, select on your keyboard the number of the category you wish to play in!";
        g.drawString(b, (SCREENWIDTH/4)-160, (HEIGHT/4)+ 15);
        String c = "Once you select a category, you have 3 minutes to identify 6 words";
        g.drawString(c, (SCREENWIDTH/4)-100, (2*HEIGHT)/5);
        String d = "Be ready - once you enter the category the game will begin!";
        g.drawString(d, (SCREENWIDTH/4)-75,  HEIGHT/3);

        g.setFont(new Font("TimesRoman", Font.BOLD, 35));
        String e = "1 - Animals             3 - US Capitals";
        String f = "2 - Colors               4 - Body Parts";
        String h = "5 - Locks";
        g.drawString(e, (SCREENWIDTH/4)-75,  ((2*HEIGHT)/3) -90);
        g.drawString(f, (SCREENWIDTH/4)-75,  ((2*HEIGHT)/3)-40);
        g.drawString(h, (SCREENWIDTH/4)+90,  ((2*HEIGHT)/3)+10);

        g.setFont(new Font("TimesRoman", Font.BOLD, 25));
        String m = "Directions: To play the game, you use the arrow keys to move the blue cursor";
        String n = "over the first letter of the desired word. Hit SPACEBAR. Use the arrow keys";
        String o = "to go directly over the remaining letters of the desired word. When at the end,";
        String p = "press SPACEBAR again. ";
        g.drawString(m, (SCREENWIDTH/10),  HEIGHT-120);
        g.drawString(n, (SCREENWIDTH/10),  HEIGHT -90);
        g.drawString(o, (SCREENWIDTH/10),  HEIGHT-60);
        g.drawString(p, (SCREENWIDTH/10), HEIGHT - 30);

      }

      if(STATE.equals("quit")){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREENWIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.BOLD, 45));
        String a = "No quitting here, try again!";
        g.drawString(a, SCREENWIDTH/10, HEIGHT/5);
        g.setFont(new Font("TimesRoman", Font.BOLD, 25));
        String b = "This is how many words you found: " + realScore;
        String c = "To restart this game, type: R";
        String d = "To go back to the menu, type: M";
        g.drawString(b, (SCREENWIDTH/5), (HEIGHT/3));
        g.drawString(c, (SCREENWIDTH/4), (HEIGHT/2)+50);
        g.drawString(d, (SCREENWIDTH/4)-30, (2*HEIGHT)/3);
      }

      if(STATE.equals("ranOutofTime")){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREENWIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.BOLD, 45));
        String a = "You ran out of time!";
        g.drawString(a, SCREENWIDTH/5, HEIGHT/5);
        g.setFont(new Font("TimesRoman", Font.BOLD, 25));
        String b = "This is how many words you found: " + gameBoard.countOfValidWords;
        String c = "To restart this game, type: R";
        String d = "To go back to the menu, type: M";
        g.drawString(b, (SCREENWIDTH/5), (HEIGHT/3));
        g.drawString(c, (SCREENWIDTH/4), (HEIGHT/2)+50);
        g.drawString(d, (SCREENWIDTH/4)-30, (2*HEIGHT)/3);
      }

      if(STATE.equals("game")){
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, SCREENWIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        for(int i = 0; i < HEIGHT + 40; i = i + 40){
          for(int j = 0; j < WIDTH; j = j + 40){
            g.drawLine(i, j, i, HEIGHT);
            g.drawLine(i, j, WIDTH, j);
          }
        }
        if(enter == true){
          if(validDirection == true){
            gameBoard.selectLetter(g);
          }
          if(validDirection == false){
            gameBoard.resetUserWords(gameBoard.userWords);
          }
        }
        gameBoard.drawPointer(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.BOLD, 30));
        gameBoard.drawLetters(g);
        gameBoard.drawScore(g);
        g.drawString(CategoryToPrint,825, 50);
        endTime = System.currentTimeMillis();
        TIME_GIVEN = gameBoard.TIME_GIVEN_board;
        timeRemaining = TIME_GIVEN - ((endTime - startTime)/1000);
        if(timeRemaining > 0){
          long min = timeRemaining/60;
          long secs = timeRemaining%60;
          if (secs < 10) {
            g.drawString("Time: " + Long.toString(min) + ":0" + Long.toString(secs), 850, 200);
          }
          else {
            g.drawString("Time: " + Long.toString(min) + ":" + Long.toString(secs), 850, 200);
          }
        }
        else{
          STATE = "ranOutofTime";
        }

        if(gameBoard.countOfValidWords == numberOfWords){
          if (timeRemaining > 15)
          extraPTS = timeRemaining/15;
          realScore +=  (gameBoard.score + extraPTS);
          STATE = "gameComplete";
          gameBoard.countOfValidWords = 0;

        }
      }

      if(STATE.equals("gameComplete")){

        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREENWIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.BOLD, 50));
        String a = "You found all of the words!";
        g.drawString(a, SCREENWIDTH/15, HEIGHT/5);
        g.setFont(new Font("TimesRoman", Font.BOLD, 10));
        String d = "You finished with " + timeRemaining + " seconds left. At 1 extra point per 15 seconds, you earned an extra " + extraPTS + " points!";
        g.drawString(d, 0, HEIGHT);
        g.setFont(new Font("TimesRoman", Font.BOLD, 25));
        String b = "To go back to the menu, type: M";
        g.drawString(b, (SCREENWIDTH/5), (HEIGHT/3));
        String c = "This is your total score so far: " + (realScore);
        g.drawString(c, (SCREENWIDTH/6), (HEIGHT/2));
      }

    }



    ////////////////////////////////
    ////////////////////////////////
    /////// BOARD Class
    ////////////////////////////////
    ////////////////////////////////

    public class Board {
      public final int N = 20; // length of square board
      Letter[][] board = new Letter[N][N]; // create an N*N array of Letters
      int[][] blankPositionMatrix = new int[N][N]; // create a matrix with 1's and 0's that maps the availability of non blank letters
      int currentX = 0; //x position of the pointer box
      int currentY = 0; //y position of the pointer box
      int[][] userWords = new int[20][2]; //create userWOrds array that holds the position of selected letters
      int userIndexX = 0; //index of the userWords array
      int countOfValidWords = 0; // increases as the player successfully finds words
      int TIME_GIVEN_board = 100; // the number of seconds to initialize the board
      Random rand = new Random(); // random object useful for generating random characters and positions
      int score;

      public Board() {
        for (int i = 0; i < N; i++) {
          for (int j = 0; j < N; j++) {
            board[i][j] = new Letter();
            blankPositionMatrix[i][j] = 1;
          }
        }
      }

      // populate the board with letters from the words and random characters and assign powerup characteristics
      public void fillBoard(String[] words) {
        for(int i = 0; i < N; i++){
          for(int j = 0; j < N; j++){
            board[i][j].color = new Color(255, 255, 255);
          }
        }

        // create a character array placeholder
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
            fits = isValidPlacement(test, row, col, wordLength, down, gameBoard.blankPositionMatrix);
          }
          for (int z = 0; z < wordLength; z++) {
            test[row][col] = words[i].charAt(z);
            if (down == true)
            row += 1;
            else
            col += 1;
          }
          // to populate words in alternating directions
          if (down == true)
          down = false;
          else
          down = true;
        }

        // once we verify that the word fits in the matrix
        // we feed the letters from the test array into the board
        for (int q = 0; q < N; q++) {
          for (int s = 0; s < N; s++) {
            if (blankPositionMatrix[q][s] == 1){
              char c  = test[q][s];
              if (c != ' '){
                board[q][s].Char = test[q][s];
                board[q][s].flashWord = true;
              }
            }
          }
        }
        //MAKE RED POWERUP
        while(true){
          int row2 = r.nextInt(20);
          int col2 = r.nextInt(20);
          if(board[row2][col2].Char != ' ' ){
            board[row2][col2].color = new Color(255,0,0);
            break;
          }
        }
        //MAKE GREEN POWERUP
        while(true){
          int row2 = r.nextInt(20);
          int col2 = r.nextInt(20);
          if(board[row2][col2].Char != ' '){
            if((board[row2][col2].color).equals(new Color(255, 255, 255))){
              board[row2][col2].color = new Color(0,255,0);
              break;
            }
          }
        }
        //MAKE PURPLE POWERUP
        while(true){
          int row2 = r.nextInt(20);
          int col2 = r.nextInt(20);
          if(board[row2][col2].Char != ' ' ){
            if(!(board[row2][col2].color).equals(new Color(255, 0, 0)) && !(board[row2][col2].color).equals(new Color(0, 255, 0))){
              board[row2][col2].color = new Color(63,31,105);
              break;
            }
          }
        }
        // MAKE GOLD POWERUP
        while(true){
          int row2 = r.nextInt(20);
          int col2 = r.nextInt(20);
          if(board[row2][col2].Char != ' ' ){
            if(!(board[row2][col2].color).equals(new Color(255, 0, 0)) && !(board[row2][col2].color).equals(new Color(63,31,105)) && !(board[row2][col2].color).equals(new Color(0, 255, 0))){
              board[row2][col2].color = new Color(255,215,0);
              break;
            }
          }
        }

        for (int q = 0; q < N; q++) {
          for (int s = 0; s < N; s++) {
            if(blankPositionMatrix[q][s] == 1){
              char c  = test[q][s];
              if (c == ' '){
                board[q][s].Char = (char)(r.nextInt(26)+65);
              //  board[q][s].Char = '*';
              }
            }
            board[q][s].position.x = 40*s;
            board[q][s].position.y = 40*q;
          }

        }
      }// fillBoard method

      public boolean isValidPlacement(char[][] test, int row, int col, int length, boolean down, int[][] blankPositionMatrix) {
        for (int x = 0; x < length; x++) {
          if (test[row][col] != ' ' || blankPositionMatrix[row][col] == 0){
            return false;
          }
          else {
            if (down == true)
            row += 1;
            else
            col += 1;
          }
        }
        return true;
      }

      // highlight the box to navigate the player
      public void drawPointer(Graphics g){
        g.setColor(Color.BLUE);
        g.fillRect(currentX, currentY, 40, 40);
      }

      // draw the letters into the board
      public void drawLetters(Graphics g){
        for (int i = 0; i < N; i++){
          for(int j = 0; j < N; j ++){
            board[i][j].draw(g);
          }
        }
      }

      // print in the margins the score
      public void drawScore(Graphics g){
        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g.drawString("# of Found Words: " + Integer.toString(countOfValidWords), 810, 100);
      }


      public void selectLetter(Graphics g){
        for(int i = 0; i < gameBoard.userIndexX; i++ ){
          g.setColor(Color.GREEN);
          g.fillRect(gameBoard.userWords[i][0], gameBoard.userWords[i][1], 40, 40);

        }
        saveWord(userWords);
      }

      // parse characters into string
      public void saveWord(int[][] userWords){
        char[] c = new char[N];
        for(int i = 0; i < userIndexX; i++){
          c[i] = board[(userWords[i][1])/40][(userWords[i][0])/40].Char;
        }
        String userInputString = new String(c);
        checkWord(userInputString);
      }

      // compare string with the word bank
      public void checkWord(String userInputString){
        boolean validWord = false;
        ArrayList<String> wordList = readInText(CATEGORY);
        wordList.add("ALFELD");
        for(String word : wordList){
          userInputString = userInputString.trim();
          word = word.trim();
          if ((userInputString).equals(word)){
            validWord = true;
            break;
          }
        }
        if(validWord == true){
          //put in validWord boolean to update if this is true;
          score++;
          countOfValidWords ++;
          update(userWords);
        }

        if(validWord == false){
          resetUserWords(userWords);
        }
        choosingWord = false;
      }

      // update() removes word + calls sift to update the board
      public void update(int[][] userWords){

        for(int i = 0; i < userIndexX ; i++){

          //RED-POWERUP: adds 30 seconds to timer
          if((board[(userWords[i][1])/40][(userWords[i][0])/40].color).equals(new Color(255, 0, 0))){
            TIME_GIVEN_board = TIME_GIVEN_board + 30;
          }

          //GREEN-POWERUP: switch rows and columns -- different orientation
          else if((board[(userWords[i][1])/40][(userWords[i][0])/40].color).equals(new Color(0,255,0))){
            for(int j = 0; j < 20; j++){
              for(int k = 0; k < j; k++){
                char hold = board[j][k].Char;
                int holdforBlank = blankPositionMatrix[j][k];
                board[j][k].Char = board[k][j].Char;
                blankPositionMatrix[j][k] = blankPositionMatrix[k][j];
                board[k][j].Char = hold;
                blankPositionMatrix[k][j] = holdforBlank;
                // Letter hold = board[j][k];
                // int holdforBlank = blankPositionMatrix[j][k];
                // board[j][k] = board[k][j];
                // blankPositionMatrix[j][k] = blankPositionMatrix[k][j];
                // board[k][j] = hold;
                // blankPositionMatrix[k][j] = holdforBlank;
              }
            }
          }

          // MAMMOTH PURPLE-POWERUP: flash the answers against the Honor Code, no biggie)
          else if((board[(userWords[i][1])/40][(userWords[i][0])/40].color).equals(new Color(63,31,105))){
            for(int j = 0; j < N; j++){
              for(int k = 0; k < N; k++){
                if(board[j][k].flashWord == true){
                  board[j][k].color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                }
              }
            }
          }

          // GOLD-POWERUP: clears all powerups
          else if((board[(userWords[i][1])/40][(userWords[i][0])/40].color).equals(new Color(255,215,0))){
            for(int j = 0; j < N; j++){
              for(int k = 0; k < N; k++){
                board[j][k].color = new Color(255, 255, 255);
              }
            }
          }
        }

        // for(int i = 0; i < userIndexX ; i++){
        //   board[(userWords[i][1])/40][(userWords[i][0])/40].color = new Color(255,255,255);
        //   board[(userWords[i][1])/40][(userWords[i][0])/40].Char = ' ';
        //   blankPositionMatrix[(userWords[i][1])/40][(userWords[i][0])/40] = 0;
        //   board[(userWords[i][1])/40][(userWords[i][0])/40].selected = new Color(255,255,255);
        // }

         for(int i = 0; i < userIndexX ; i++){
          board[(userWords[i][1])/40][(userWords[i][0])/40].color = new Color(255,255,255);
          board[(userWords[i][1])/40][(userWords[i][0])/40].Char = ' ';
          blankPositionMatrix[(userWords[i][1])/40][(userWords[i][0])/40] = 0;
          board[(userWords[i][1])/40][(userWords[i][0])/40].selected = new Color(255,255,255);
        }
        choosingWord = false;
        resetUserWords(userWords);
        siftDown();

      }


      // resetUserWords() resets the array to allow user to select another word
      public void resetUserWords(int[][] userWords){
        for(int i = 0; i < 20; i++){
         board[(userWords[i][1])/40][(userWords[i][0])/40].selected = new Color(255,255,255);
}
         for(int i =0; i<20;i++){
          userWords[i][0] = 0;
          userWords[i][1] = 0;
        }
        userIndexX = 0;
        validDirection = false;
        enter = false;
      }

      // change the position of the blank cells so that they are on the top of the grid
      public void siftDown() {
        for (int i = 0; i < 20; i++) {
          for (int j = 18; j >= 0; j--) {
            for (int k = 0; k < N; k++) {
              if (board[j+1][k].Char == ' ') {
                char hold = board[j][k].Char;
                int holdforBlank = blankPositionMatrix[j][k];
                board[j][k].Char = board[j+1][k].Char;
                blankPositionMatrix[j][k] = blankPositionMatrix[j + 1][k];
                board[j+1][k].Char = hold;
                blankPositionMatrix[j + 1][k] = holdforBlank;
              }
            }
          }
        }
      }

    } // Board class

    ////////////////////////////////
    /////// LETTER Class
    ////////////////////////////////

    class Letter{
      char Char; // char holds the letters
      Pair position; // (x, y) to hold the position in the grid
      Color color; // color of the string
      Color selected; // color for highlight
      boolean flashWord; // all words selected from the list should be able to flash under the activation of the mammoth purple powerup

      public Letter(){
        Random r = new Random();
        position = new Pair(0.0, 0.0);
        color = new Color(255, 255, 255);
        selected = new Color(255, 255, 255);
        Char = ' ';
        flashWord = false;
      }

      // draw the character with the designated color
      public void draw(Graphics g){
        if (color.equals(new Color(255,255,255))) {
          g.setColor(selected);
        }
        else
        g.setColor(color);
        String s = Character.toString(Char);
        g.drawString(s, (int)position.x + 15, (int)position.y + 35);

      }
    }



      ////////////////////////////////
      /////// PAIR Class
      /////// TAKEN FROM OOBOUNCING
      ////////////////////////////////

      class Pair{
        double x;
        double y;
        public Pair(double x, double y){
          this.x = x;
          this.y = y;
        }
      }
    }// Game class
