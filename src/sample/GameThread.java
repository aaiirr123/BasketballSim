package sample;

import basketballSim.BasketballSim;

class GameThread extends Thread {

    public BasketballSim sim;
    GameThread(BasketballSim sim)
    {
        this.sim = sim;
    }
    public void run()
    {
        try {
            System.out.println("Running game thread");
            sim.start();
        }
        catch (Exception e) {
            System.out.println("Exception is caught");
        }
    }
}
