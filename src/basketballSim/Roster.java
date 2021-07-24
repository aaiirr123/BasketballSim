package basketballSim;

import java.util.*;
import java.io.*;

public class Roster
{
  public static enum teamSide {
    HOME,
    AWAY
  }

  private ArrayList<Player> team = new ArrayList<>();
  private teamSide side;
  private int numPlayers = 0;
  private Coach headCoach = new Coach();
  private String teamName;
  private Player currentFive[] = new Player[5];
  private ArrayList<Player> benchPlayers = new ArrayList<>();
  private int teamScore = 0;

  Roster(teamSide side, int numPlayers, String teamName, Coach coach) throws FileNotFoundException
  {
    this.side = side;
    this.numPlayers = numPlayers;
    this.teamName = teamName;

    for (int i = 0; i < numPlayers; i++)
    {
      initiatePlayer(new Player(Person.randomName()));
    }

    if (coach == null) initiateCoach(new Coach(Person.randomName()));
    else this.headCoach = coach;

    initiateStarters();
    initiateBenchPlayers();
  }

  Roster(teamSide side, int numPlayers, String teamName, Coach coach, Player[] players) throws FileNotFoundException
  {
    this.side = side;
    this.numPlayers = numPlayers;
    this.teamName = teamName;

    for (Player nextPlayer : players) initiatePlayer(nextPlayer);    

    if (coach == null) initiateCoach(new Coach(Person.randomName()));
    else this.headCoach = coach;

    initiateStarters();
    initiateBenchPlayers();
  }

  private void initiateStarters()
  {
    for (int i = 0; i < 5; i++)
    {
      team.get(i).setPlayerIndex(i);
      currentFive[i] = team.get(i);
      currentFive[i].playerSubIn(0);
    }
  }

  private void initiateBenchPlayers()
  {
    int i = 5;
    while (i < team.size())
    {
      benchPlayers.add(team.get(i));
      i++;
    }
  }



  public Player getOnCourtPlayer(int position)
  {
    return currentFive[position];
  }

  public Player getOnCourtPlayer(Player.Position position)
  {
    int index;

    if (position == Player.Position.PG) index = 1;
    else if (position == Player.Position.SG) index = 2;
    else if (position == Player.Position.SF) index = 3;
    else if (position == Player.Position.PF) index = 4;
    else index = 5;

    return currentFive[index - 1];
  }

  public void subPlayers(Player subIn, Player subOut, int subInIndex)
  {
    currentFive[subInIndex] = subIn;
    subIn.setPlayerIndex(subInIndex);
    benchPlayers.add(subOut);
  }

  private void initiatePlayer(Player newPlayer)
  {
    team.add(newPlayer);
  }

  private void initiateCoach(Coach newCoach)
  {
    headCoach = newCoach;
  }

  public Player getPlayer(int index)
  {
    return team.get(index);
  }

  public String getTeamName()
  {
    return teamName;
  }

  public int getNumPlayers()
  {
    return numPlayers;
  }

  public Player getRandomOnCourt()
  {
    int rand = (int)(5 * Math.random());
    return getOnCourtPlayer(rand);
  }
  // This version excludes current player
  public Player getRandomOnCourt(int exclude)
  {
    int randomPlayer = exclude;
    while (randomPlayer == exclude)
    {
      randomPlayer = (int)(5 * Math.random());
    }
    return getOnCourtPlayer(randomPlayer);
  }

  public Player getRandomOnCourt(Player exclude)
  {
    Player randomPlayer = exclude;
    while (randomPlayer == exclude)
    {
      randomPlayer = getOnCourtPlayer((int)(5 * Math.random()));
    }
    return randomPlayer;
  }

  public Player getBenchPlayer()
  {
    Player benchPlayer;
    int benchIndex = randomNumberGenerator(benchPlayers.size());
    benchPlayer = benchPlayers.get(benchIndex);
    benchPlayers.remove(benchIndex);
    return benchPlayer;
  }

  public Coach getHeadCoach()
  {
    return this.headCoach;
  }

  public teamSide getTeamSide()
  {
    return this.side;
  }

  public int getTeamScore()
  {
    return this.teamScore;
  }

  public void madeBasket(int shotType)
  {
    this.teamScore += shotType;
  }

  private int randomNumberGenerator(int max)
  {
    return (int)(Math.random() * max);
  }

}
