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

  Roster(teamSide side, int numPlayers, String teamName) throws FileNotFoundException
  {
    this.side = side;
    this.numPlayers = numPlayers;
    this.teamName = teamName;

    for (int i = 0; i < numPlayers; i++)
    {
      initiatePlayer(new Player(Person.randomName()));
    }

    initiateCoach(new Coach(Person.randomName()));

    initiateStarters();
  }

  private void initiateStarters()
  {
    for (int i = 0; i < 5; i++)
    {
      currentFive[i] = team.get(i);
    }
  }

  public Player getOnCourtPlayer(int position)
  {
    return currentFive[position - 1];
  }

  public void subPlayer(int position, int player)
  {
    currentFive[position - 1] = team.get(player - 1);
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
    int rand = (int)(5 * Math.random()) + 1;
    return getOnCourtPlayer(rand);
  }

}
