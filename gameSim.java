import java.util.concurrent.TimeUnit;

public class gameSim
{
  public static final int quarterTime = 8 * 60;
  public static final double tick = 0.5;

  private double totalTime = 0;
  private boolean running = false;
  Roster homeTeam;
  Roster awayTeam;
  Roster offensiveTeam;
  Roster defensiveTeam;
  Roster.teamSide ballPosession;
  Player currentPlayer;


  gameSim(Roster home, Roster away)
  {
    this.homeTeam = home;
    this.awayTeam = away;
    running = true;
  }

  public static void pause(int ms) {
    try {
        Thread.sleep(ms);
    } catch (InterruptedException e) {
        System.err.format("IOException: %s%n", e);
    }
  }

  void startGame()
  {
    System.out.println("Printing Teams");
    System.out.println(homeTeam.getTeamName());
    System.out.println(homeTeam.getNumPlayers());
    for (int i = 0; i < homeTeam.getNumPlayers(); i++)
    {
      Player hold = homeTeam.getPlayer(i);
      System.out.print(hold.getName() + " ");
      System.out.println(hold.getHeight() + "   " + hold.getDefense());

    }
    System.out.println(awayTeam.getTeamName());
    for (int i = 0; i < awayTeam.getNumPlayers(); i++)
    {
      Player hold = awayTeam.getPlayer(i);
      System.out.print(hold.getName() + " ");
      System.out.println(hold.getHeight() + "   " + hold.getDefense());

    }
    while (running)
    {
      jumpBall();
      System.out.printf( ballPosession + " won jump ball");
      for (int i = 1; i <= 4; i++)
      {
        while (totalTime < i * quarterTime)
        {
          decideAction();
          pause(200);

          totalTime += tick * 40;
          System.out.println("Total Time: " + totalTime);
        }

        System.out.println("End of Quarter " + i);
      }
       running = false;
       System.out.println("homeTeam : " + homeTeam.getTeamScore());
       System.out.println("awayTeam : " + awayTeam.getTeamScore());

    }
  }

  private void jumpBall()
  {
    double homeScore;
    double awayScore;

    Player holder = homeTeam.getOnCourtPlayer(5);
    homeScore = holder.getHeight() * generateRandomVariance();

    holder = awayTeam.getOnCourtPlayer(5);
    awayScore = holder.getHeight() * generateRandomVariance();

    if (awayScore - homeScore > 0)
    {
      ballPosession = Roster.teamSide.AWAY;
      offensiveTeam = homeTeam;
      defensiveTeam = awayTeam;
      currentPlayer = awayTeam.getRandomOnCourt();
    }
    else
    {
      ballPosession = Roster.teamSide.HOME;
      offensiveTeam = awayTeam;
      defensiveTeam = homeTeam;
      currentPlayer = homeTeam.getRandomOnCourt();
    }

  }

  public static double generateRandomVariance()
  {
    return Math.random();
  }

  private void decideAction()
  {
    double rand = Math.random();

    if(rand < .3333)
    {
      System.out.println(ballPosession + ": " + currentPlayer.getName() + " is dribbilling the ball");
    }
    else if(rand > .3333 && rand < .6666)
    {
      shoot();
    }
    else
    {
      pass();
    }
  }

  private void shoot()
  {
    boolean made = false;

    String shotType = "Set";
    // Calculate shot quality based on shot type, coach skills, player discipline: out of 100
    double shotQuality = determineShotQuality(shotType);
    // Calculate defensive pressure
    double defensivePressure = determineDefensivePressure();

    if (currentPlayer.getShooting() + shotQuality > defensivePressure)
    {
      made = true;
    }

    if (!made)
    {
      rebound();
    }
    else
    {
      offensiveTeam.madeBasket(2);
      swapPossesion();
      currentPlayer = defensiveTeam.getRandomOnCourt();
    }
  }

  private void rebound()
  {
    double rand = Math.random();
    if ( rand < 0.5 )
    {
      ballPosession = Roster.teamSide.HOME;
      currentPlayer = homeTeam.getRandomOnCourt();
    }
    else
    {
      ballPosession = Roster.teamSide.AWAY;
      currentPlayer = awayTeam.getRandomOnCourt();
    }
    System.out.println(ballPosession + ":  " + currentPlayer.getName() + " grabs the rebound");
  }

  private void pass()
  {
    System.out.print(currentPlayer.getName() + " passes the ball to ");
    if (ballPosession == Roster.teamSide.HOME) currentPlayer = awayTeam.getRandomOnCourt();
    else currentPlayer = homeTeam.getRandomOnCourt();

    System.out.println(currentPlayer.getName());
  }

  private double determineShotQuality(String shotType)
  {
     double coachingBuf = (offensiveTeam.getheadCoach().getOffense());
     double shotTypeFactor = 1;
     if (shotType == "Set") shotTypeFactor = 1;
     if (shotType == "Moving") shotTypeFactor = 0.9;

     return shotTypeFactor * (coachingBuf + currentPlayer.getFatigue());
  }
  private double determineDefensivePressure()
  {
    double coachingBuf = defensiveTeam.getheadCoach().getDefense();
    Player defender = defensiveTeam.getOnCourtPlayer(currentPlayer.getPosistion());
    double individualDefense = defender.getHeight() + defender.getDefense();

    return coachingBuf + individualDefense;

  }

  private void swapPossesion()
  {
    Roster holder = offensiveTeam;
    offensiveTeam = defensiveTeam;
    defensiveTeam = holder;
  }
}
