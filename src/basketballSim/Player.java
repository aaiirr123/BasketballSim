package basketballSim;

import java.lang.Math;

public class Player extends Person
{
  public static enum Position {
    PG, SG, SF, PF, C
  }
  private Player passingTeamate;
  private boolean onCourt = false;
  // this will track the total minutes a player gets per game
  private double totalTime = 0;
  private double subInTime = 0;
  // this is to figure out how much stamina to restore to a player when they are subbed back in
  private double lastSubOutTime = 0;

  private int playerIndex;
  private Position position;
  private boolean assisted = false;
  // Physical attributes
  private int height;
  private int weight;
  private int wingSpan;

  // Athletic attributes
  private int vertical;
  private int speed;
  private int strength;
  private int fatigue;
  private int maxFatigue;

  // Skill attributes
  private int midRange;
  private int pullupMidRange;
  private int threePoint;
  private int pullupThreePoint;
  private int drivingLayup;
  private int dunkingAbility;

  private int freethrowShooting;
  private int passing;
  private int ballHandling;
  private int defense;
  private int rebound;

  // Player stats
  private int fga = 0;
  private int fgm = 0;
  private int threeFGM = 0;
  private int threeFGA = 0;
  private int rebounds = 0;
  private int steals = 0;
  private int blocks = 0;
  private int fta = 0;
  private int ftm = 0;
  private int assists = 0;
  private int fouls = 0;
  private int turnovers = 0;

  // Tendencies
  private int pullupMidRangeTendency = 30;
  private int pullupThreeTendency = 15;
  private int layupTendency = 15;
  private int setMidRangeTendency = 10;
  private int setThreeTendency = 15;
  private int dunkTendency = 15;


  Player(String name)
  {
    super(name);
    // Just a name so make random attributes
    this.height = 72 + (int)(12*(Math.random() - 0.5));
    this.weight = 72 + (int)(12*(Math.random() - 0.5));
    this.wingSpan = this.height +  (int)(2*(Math.random() - 0.5));

    this.vertical = super.genRandomAttribute(50);
    this.speed = super.genRandomAttribute(50);
    this.strength = super.genRandomAttribute(50);
    this.fatigue = super.genRandomAttribute(50);
    this.maxFatigue = this.fatigue;



    this.midRange = super.genRandomAttribute(50);
    this.pullupMidRange = super.genRandomAttribute(50);
    this.threePoint = super.genRandomAttribute(50);
    this.pullupThreePoint = super.genRandomAttribute(50);
    this.drivingLayup = super.genRandomAttribute(50);
    this.dunkingAbility = super.genRandomAttribute(50);
    this.freethrowShooting = super.genRandomAttribute(50);
    this.passing = super.genRandomAttribute(50);
    this.ballHandling = super.genRandomAttribute(50);
    this.defense = super.genRandomAttribute(50);
    this.rebound = super.genRandomAttribute(50);

    this.position = Position.PG;

  }

  Player(String name, int[] physical, int[] athletic, int[] skill, Position pos)
  {
    super(name);

    this.height = physical[0];
    this.weight = physical[1];
    this.wingSpan = physical[2];

    this.vertical = athletic[0];
    this.speed = athletic[1];
    this.strength = athletic[2];
    this.fatigue = athletic[3];
    this.maxFatigue = this.fatigue;

    this.midRange = skill[0];
    this.pullupMidRange = skill[1];
    this.threePoint = skill[2];
    this.pullupThreePoint = skill[3];
    this.drivingLayup = skill[4];
    this.dunkingAbility = skill[5];
    this.freethrowShooting = skill[6];
    this.passing = skill[7];
    this.ballHandling = skill[8];
    this.defense = skill[9];
    this.rebound = skill[10];

    this.position = pos;
  }

  // Setters and Getters

  public int getSpeed() { return this.speed; }

  public int getStrength() { return this.strength; }

  public int getVertical() { return this.vertical; }

  public Position getPosistion() { return this.position; }

  public int getDefense() {  return this.defense; }

  public int getHeight() { return this.height; }

  public int getWeight() { return this.weight; }

  public int getWingSpan(){ return this.wingSpan; }
  public int getMaxFatigue()
  {
    return this.maxFatigue;
  }
  public int getFatigue(double currentTime)
  {
    if (!onCourt)
    {
      int tempFatigue = this.fatigue + (int)(currentTime - this.lastSubOutTime);
      return Math.min(tempFatigue, maxFatigue);
    }
    else return this.fatigue;
  }

  public void addBlock() { this.blocks++; }

  public void addRebound() { this.rebounds++; }

  public void addSteal(){ this.steals++; }

  public void addFGA() { this.fga++; }

  public void addFGM() { this.fgm++; }

  public void addFTA() { this.fta++; }

  public void addFTM() { this.ftm++; }

  public void addThreeFGM() { this.threeFGM++; }

  public void addThreeFGA() { this.threeFGA++; }

  public void addAssist() { this.assists++; }

  public void addFoul() { this.fouls++; }

  public void setPlayerIndex(int index) { this.playerIndex = index; }

  public int getPlayerIndex() { return this.playerIndex; }

  public void assistBonus(boolean assist) { this.assisted = assist; }

  public void restPlayerFully() { this.fatigue = 100; }

  public void addFatigue(int amount) { this.fatigue -= amount; }

  public int getFreeThrowAbility() { return this.freethrowShooting; }

  public boolean isAssisted()
  {
    if (assisted) return true;
    return false;
  }

  public void addPassingTeamate(Player passer){ this.passingTeamate = passer; }

  public void creditAssist() { passingTeamate.addAssist(); }

  public int getShooting(gameSim.shotType shot)
  {
    if (shot == gameSim.shotType.PULLUP_MID_RANGE) return this.pullupMidRange;
    if (shot == gameSim.shotType.SET_MID_RANGE) return this.midRange;
    if (shot == gameSim.shotType.PULLUP_THREE) return this.pullupThreePoint;
    if (shot == gameSim.shotType.SET_THREE) return this.threePoint;
    if (shot == gameSim.shotType.DUNK) return this.dunkingAbility;
    if (shot == gameSim.shotType.FREE_THROW) return this.freethrowShooting;
    else return this.drivingLayup;
  }

  // Tendencies and decision making

  // currently has nothing to do with tendencies, definitly need to come up with a better
  // scenario. Can most likely use the same base for the decision scenario as this.
  public gameSim.shotType shotTendency()
  {
    int selectionNumber = (int) Math.ceil(100 * Math.random());

    int currentTotal = this.pullupMidRangeTendency;

    if (selectionNumber < currentTotal) return gameSim.shotType.PULLUP_MID_RANGE;

    currentTotal += this.pullupThreeTendency;

    if (selectionNumber < currentTotal) return gameSim.shotType.PULLUP_THREE;

    currentTotal += this.setMidRangeTendency;

    if (selectionNumber < currentTotal) return gameSim.shotType.SET_MID_RANGE;

    currentTotal += this.setThreeTendency;

    if (selectionNumber < currentTotal) return gameSim.shotType.SET_THREE;

    currentTotal += this.dunkTendency;

    if (selectionNumber < currentTotal) return gameSim.shotType.DUNK;
    // base case is layup
    return gameSim.shotType.LAYUP;

  }

  public void playerSubOut(double currentTime)
  {
    this.totalTime += (currentTime - this.subInTime);
    this.lastSubOutTime = currentTime;
    this.onCourt = false;
  }

  public void playerSubIn(double currentTime)
  {
    this.subInTime = currentTime;
    this.fatigue = getFatigue(currentTime);
    this.onCourt = true;
  }

  public void stats()
  {
    System.out.print("Points: " + getMinutes() + " |");
    System.out.print("Points: " + getPoints() + " |");
    System.out.print("fga " + this.fga + " |");
    System.out.print("fgm " + this.fgm + " |");
    System.out.print("3ptA " + this.threeFGA + " |");
    System.out.print("3ptM " + this.threeFGM + " |");
    System.out.print("rebounds " + this.rebounds + " |");
    System.out.print("steals " + this.steals + " |");
    System.out.print("blocks " + this.blocks + " |");
    System.out.print("fta " + this.fta + " |");
    System.out.print("ftm " + this.ftm + " |");
    System.out.print("assists " + this.assists + " |");
  }
  public int getPassingAbility()
  {
    return this.passing;
  }

  public int getBallHandling()
  {
    return this.ballHandling;
  }

  public int getReboundAbility()
  {
    return this.rebound;
  }

  public double getMinutes()
  {
    return totalTime / 60;
  }

  public int getPoints()
  {
    return (this.threeFGM * 3) + ((this.fgm - this.threeFGM) * 2) + this.ftm;
  }

  public int getStatRebounds() { return this.rebounds; }

  public int getStatDRebounds() { return this.rebounds; }

  public int getStatORebounds() { return this.rebounds; }

  public int getStatAssists() { return this.assists; }

  public int getStatFGA() { return this.fga; }

  public int getStatFGM() { return this.fgm; }

  public int getStatThreeFGA() { return this.threeFGA; }

  public int getStatThreeFGM() { return this.threeFGM; }

  public int getStatFTA() { return this.fta; }

  public int getStatFTM() { return this.ftm; }

  public int getStatBlocks() { return this.blocks; }

  public int getStatSteals() { return this.steals; }

  public int getStatTurnovers() {return this.turnovers; }









}
