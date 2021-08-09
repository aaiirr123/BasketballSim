package sample;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import basketballSim.BasketballSim;
import basketballSim.Roster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;

public class GameSimController implements Initializable {
    @FXML
    private Text quarterNumber;
    @FXML private Text currentTime;
    @FXML private Text scoreTeam1;
    @FXML private Text scoreTeam2;
    @FXML private Text gameMessage;
    @FXML private TextArea logText;
    @FXML private Button statsView;
    @FXML private Button pauseButton;
    @FXML private Button homeTeamStats;
    @FXML private Button awayTeamStats;
    @FXML private Button homeTeamRoster;
    @FXML private Button awayTeamRoster;


    private Parent root;
    private Scene scene;
    private Stage stage;

    private int currentQuarter = 1;
    private double quarterTime = 8 * 60;
    private BasketballSim game;
    boolean suspended = false;
    private Roster userTeam;
    private Roster cpuTeam;

    // this is a log for what happened in the sim
    private ArrayList<String> eventLog = new ArrayList<>();
    GameThread basketballSim;

    public void startGameSim(ActionEvent event) throws FileNotFoundException
    {
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
        Button btn = (Button) event.getSource();
        int teamSide = 0;
        if(btn.getId().toString().equals("homeTeamStats")) teamSide = 1;
        else teamSide = 2;

        Stage teamStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/finalStats.fxml"));
        Parent root = loader.load();
        FinalStats finalStats = loader.getController();
        finalStats.start(game,teamSide);
        Scene statsScene = new Scene(root);
        teamStage.setScene(statsScene);
        teamStage.show();
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

    public void viewTeam(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        int teamSide = 0;
        if(btn.getId().toString().equals("homeTeamRoster")) teamSide = 1;
        else teamSide = 2;

        Stage teamStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/viewTeam.fxml"));
        Parent root = loader.load();
        viewTeamController controller1 = loader.getController();
        controller1.addTeam(game,teamSide);
        Scene viewTeamScene = new Scene(root);
        teamStage.setScene(viewTeamScene);
        teamStage.show();
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
    public void initTeams(Roster team1, Roster team2) throws FileNotFoundException {
        this.userTeam = team1;
        this.cpuTeam = team2;
        this.game = new BasketballSim(this, team1, team2);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
