public class Person
{
  protected boolean ejected = false;
  protected String name;
  protected Roster.teamSide team;

  Person()
  {
    this.name = "Nameless";
  }

  Person (String name)
  {
    this.name = name;
  }

  Person (String name, Roster.teamSide team)
  {
    this.name = name;
    this.team = team;
  }

  public String getName()
  {
    return this.name;
  }

  public Roster.teamSide getTeam()
  {
    return this.team;
  }

  public void eject()
  {
    this.ejected = true;
  }

}
