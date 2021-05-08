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
    finalStats();
  }

  public static void main(String [] args) throws FileNotFoundException
  {
    BasketballSim Aarons = new BasketballSim();
  }

  private void finalStats()
  {
    System.out.println("Printing final stats\n\n");
    System.out.println("\n home team final score " + home.getTeamScore());
    int finalScore = 0;
    for (int i = 0; i < home.getNumPlayers(); i++)
    {
      Player holder = home.getPlayer(i);
      System.out.println("\n Player: " + holder.getName());
      holder.stats();
      finalScore += holder.getPoints();
    }
    System.out.println("Correct final score " + finalScore);
    finalScore = 0;

    System.out.println("\n away team final score " + away.getTeamScore());
    for (int i = 0; i < away.getNumPlayers(); i++)
    {
      Player holder = away.getPlayer(i);
      System.out.println("\n Player: " + holder.getName());
      holder.stats();
      finalScore += holder.getPoints();
    }
    System.out.println("Correct final score " + finalScore);


  }
}
