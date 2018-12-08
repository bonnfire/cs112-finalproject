import java.awt.Color;
public class Letter {
  char letter;
  int posX;
  int posY;
  int velY;
  String color;

  public Letter(char l, int x, int y, int v, String c) {
    this.letter = l;
    this.posX = x;
    this.posY = y;
    this.velY = v;
    this.color = c;
  }

  public void setVelocity(int v) {
    this.velY = v;
  }
}
