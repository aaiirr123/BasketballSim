import java.util.*;
import java.io.*;

public class BasketballSim
{
  private Roster home;
  private Roster away;
  BasketballSim() throws FileNotFoundException
  {
    System.out.println("Starting Sim");
    this.home = new Roster(Roster.teamSide.HOME, 10, "Dragons");
    this.away = new Roster(Roster.teamSide.AWAY, 10, "Hawks");
    gameSim game = new gameSim(home, away);
    game.startGame();
  }

  public static void main(String [] args) throws FileNotFoundException
  {
    BasketballSim Aarons = new BasketballSim();
  }
}
