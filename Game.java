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
import java.io.FileNotFoundException;

public class Game extends JPanel implements KeyListener{
  final int FPS = 60;
  final int SCREENWIDTH = 1000;
  final int WIDTH = 800;
  final int HEIGHT = 800;
  final int TIME_GIVEN = 10;
  int numberOfWords = 6;
  ArrayList<String> wordList = readInText("AnimalsCategory.txt");
  Board gameBoard = null;
  boolean validDirection = false;
  boolean enter = false;
  boolean choosingWord = false;
  long startTime = System.currentTimeMillis();
  String STATE = "menu";
  String CATEGORY;


  public Game(){
    this.setPreferredSize(new Dimension(SCREENWIDTH, HEIGHT));
    addKeyListener(this);
    gameBoard = new Board();
    //String[] currentWords = getWords(numberOfWords, wordList);
    //gameBoard.fillBoard(currentWords);
    // gameBoard.saveWord()
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

  private void setup(){
    ArrayList<String> wordList = readInText(CATEGORY);
    String[] currentWords = getWords(numberOfWords, wordList);
    gameBoard.fillBoard(currentWords);
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

    public void trackOfKeys(){
      if(gameBoard.userIndexX == 20){
        System.out.println("Invalid Word Entry - try again!");
        gameBoard.resetUserWords(gameBoard.userWords);
        choosingWord = false;
      }
      else{
        gameBoard.userWords[gameBoard.userIndexX][0] = gameBoard.currentX;
        gameBoard.userWords[gameBoard.userIndexX][1] = gameBoard.currentY;
        System.out.println("Blue box position:" + gameBoard.currentX + ", " + gameBoard.currentY);
        System.out.println("User letter choice position:" + gameBoard.userWords[gameBoard.userIndexX][0] + ", " + gameBoard.userWords[gameBoard.userIndexX][1]);
        gameBoard.userIndexX += 1;
      }
    }


    public void keyPressed(KeyEvent e) {
      int code = e.getKeyCode();
      if(STATE.equals("game")){
      if (code == KeyEvent.VK_DOWN) {
        System.out.println("In DOWN");
        gameBoard.currentY = gameBoard.currentY + 40;
        if (choosingWord == true){
          System.out.println("In DOWN  w/ choosingWord TRUE");
          trackOfKeys();
        }
      }
      else if (code == KeyEvent.VK_UP) {
        System.out.println("In UP");
        gameBoard.currentY = gameBoard.currentY - 40;
        if (choosingWord == true){
          System.out.println("In UP  w/ choosingWord TRUE");
          trackOfKeys();
        }
      }
      else if (code == KeyEvent.VK_LEFT) {
        System.out.println("In LEFT");
        gameBoard.currentX = gameBoard.currentX - 40;
        if (choosingWord == true){
          System.out.println("In LEFT  w/ choosingWord TRUE");
          trackOfKeys();
        }
      }
      else if (code == KeyEvent.VK_RIGHT) {
        System.out.println("In RIGHT");
        gameBoard.currentX = gameBoard.currentX + 40;
        if (choosingWord == true){
          System.out.println("In RIGHT  w/ choosingWord TRUE");
          trackOfKeys();
        }
      }
      else if (code == KeyEvent.VK_SPACE && choosingWord == true) {
        enter = true;
        System.out.println("In Space w/ choosingWord TRUE");
        // trackOfKeys();
        System.out.println(gameBoard.userIndexX);
        for(int i = 0; i < gameBoard.userIndexX-1; i++){
          if(gameBoard.userWords[i][0] == gameBoard.userWords[i+1][0] &&
          gameBoard.userWords[i][1] == (gameBoard.userWords[i+1][1])-40) {
            //  if(gameBoard.userWords[i+1][1] == gameBoard.userWords[i][1] + 40){
            System.out.println("Down direction worked");
            validDirection = true;
          }
          // checking valid right direction
          else if(gameBoard.userWords[i][0] == (gameBoard.userWords[i+1][0])-40 &&
          gameBoard.userWords[i][1] == gameBoard.userWords[i+1][1]){
            System.out.println("Right direction worked");
            validDirection = true;
          }
          else {
            System.out.println("Invalid direction - create new word");
            gameBoard.resetUserWords(gameBoard.userWords);
            gameBoard.userWords = new int[20][2];
            validDirection = false;
            choosingWord = false;
          }
        }
        System.out.println(choosingWord);
      }
      else if (code == KeyEvent.VK_SPACE && choosingWord == false) {
        System.out.println("In Space w/ choosingWord FALSE");
        choosingWord = true;
        System.out.println("choosingWord = " + choosingWord);
        gameBoard.userIndexX = 0;
        gameBoard.userWords[gameBoard.userIndexX][0] = gameBoard.currentX;
        gameBoard.userWords[gameBoard.userIndexX][1] = gameBoard.currentY;
        System.out.println("Position of letter that begins word:" + gameBoard.currentX + ", " + gameBoard.currentY);
        System.out.println("User letter choice position:" + gameBoard.userWords[gameBoard.userIndexX][0] + ", " + gameBoard.userWords[gameBoard.userIndexX][1]);
        gameBoard.userIndexX += 1;
      }
      else if(code == 's' || code == 'S'){
        System.out.println("Shuffle was attempted");
        String[] currentWords = getWords(numberOfWords - gameBoard.countOfValidWords, wordList);
        gameBoard.dotheShuffle(currentWords);
      }
      else if(code == 'q' || code == 'Q'){
        STATE = "quit";
      }
    }


    if(STATE.equals("menu")){
      if(code == '1'){
          CATEGORY = "AnimalsCategory.txt";
          STATE = "game";
          setup();
      }
      else if(code == '2'){
          CATEGORY = "ColorsCategory.txt";
          STATE = "game";
          setup();
      }
      else if(code == '3'){
          CATEGORY = "StateCapitalsCategory.txt";
          STATE = "game";
          setup();
      }
      else if(code == '4'){
          CATEGORY = "Furniture.txt";
          STATE = "game";
          setup();
      }
    }

    if(STATE.equals("quit")){
      if(code == 'm' || code == 'M'){
        STATE = "menu";
      }
      if(code == 'r' || code == 'R'){
        STATE = "game";
      }
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

      if (STATE.equals("menu")){
    	    super.paintComponent(g);
    	    g.setColor(Color.BLACK);
    	    g.fillRect(0, 0, SCREENWIDTH, HEIGHT);
    	    g.setColor(Color.WHITE);
    	    g.setFont(new Font("TimesRoman", Font.BOLD, 45));
    	    String a = "Welcome to MegaWordSearch!";
    	    g.drawString(a, SCREENWIDTH/10, HEIGHT/5);

    	    g.setFont(new Font("TimesRoman", Font.BOLD, 20));
    	    String b = "To play, select on your keyboard the number of the category you wish to play in!";
    	    g.drawString(b, (SCREENWIDTH/4)-215, (HEIGHT/4)+ 15);
    	    String c = "Once you select a category, you have 3 minutes to identify 6 words";
    	    g.drawString(c, (SCREENWIDTH/4)-125, (2*HEIGHT)/5);
    	    String d = "Be ready - once you enter the category the game will begin!";
    	    g.drawString(d, (SCREENWIDTH/4)-75,  HEIGHT/3);

    	    g.setFont(new Font("TimesRoman", Font.BOLD, 35));
    	    String e = "1 - Animals             3 - US Capitals";
    	    String f = "2 - Colors                4 - Furniture";
    	    g.drawString(e, (SCREENWIDTH/4)-75,  (2*HEIGHT)/3);
    	    g.drawString(f, (SCREENWIDTH/4)-75,  ((2*HEIGHT)/3)+ 50);
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
      for(int i = 0; i < HEIGHT; i = i + 40){
        for(int j = 0; j < WIDTH; j = j + 40){
          g.drawLine(i, j, i, HEIGHT);
          g.drawLine(i, j, WIDTH, j);
        }
      }
      if(enter == true){
        if(validDirection == true){
          //System.out.println("Words were removed");
          gameBoard.selectLetter(g);
        }
        if(validDirection == false){
          //System.out.println("In if statements that call update" + validDirection);
          gameBoard.resetUserWords(gameBoard.userWords);
        }
      }
      gameBoard.drawPointer(g);
      g.setColor(Color.WHITE);
      g.setFont(new Font("TimesRoman", Font.BOLD, 30));
      gameBoard.drawLetters(g);
      gameBoard.drawScore(g);
      long endTime = System.currentTimeMillis();
      long timeRemaining = TIME_GIVEN - ((endTime - startTime)/1000);
      if(timeRemaining > 0){
        g.drawString("Time: " + Long.toString(timeRemaining), 800, 200);
      }
      else{
        g.drawString("Out of Time ", 800, 200);
      }
    }

    }




    ////////////////////////////////
    /////// BOARD Class
    ////////////////////////////////


    public class Board {
      public final int N = 20; // length of square board
      Letter[][] board = new Letter[N][N];
      int currentX = 0;
      int currentY = 0;
      int userIndexX = 0;
      //int userIndexY = 0 ;
      int[][] userWords = new int[20][2];
      int inputLettersIndex = -1;
      int countOfValidWords = 0;
      int[][] blankPositionMatrix = new int[N][N];

      public Board() {
        for (int i = 0; i < N; i++) {
          for (int j = 0; j < N; j++) {
            board[i][j] = new Letter();
            blankPositionMatrix[i][j] = 1;
          }
        }
      }

      /* public void fillBoard(String[] words) {
        char[][] test = new char[N][N];
        // create a character array placeholder
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
          while (fits == true) {
            wordLength = words[i].length();
            if (down == true) { // word going down
              row = r.nextInt(N - wordLength);
              col = r.nextInt(N);
            }
            else { // word going right
              col = r.nextInt(N - wordLength);
              row = r.nextInt(N);
            }
            // for (int x = 0; x < wordLength; x++) {
            //   while(blankPositionMatrix[row][col] == 1){
            //     if (test[row][col] != ' '){
            //       fits = false;
            //     }
            //     else {
            //       if (down == true)
            //       row += 1;
            //       else
            //       col += 1;
            //     }
            //   }
            // }
            // fits = true;
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
              if (c == ' '){
                //board[q][s].Char = (char)(r.nextInt(26)+65);
                board[q][s].Char = '*';
              }
              else{
                board[q][s].Char = test[q][s];
              }
            }
            else{
              board[q][s].Char = ' ';
            }
            board[q][s].position.x = 40*s;
            board[q][s].position.y = 40*q;
          }
        }
} // fillBoard method
*/
public void fillBoard(String[] words) {
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

      for (int x = 0; x < wordLength; x++) {
        if (test[row][col] != ' '){
          if (test[row][col] == 0)
            fits = false;
        else {
          if (down == true)
          row += 1;
          else
          col += 1;
        }
      }
      fits = true;
    }
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
        if (c == ' '){
          //board[q][s].Char = (char)(r.nextInt(26)+65);
          board[q][s].Char = '*';
        }
        else{
          board[q][s].Char = test[q][s];
        }
      }
      else{
        board[q][s].Char = ' ';
      }
      board[q][s].position.x = 40*s;
      board[q][s].position.y = 40*q;


    }
  }

      /*
      if (test[q][s] != ' '){
      if(blankPositionMatrix[q][s] == 1){
      board[q][s].Char = test[q][s];
      //goodShuffle = true;
    }
    else if(blankPositionMatrix[q][s] == 0){
    board[q][s].Char = ' ';
  }
}
else{
// uncomment two lines below to see asterisks instead of random letters
// String ast = "*";
// board[q][s].Char = ast.charAt(0);
board[q][s].Char = (char)(r.nextInt(26)+65);
}
board[q][s].position.x = 40*s;
board[q][s].position.y = 40*q;
}
}
}

*/
} // fillBoard method

public boolean isValidPlacement(char[][] test, int row, int col, int length, boolean down, int[][] blankPositionMatrix) {
  for (int x = 0; x < length; x++) {
    if (test[row][col] != ' '){
      if(blankPositionMatrix[row][col] == 0){
        return false;
      }
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

// public boolean isValidPlacement(char[][] test, int row, int col, int length, boolean down, int[][] blankPositionMatrix) {
//   for (int x = 0; x < length; x++) {
//     //while(blankPositionMatrix[row][col] == 1){
//       if (test[row][col] != ' '){
//         return false;
//       }
//       else {
//         if (down == true)
//         row += 1;
//         else
//         col += 1;
//       }
//     }
//   //}
//   return true;
// }

public void drawPointer(Graphics g){
  g.setColor(Color.BLUE);
  g.fillRect(currentX, currentY, 40, 40);
}

public void drawLetters(Graphics g){
  for (int i = 0; i < N; i++){
    for(int j = 0; j < N; j ++){
      board[i][j].draw(g);
    }
  }
}


public void drawScore(Graphics g){
  g.setFont(new Font("TimesRoman", Font.BOLD, 20));
  g.drawString("# of Found Words: " + Integer.toString(countOfValidWords), 800, 100);
}

public void selectLetter(Graphics g){
  for(int i = 0; i < gameBoard.userIndexX; i++ ){
    g.setColor(Color.GREEN);
    g.fillRect(gameBoard.userWords[i][0], gameBoard.userWords[i][1], 40, 40);
  }
  saveWord(userWords);
}

public void saveWord(int[][] userWords){
  char[] c = new char[N];
  for(int i = 0; i < userIndexX; i++){
    c[i] = board[(userWords[i][1])/40][(userWords[i][0])/40].Char;
  }
  String userInputString = new String(c);
  System.out.println("String is: "+ userInputString);

  checkWord(userInputString);
}

////CHANGE THIS TO MAKE TEXT READ FROM USER CHOSEN CATEGORY
public void checkWord(String userInputString){
  boolean validWord = false;
  ArrayList<String> wordList = readInText("AnimalsCategory.txt");
  for(String word : wordList){
    userInputString = userInputString.trim();
    word = word.trim();
    System.out.println("In checkWord for loop");
    System.out.println("userInputString: " + userInputString);
    if ((userInputString).equals(word)){
      System.out.println("Word in list being checked: " +word);
      System.out.println("Use word being checked: "+ userInputString);
      validWord = true;
      break;
    }
  }
  if(validWord == true){
    //put in validWord boolean to update if this is true;
    countOfValidWords ++;
    System.out.println(countOfValidWords);
    System.out.println("Word was found in text file");
    update(userWords);
  }

  if(validWord == false){
    System.out.println("Invalid selection - try again");
    resetUserWords(userWords);
  }
  choosingWord = false;
}

// update() removes word + calls sift to update the board
public void update(int[][] userWords){
  System.out.println("Went into update");
  for(int i = 0; i < userIndexX ; i++){
    board[(userWords[i][1])/40][(userWords[i][0])/40].Char = ' ';
    blankPositionMatrix[(userWords[i][1])/40][(userWords[i][0])/40] = 0;
  }
  choosingWord = false;
  resetUserWords(userWords);
  siftDown();
}

// resetUserWords() resets the array to allow user to select another word
// XX MIGHT BE THE PROBLEM FOR THE OUTOFBOUNDS CASE
public void resetUserWords(int[][] userWords){
  System.out.println("Went into resetUserWords method");
  for(int i = 0; i < 20; i++){
    userWords[i][0] = 0;
    userWords[i][1] = 0;
  }
  userIndexX = 0;
  validDirection = false;
  enter = false;
}

public void siftDown() {
  System.out.println("SiftDown method got called");
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

public void dotheShuffle(String[] currentWords){
  System.out.println("Shuffle is called");
  gameBoard.fillBoard(currentWords);
}

}


////////////////////////////////
/////// LETTER Class
////////////////////////////////

class Letter{
  char Char;
  Pair position;
  Pair velocity;
  Color color;
  Pair size;
  boolean isBlank;

  public Letter(){
    Random r = new Random();
    position = new Pair(0.0, 0.0);
    velocity = new Pair(0.0, 0.0);
    color = new Color(255, 255, 255);
    Char = ' ';
  }


  public void draw(Graphics g){
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

// class TimerListener implements ActionListener{
//   int elapsedSecond = 30;
//
//   public void actionPerformed(ActionEvent evt){
//     elapsedSecond--;
//     timerLabel.setText(elapsedSecond);
//     if(elapsedSeconds <= 0){
//       timer.stop();
//       wrong();
//     }
//   }
// }



} // Game class
