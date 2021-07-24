package basketballSim;

public class Coach extends Person
{
  // Skills
  private int offense;
  private int defense;
  private int motivation;

  Coach()
  {
    super();
  }

  Coach(String name)
  {
    super(name);
  }

  Coach(String name, int offense, int defense, int motivation)
  {
      super(name);
      this.offense = offense;
      this.defense = defense;
      this.motivation = motivation;
  }

  public int getMotivation()
  {
    return this.motivation;
  }

  public int getOffense()
  {
    return this.offense;
  }

  public int getDefense()
  {
    return this.defense;
  }
}
