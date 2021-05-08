import java.util.concurrent.TimeUnit;
public class gameSim
{
  public static final int gameSpeedFactor = 20;
  public static final int quarterTime = 8 * 60;
  public static final double tick = 0.5;

  private double totalTime = 0;
  private boolean running = false;

  Roster homeTeam;
  Roster awayTeam;
  // To keep track by reference
  Roster offensiveTeam;
  Roster defensiveTeam;
  // This is the on court player with possesion of the ball
  Player currentPlayer;

  public static enum shotType {
    LAYUP,
    DUNK,
    SET_THREE,
    SET_MID_RANGE,
    PULLUP_THREE,
    PULLUP_MID_RANGE,
  }


  gameSim(Roster home, Roster away)
  {
    this.homeTeam = home;
    this.awayTeam = away;
    running = true;
  }

  void startGame()
  {
    while (running)
    {
      jumpBall();
      System.out.printf( offensiveTeam.getTeamName() + " won jump ball");
      for (int i = 1; i <= 4; i++)
      {
        while (totalTime < i * quarterTime)
        {
          decideAction();
          pause(1500 / gameSpeedFactor);

          totalTime += tick * 15;
          System.out.println("Total Time: " + totalTime);
        }

        System.out.println("End of Quarter " + i);
      }
       running = false;
    }
  }

  private void jumpBall()
  {
    double homeScore;
    double awayScore;

    Player holder = homeTeam.getOnCourtPlayer(5);
    homeScore = holder.getHeight() * generateRandomVariance(.75,1);

    holder = awayTeam.getOnCourtPlayer(5);
    awayScore = holder.getHeight() * generateRandomVariance(.75,1);

    if (awayScore > homeScore)
    {
      offensiveTeam = awayTeam;
      defensiveTeam = homeTeam;
    }
    else
    {
      offensiveTeam = homeTeam;
      defensiveTeam = awayTeam;
    }
    currentPlayer = offensiveTeam.getRandomOnCourt();
  }

  public static double generateRandomVariance(double min, double max)
  {
    return (Math.random() * (max - min)) + min;
  }

  private void decideAction()
  {
    double rand = Math.random();

    if (rand < .3333)
    {
      System.out.println(currentPlayer.getName() + " is dribbilling the ball");
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

    shotType shot = currentPlayer.shotTendency();
    // Calculate shot quality based on shot type, coach skills, player discipline: out of 100
    double shotQuality = determineShotQuality(shot);
    // Calculate defensive pressure
    double defensivePressure = determineDefensivePressure();

    System.out.println("plsyer shooting ability: " + currentPlayer.getShooting() + ", shot quality: " + shotQuality);
    System.out.println("Defensive ability: " + defensivePressure);
    if ( isFoul(shot, shotQuality, defensivePressure) )
    {
      System.out.println("shooting foul");
      Player defender = defensiveTeam.getOnCourtPlayer(currentPlayer.getPlayerIndex());
      defender.addFoul();
      currentPlayer.addFTA();
      currentPlayer.addFTM();
      System.out.println(currentPlayer.getName() + " mad a free throw");
      offensiveTeam.madeBasket(1);

      currentPlayer.addFTA();
      currentPlayer.addFTM();
      System.out.println(currentPlayer.getName() + " mad a free throw");
      offensiveTeam.madeBasket(1);
      swapPossesion();
      return;

    }
    if ((currentPlayer.getShooting() * shotQuality) * generateRandomVariance(0.66,1) > defensivePressure * generateRandomVariance(0.66,1))
    {
      made = true;
    }

    if (defensivePressure > shotQuality)
    {
      if (blockShot())
      {
        made = false;
      }
    }

    if (!made)
    {
      System.out.println(currentPlayer.getName() + " misses");
      currentPlayer.addFGA();
      rebound();
    }
    else
    {
      System.out.println(currentPlayer.getName() + " makes his shot");
      currentPlayer.addFGA();
      currentPlayer.addFGM();
      if (shot == shotType.SET_THREE || shot == shotType.PULLUP_THREE)
      {
        offensiveTeam.madeBasket(3);
        currentPlayer.addThreeFGA();
        currentPlayer.addThreeFGM();
      }
      else offensiveTeam.madeBasket(2);
      currentPlayer = defensiveTeam.getRandomOnCourt();
      swapPossesion();
    }
  }

  private void rebound()
  {
    double rand = Math.random();
    if ( rand < 0.5 )
    {
      System.out.println("Offesnive rebound\n");
      currentPlayer = offensiveTeam.getRandomOnCourt();
    }
    else
    {
      System.out.println("Defensive rebound\n");
      currentPlayer = defensiveTeam.getRandomOnCourt();
      swapPossesion();
    }
    currentPlayer.addRebound();
    System.out.println(currentPlayer.getName() + " grabs the rebound");
  }

  private void pass()
  {
    System.out.print(currentPlayer.getName() + " passes the ball to ");
    int score = 0;
    int bestScore = -100;

    Player passTarget;
    Player defender;

    Player bestTarget = currentPlayer;
    Player bestDefender = defensiveTeam.getOnCourtPlayer(currentPlayer.getPlayerIndex());
    for (int i = 0; i < 5; i++)
    {

      if (i != currentPlayer.getPlayerIndex())
      {
        passTarget = offensiveTeam.getOnCourtPlayer(i + 1);
        defender = defensiveTeam.getOnCourtPlayer(i + 1);
        score = (int)(generateRandomVariance(0.6,1) * passTarget.getSpeed()) - (int)(generateRandomVariance(0.6,1) * defender.getSpeed());
        if (score > bestScore)
        {
          bestScore = score;
          passTarget = bestTarget;
          defender = bestDefender;
        }
      }
    }
      // Steal
    if (bestScore < 0)
    {
      System.out.println("\n\n" + bestDefender.getName() + " stole the ball from " + currentPlayer.getName());
      currentPlayer = bestDefender;
      currentPlayer.addSteal();
      swapPossesion();
    }
    else
    {
      currentPlayer = bestTarget;
    }


    System.out.println(currentPlayer.getName());
  }

  private double determineShotQuality(shotType shot)
  {
     double coachingBuf = (offensiveTeam.getheadCoach().getOffense());
     double shotTypeFactor = 1;
     if (shot == shotType.SET_MID_RANGE) shotTypeFactor = 0.9;
     if (shot == shotType.SET_THREE) shotTypeFactor = 0.8;
     if (shot == shotType.PULLUP_THREE) shotTypeFactor = 0.7;
     if (shot == shotType.PULLUP_MID_RANGE) shotTypeFactor = 0.75;
     if (shot == shotType.LAYUP) shotTypeFactor = 1;
     if (shot == shotType.DUNK) shotTypeFactor = 1;

     return shotTypeFactor * (coachingBuf + currentPlayer.getFatigue());
  }

  private double determineDefensivePressure()
  {
    double coachingBuf = defensiveTeam.getheadCoach().getDefense();
    Player defender = defensiveTeam.getOnCourtPlayer(currentPlayer.getPlayerIndex());
    double individualDefense = defender.getHeight() + defender.getDefense();

    return coachingBuf + individualDefense;
  }

  private boolean blockShot()
  {
    Player defender = defensiveTeam.getOnCourtPlayer(currentPlayer.getPlayerIndex());
    double shotBlock = generateRandomVariance(0.66, 1) * defender.getHeight() + defender.getDefense();
    double offenseAbility = generateRandomVariance(0.66, 2) * currentPlayer.getHeight() + currentPlayer.getShooting();

    if (shotBlock > offenseAbility)
    {
      System.out.println("Shot was blocked by " + defender.getName() + " !");
      defender.addBlock();
      return true;
    }
    return false;
  }

  private boolean isFoul(shotType shot, double shotQuality, double defensivePressure)
  {
    float modifier = 0;
    if (shot == shotType.LAYUP) modifier = 1;
    else if (shot == shotType.SET_THREE) modifier = 1;
    else if (shot == shotType.PULLUP_THREE) modifier = 1;
    else if (shot == shotType.PULLUP_MID_RANGE) modifier = 1;
    else if (shot == shotType.SET_MID_RANGE) modifier = 1;
    else if (shot == shotType.DUNK) modifier = 1;
    else System.out.println("Error shot type not added");

    System.out.println("in is foul");

    if (generateRandomVariance(0,.75) > .5)
    {
      // the higher defnsive pressure and the lower the coaches defensive IQ, the more likely the foul
        System.out.println("in is foul");
        double rand = generateRandomVariance(0,1);
        System.out.println("rand " + rand);

        if (rand > 0.5) return true;
    }
    return false;
  }

  private void swapPossesion()
  {
    Roster holder = offensiveTeam;
    offensiveTeam = defensiveTeam;
    defensiveTeam = holder;
  }

  public static void pause(int ms)
  {
    try {
        Thread.sleep(ms);
    } catch (InterruptedException e) {
        System.err.format("IOException: %s%n", e);
    }
  }
}
