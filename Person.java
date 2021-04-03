public abstract class Person
{
  private boolean ejected = false;
  private String name;
  private String team;

  Person (String name)
  {
    self.name = name;
  }

  public getName()
  {
    return self.name;
  }

  public getTeam()
  {
    return self.team;
  }

  public eject()
  {
    ejected = true;
  }

}
