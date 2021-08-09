package basketballSim;

import sample.GameSimController;

import java.util.concurrent.TimeUnit;
import java.io.*;

public class gameSim
{
  private GameSimController controller;
  public static final int gameSpeedFactor = 2;
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
    FREE_THROW
  }


  gameSim(Roster home, Roster away, GameSimController controller)
  {
    this.homeTeam = home;
    this.awayTeam = away;
    this.controller = controller;
    running = true;
  }

  void startGame()
  {
    while (running)
    {
      jumpBall();
      System.out.printf( offensiveTeam.getTeamName() + " won jump ball");
      controller.broadcastMessage(offensiveTeam.getTeamName() + " won jump ball");
      for (int i = 1; i <= 4; i++)
      {
        while (totalTime < i * quarterTime)
        {
          decideAction();
          pause(1500 / gameSpeedFactor);

          totalTime += tick * 15;
          controller.updateTime(tick * 15);
          System.out.println("Total Time: " + totalTime);
        }

        System.out.println("End of Quarter " + i);
        controller.updateQuarter();
        controller.broadcastMessage("End of Quarter " + i);

        pause(4000 / gameSpeedFactor);
      }
        for ( int i = 0; i < 5; i++)
        {
            offensiveTeam.getOnCourtPlayer(i).playerSubOut(totalTime);
            defensiveTeam.getOnCourtPlayer(i).playerSubOut(totalTime);
        }

        controller.gameEnded();



      running = false;
    }
  }

  private void jumpBall()
  {
    double homeScore;
    double awayScore;

    // current jump ball logic
    //
    Player holder = homeTeam.getOnCourtPlayer(Player.Position.C);
    homeScore = holder.getHeight() * generateRandomVariance(.75,1);

    holder = awayTeam.getOnCourtPlayer(Player.Position.C);
    awayScore = holder.getHeight() * generateRandomVariance(.75,1);
    //
    // current jump ball logic

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
    Player excludeCenter = offensiveTeam.getOnCourtPlayer(Player.Position.C);
    currentPlayer = offensiveTeam.getRandomOnCourt(excludeCenter);
  }
  //
  //
  // This whole section needs major upgrades in terms of decision making ability, logic is currently 1/3 chance
  // for each option. Will most likely be changed by adding some attributes regarding player Tendencies.
  //
  //
  //

  private void decideAction()
  {
    double rand = Math.random();

    if (rand < .3333)
    {
      currentPlayer.assistBonus(false);
      currentPlayer.addFatigue(2);
      System.out.println(currentPlayer.getName() + " is dribbling the ball");
      controller.broadcastMessage(currentPlayer.getName() + " is dribbling the ball");
    }
    else if(rand > .3333 && rand < .6666)
    {
      shoot();
      currentPlayer.assistBonus(false);
    }
    else
    {
      currentPlayer.assistBonus(false);
      pass();
    }
  }
  //
  //
  // Shooting system is completly broken now, no real logic. Have added in some form of players pciking
  // picking different shots
  // must fix issue where the player can still be blocked after getting fouled
  //
  //


  private void shoot()
  {
    boolean made = false;
    currentPlayer.addFatigue(4);
    shotType shot = currentPlayer.shotTendency();
    // Calculate shot quality based on shot type, coach skills, player discipline: out of 100
    double shotQuality = determineShotQuality(shot);
    // Calculate defensive pressure
    double defensivePressure = determineDefensivePressure();

    if (isFoul(shot, shotQuality, defensivePressure))
    {
      currentPlayer.assistBonus(false);
      System.out.println("shooting foul");
      controller.broadcastMessage("Shooting Foul");
      Player defender = defensiveTeam.getOnCourtPlayer(currentPlayer.getPlayerIndex());
      defender.addFoul();

      shootFreeThrow();
      stopInPlay();
      // We need to rebound on the second freethrow
      if(!shootFreeThrow())
      {
        rebound();
        return;
      }
      swapPossesion();
      currentPlayer = offensiveTeam.getRandomOnCourt();
      return;
    }
    // The logic behind whether or not a plater makes a shot
    if ((currentPlayer.getShooting(shot) * shotQuality) * generateRandomVariance(0.66,1) > defensivePressure * generateRandomVariance(0.66,1))
    {
      made = true;
    }
    // This goes to the blocked function and can reverse a make
    if (defensivePressure > shotQuality)
    {
      if (blockShot(shot)) made = false;
    }

    if (!made)
    {
      currentPlayer.assistBonus(false);
      System.out.println(currentPlayer.getName() + " misses");
      controller.broadcastMessage(currentPlayer.getName() + " misses");
      currentPlayer.addFGA();
      rebound();
    }
    else
    {
      System.out.println(currentPlayer.getName() + " makes his shot");
      controller.broadcastMessage(currentPlayer.getName() + " makes his shot");

      currentPlayer.addFGA();
      currentPlayer.addFGM();

      if (currentPlayer.isAssisted()) currentPlayer.creditAssist();

      if (shot == shotType.SET_THREE || shot == shotType.PULLUP_THREE)
      {
        offensiveTeam.madeBasket(3);
        currentPlayer.addThreeFGA();
        currentPlayer.addThreeFGM();
        if (offensiveTeam.getTeamSide() == Roster.teamSide.HOME) controller.addPoints(1,offensiveTeam.getTeamScore());
        else controller.addPoints(2,offensiveTeam.getTeamScore());
      }
      else
      {
        offensiveTeam.madeBasket(2);
        if (offensiveTeam.getTeamSide() == Roster.teamSide.HOME) controller.addPoints(1,offensiveTeam.getTeamScore());
        else controller.addPoints(2,offensiveTeam.getTeamScore());
      }

      currentPlayer.assistBonus(false);
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
      controller.broadcastMessage("Offensive Rebound");

      currentPlayer = offensiveTeam.getRandomOnCourt();
    }
    else
    {
      System.out.println("Defensive rebound\n");
      controller.broadcastMessage("Defensive Rebound");

      currentPlayer = defensiveTeam.getRandomOnCourt();
      swapPossesion();
    }
    currentPlayer.addRebound();
    System.out.println(currentPlayer.getName() + " grabs the rebound");
    controller.broadcastMessage(currentPlayer.getName() + " grabs the rebound");

  }

  private void pass()
  {
    System.out.print(currentPlayer.getName() + " passes the ball to ");
    controller.broadcastMessage(currentPlayer.getName() + " passes the ball to ");

    int score = 0;
    int bestScore = -100;

    Player passTarget;
    Player defender;

    Player bestTarget = offensiveTeam.getRandomOnCourt(currentPlayer.getPlayerIndex());
    Player bestDefender = defensiveTeam.getOnCourtPlayer(currentPlayer.getPlayerIndex());
    // loop through
    for (int i = 0; i < 5; i++)
    {
      if (i != currentPlayer.getPlayerIndex())
      {
        passTarget = offensiveTeam.getOnCourtPlayer(i);
        defender = defensiveTeam.getOnCourtPlayer(i);
        score = (int)(generateRandomVariance(0.6,1) * passTarget.getSpeed()) - (int)(generateRandomVariance(0.6,1) * defender.getSpeed());
        if (score > bestScore)
        {
          bestScore = score;
          bestTarget = passTarget;
          defender = bestDefender;
        }
      }
    }
      // Steal
    if (bestScore < 0)
    {
      System.out.println("\n\n" + bestDefender.getName() + " stole the ball from " + currentPlayer.getName());
      controller.broadcastMessage(bestDefender.getName() + " stole the ball from " + currentPlayer.getName());

      currentPlayer = bestDefender;
      currentPlayer.addSteal();
      currentPlayer.addFatigue(4);
      swapPossesion();
    }
    else
    {
      bestTarget.addPassingTeamate(currentPlayer);
      currentPlayer = bestTarget;
      currentPlayer.assistBonus(true);
    }


    System.out.println(currentPlayer.getName());
  }

  private double determineShotQuality(shotType shot)
  {
     double coachingBuf = (offensiveTeam.getHeadCoach().getOffense());
     double shotTypeFactor = 1;
     if (shot == shotType.SET_MID_RANGE) shotTypeFactor = 0.9;
     if (shot == shotType.SET_THREE) shotTypeFactor = 0.8;
     if (shot == shotType.PULLUP_THREE) shotTypeFactor = 0.7;
     if (shot == shotType.PULLUP_MID_RANGE) shotTypeFactor = 0.75;
     if (shot == shotType.LAYUP) shotTypeFactor = 1;
     if (shot == shotType.DUNK) shotTypeFactor = 1;

     return shotTypeFactor * (coachingBuf + currentPlayer.getFatigue(totalTime));
  }

  private double determineDefensivePressure()
  {
    double coachingBuf = defensiveTeam.getHeadCoach().getDefense();
    Player defender = defensiveTeam.getOnCourtPlayer(currentPlayer.getPlayerIndex());
    double individualDefense = defender.getHeight() + defender.getDefense();

    return coachingBuf + individualDefense;
  }

  private boolean blockShot(shotType shot)
  {
    Player defender = defensiveTeam.getOnCourtPlayer(currentPlayer.getPlayerIndex());
    double shotBlock = generateRandomVariance(0.66, 1) * defender.getHeight() + defender.getDefense();
    double offenseAbility = generateRandomVariance(0.66, 2) * currentPlayer.getHeight() + currentPlayer.getShooting(shot);

    if (shotBlock > offenseAbility)
    {
      System.out.println("Shot was blocked by " + defender.getName() + " !");
      controller.broadcastMessage("Shot was blocked by " + defender.getName() + " !");

      defender.addBlock();
      defender.addFatigue(4);
      return true;
    }
    return false;
  }

  private boolean shootFreeThrow()
  {
    currentPlayer.addFTA();
    if (currentPlayer.getFreeThrowAbility() * generateRandomVariance(0.6,1) > 50)
    {
      currentPlayer.addFTM();
      System.out.println(currentPlayer.getName() + " made a free throw");
      controller.broadcastMessage(currentPlayer.getName() + " made a free throw");


      offensiveTeam.madeBasket(1);
      return true;
    }

    System.out.println(currentPlayer.getName() +  " missed freethrow");
    controller.broadcastMessage(currentPlayer.getName() +  " missed freethrow");

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

  private void stopInPlay()
  {
    // check to see if there are any substitutions needed
    for ( int i = 0; i < 5; i++)
    {
      if (offensiveTeam.getOnCourtPlayer(i).getFatigue(totalTime) < 50)
      {
        Player subOut = offensiveTeam.getOnCourtPlayer(i);
        subOut.playerSubOut(totalTime);
        Player subIn = offensiveTeam.getBenchPlayer();
        // need to change
        subIn.playerSubIn(totalTime);
        //
        offensiveTeam.subPlayers(subIn,subOut, i);
      }

      if (defensiveTeam.getOnCourtPlayer(i).getFatigue(totalTime) < 50)
      {
        Player subOut = defensiveTeam.getOnCourtPlayer(i);
        subOut.playerSubOut(totalTime);

        Player subIn = defensiveTeam.getBenchPlayer();
        // need to change
        subIn.playerSubIn(totalTime);

        //
        defensiveTeam.subPlayers(subIn,subOut, i);
      }
    }
  }

  private void swapPossesion()
  {
    Roster holder = offensiveTeam;
    offensiveTeam = defensiveTeam;
    defensiveTeam = holder;
  }

  public static double generateRandomVariance(double min, double max)
  {
    return (Math.random() * (max - min)) + min;
  }

  public static void pause(int ms)
  {
    try {
        Thread.sleep(ms);
    } catch (InterruptedException e) {
        System.err.format("IOException: %s%n", e);
    }
  }

  public double getTotalTime() {
    return totalTime;
  }

  public void pauseGame()
  {
    while(true)
    {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        System.err.format("IOException: %s%n", e);
      }
    }

  }
}
