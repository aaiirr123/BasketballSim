
public class BasketballSim
{
  private Roster home = new Roster(Roster.teamSide.HOME, 10, "Dragons");
  private Roster away = new Roster(Roster.teamSide.AWAY, 10, "Hawks");

  BasketballSim()
  {
    System.out.println("Starting Sim");
    System.out.println(home.getPlayer(1).getName());
    gameSim game = new gameSim();
    game.startGame();
  }

  public static void main(String [] args)
  {
    BasketballSim Aarons = new BasketballSim();
  }
}
