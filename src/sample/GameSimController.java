package sample;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import basketballSim.BasketballSim;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GameSimController {
    @FXML
    private Text quarterNumber;
    @FXML private Text currentTime;
    @FXML private Text scoreTeam1;
    @FXML private Text scoreTeam2;
    @FXML private Text gameMessage;
    @FXML private TextArea logText;
    @FXML private Button statsView;
    @FXML private Button pauseButton;

    private Parent root;
    private Scene scene;
    private Stage stage;

    private int currentQuarter = 1;
    private double quarterTime = 8 * 60;
    private BasketballSim game;
    boolean suspended = false;

    // this is a log for what happened in the sim
    private ArrayList<String> eventLog = new ArrayList<>();
    GameThread basketballSim;

    public void startGameSim(ActionEvent event) throws FileNotFoundException
    {
        game = new BasketballSim(this);
        basketballSim = new GameThread(game);
        basketballSim.start();
    }

    private void loadScreen(Parent root, ActionEvent event)
    {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void updateTime(double tick)
    {
        quarterTime -= tick;
        if (quarterTime <= 0 )
        {
            currentTime.setText("00:00");
        }
        if (quarterTime > 0) currentTime.setText(convertToMinSec((int)quarterTime));

    }
    public void updateQuarter()
    {
        if (currentQuarter < 4) currentQuarter++;
        quarterTime = 8 * 60;
        quarterNumber.setText(Integer.toString(currentQuarter));
    }

    public static String convertToMinSec(int quarterTime)
    {
        int minutes = quarterTime / 60;
        int seconds = quarterTime % 60;
        if (seconds < 9) return "0" + minutes + ":" + "0" + seconds;
        return "0" + minutes + ":" + seconds;
    }

    public void gameEnded()
    {
        statsView.setVisible(true);
        statsView.setDisable(false);
    }
    public void viewStats(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/finalStats.fxml"));
        Parent root = loader.load();
        FinalStats finalStats = loader.getController();
        loadScreen(root, event);
        finalStats.start();
    }

    public void broadcastMessage(String message)
    {
        gameMessage.setText(message);
        eventLog.add(message);
    }

    public void addPoints(int team, int score)
    {
        if(team == 1) scoreTeam1.setText(Integer.toString(score));
        else scoreTeam2.setText(Integer.toString(score));
    }

    public void exitGame(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/mainMenu.fxml"));
        Parent root = loader.load();
        loadScreen(root, event);
    }

    public void onPause(ActionEvent event)
    {
        if ((!suspended)) {
            basketballSim.suspend();
        } else {
            basketballSim.resume();
        }
        suspended = !suspended;
    }
}
