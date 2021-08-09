package basketballSim;

import sample.GameSimController;

import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class BasketballSim
{
  private Roster home;
  private Roster away;
  private gameSim game;
  private boolean gameStarted = false;
  private boolean gameFinished = false;

  GameSimController controller;
  public BasketballSim(GameSimController controller, Roster userTeam, Roster cpuTeam) throws FileNotFoundException
  {
    System.out.println("Starting Sim");
    this.controller = controller;
    this.home = userTeam;
    // this.home = new Roster(Roster.teamSide.HOME, 10, "Dragons");
    this.away = cpuTeam;
    game = new gameSim(home, away, controller);
    System.out.println(this.home.getHeadCoach().getName());
    System.out.println(this.away.getHeadCoach().getName());
  }
  public void start()
  {
    this.gameStarted = true;
    game.startGame();
    finalStats();
  }

  public double getTime()
  {
    return game.getTotalTime();
  }

  public void addPointsUI(int team, int score)
  {
    if(team == 1) controller.addPoints(1,score);
    else controller.addPoints(2,score);
  }

//  public static void main(String [] args) throws FileNotFoundException
//  {
//    BasketballSim Aarons = new BasketballSim();
//  }

  public void getCurrentStats()
  {

  }
  public String finalStats()
  {
    ArrayList<String> statList = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    double totalT = 0;
    System.out.println("Printing final stats\n\n");
    System.out.println("\n home team final score " + home.getTeamScore());

    int finalScore = 0;
    for (int i = 0; i < home.getNumPlayers(); i++)
    {
      Player holder = home.getPlayer(i);
      System.out.println("\n Player: " + holder.getName());
      sb.append("\n Player: " + holder.getName());
      holder.stats();
      finalScore += holder.getPoints();
      totalT += holder.getMinutes();
    }
    System.out.println("Correct final time " + totalT);


    System.out.println("Correct final score " + finalScore);

    finalScore = 0;
    totalT = 0;

    System.out.println("\n away team final score " + away.getTeamScore());
    for (int i = 0; i < away.getNumPlayers(); i++)
    {
      Player holder = away.getPlayer(i);
      System.out.println("\n Player: " + holder.getName());
      sb.append("\n Player: " + holder.getName());
      holder.stats();
      finalScore += holder.getPoints();
      totalT += holder.getMinutes();
    }
    System.out.println("Correct final score " + finalScore);
    System.out.println("Correct final time " + totalT);

    return sb.toString();
  }
  public boolean isGameStarted()
  {
    return this.gameStarted;
  }

  public boolean isGameFinished()
  {
    return this.gameFinished;
  }

  public Roster getRoster(int teamSide)
  {
    if(teamSide == 1) return this.home;
    return this.away;
  }
}
