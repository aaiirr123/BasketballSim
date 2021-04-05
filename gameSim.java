import java.util.concurrent.TimeUnit;

public class gameSim
{
  public static final int courtWidth = 49;
  public static final int courtLength = 92;
  public static final int quarterTime = 10 * 60;
  public static final double tick = 0.5;

  private double totalTime = quarterTime * 4;
  private boolean running = false;

  gameSim()
  {
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
    while (running)
    {
      for (int i = 1; i <= 4; i++)
      {
        while (totalTime > i * quarterTime)
        {
          pause(1000);

          totalTime -= tick * 10;
          System.out.println("Total Time: " + totalTime);
        }

        System.out.println("End of Quarter " + i);
      }
       running = false;
    }
  }


}
