import java.util.*;

public class Roster
{
  public static enum teamSide {
    HOME,
    AWAY
  }

  private ArrayList<Player> team = new ArrayList<>();
  private teamSide side;
  private int numPlayers;
  private Coach headCoach = new Coach();
  private String teamName;

  Roster(teamSide side, int numPlayers, String teamName)
  {
    this.side = side;
    this.numPlayers = numPlayers;
    this.teamName = teamName;

    for (int i = 0; i < numPlayers; i++)
    {
      initiatePlayer(new Player("Nameless"));
    }

    initiateCoach(new Coach("Nameless"));

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

}
