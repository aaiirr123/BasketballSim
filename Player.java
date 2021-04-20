import java.lang.Math;

public class Player extends Person
{
  public static enum Position {
    PG, SG, SF, PF, C
  }
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

  Player(String name)
  {
    super(name);
    // Just a name so make random attributes
    this.height = 72 + (int)(12*(Math.random() - 0.5));
    this.weight = 72 + (int)(12*(Math.random() - 0.5));
    this.wingSpan = this.height +  (int)(2*(Math.random() - 0.5));

    this.vertical = super.genRandomAttribute(25);
    this.speed = super.genRandomAttribute(25);
    this.strength = super.genRandomAttribute(25);
    this.fatigue = super.genRandomAttribute(25);


    this.shooting = super.genRandomAttribute(25);
    this.passing = super.genRandomAttribute(25);
    this.ballHandling = super.genRandomAttribute(25);
    this.defense = super.genRandomAttribute(25);
    this.rebound = super.genRandomAttribute(25);

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


}
