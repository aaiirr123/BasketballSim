import java.lang.Math;

public class Player extends Person
{
  public static enum Position {
    PG, SG, SF, PF, C
  }
  private int playerIndex;
  private Position position;
  // Physical attributes
  private int height;
  private int weight;
  private int wingSpan;

  // Athletic attributes
  private int vertical;
  private int speed;
  private int strength;
  private int fatigue;

  // Skill attributes
  private int shooting;
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

  // Tendencies
  private int pullupMidRange = 30;
  private int pullupThree = 15;
  private int layup = 15;
  private int setMidRange = 10;
  private int setThree = 15;
  private int dunk = 15;


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


    this.shooting = super.genRandomAttribute(50);
    this.passing = super.genRandomAttribute(50);
    this.ballHandling = super.genRandomAttribute(50);
    this.defense = super.genRandomAttribute(50);
    this.rebound = super.genRandomAttribute(50);

    this.position = Position.PG;

  }

  Player(String name, Roster.teamSide team, int[] physical, int[] athletic, int[] skill, Position pos)
  {
    super(name, team);

    this.height = physical[0];
    this.weight = physical[1];
    this.wingSpan = physical[2];

    this.vertical = athletic[0];
    this.speed = athletic[1];
    this.strength = athletic[2];

    this.shooting = skill[0];
    this.passing = skill[1];
    this.ballHandling = skill[2];
    this.defense = skill[3];
    this.rebound = skill[4];

    this.position = pos;
  }

  public int getSpeed()
  {
    return this.speed;
  }

  public Position getPosistion()
  {
    return this.position;
  }

  public int getDefense()
  {
    return this.defense;
  }

  public int getHeight()
  {
    return this.height;
  }

  public int getFatigue()
  {
    return this.fatigue;
  }

  public int getShooting()
  {
    return this.shooting;
  }

  public void addBlock()
  {
    this.blocks++;
  }

  public void addRebound()
  {
    this.rebounds++;
  }

  public void addSteal()
  {
    this.steals++;
  }

  public void addFGA()
  {
    this.fga++;
  }

  public void addFGM()
  {
    this.fgm++;
  }

  public void addFTA()
  {
    this.fta++;
  }

  public void addFTM()
  {
    this.ftm++;
  }

  public void addThreeFGM()
  {
    this.threeFGM++;
  }

  public void addThreeFGA()
  {
    this.threeFGA++;
  }

  public void addFoul()
  {
    this.fouls++;
  }

  public void setPlayerIndex(int index)
  {
    this.playerIndex = index + 1;
  }

  public int getPlayerIndex()
  {
    return this.playerIndex;
  }

  public gameSim.shotType shotTendency()
  {
    int selectionNumber = (int) Math.ceil(100 * Math.random());

    int currentTotal = this.pullupMidRange;

    if (selectionNumber < currentTotal) return gameSim.shotType.PULLUP_MID_RANGE;

    currentTotal += this.pullupThree;

    if (selectionNumber < currentTotal) return gameSim.shotType.PULLUP_THREE;

    currentTotal += this.setMidRange;

    if (selectionNumber < currentTotal) return gameSim.shotType.SET_MID_RANGE;

    currentTotal += this.setThree;

    if (selectionNumber < currentTotal) return gameSim.shotType.SET_THREE;

    currentTotal += this.dunk;

    if (selectionNumber < currentTotal) return gameSim.shotType.DUNK;
    // base case is layup
    return gameSim.shotType.LAYUP;

  }

  public void stats()
  {
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

  public int getPoints()
  {
    return (this.threeFGM * 3) + ((this.fgm - this.threeFGM) * 2) + this.ftm;
  }


}
