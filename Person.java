import java.io.*;
import java.util.*;

public class Person
{
  protected boolean ejected = false;
  protected String name;
  protected Roster.teamSide team;
  static int namePlace = 1;
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
  public static String randomName() throws FileNotFoundException
  {
    File file = new File("names.txt");
    Scanner sc = new Scanner(file);

    for (int i = 0; i < namePlace; i++)
    {
      sc.nextLine();
    }
    namePlace++;
    return sc.nextLine();
  }

}
