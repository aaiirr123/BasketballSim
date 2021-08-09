package sample;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Timer;

import basketballSim.BasketballSim;
import basketballSim.Player;
import basketballSim.Roster;
import basketballSim.gameSim;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
public class viewTeamController {
    @FXML GridPane playerGrid;
    Roster team;
    BasketballSim game;
    ArrayList<Text> playerFatigues = new ArrayList<>();
    int row = 1;
    int teamside;

    public void update()
    {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent actionEvent) {
                                for (int i = 0; i < team.getNumPlayers(); i++)
                                {
                                    playerFatigues.get(i).setText(Integer.toString(team.getPlayer(i).getFatigue(game.getTime())));
                                }
                            }
                        }
                ),
                new KeyFrame(
                        Duration.seconds(2)
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    public void addPlayer(String position, String name, String height,
                          String threePtr, String midRange, String defense,
                          String rebound, String passing, String dunk, String fatigue)
    {
        Text fatigueText = new Text(fatigue);
        playerFatigues.add(fatigueText);
        playerGrid.addRow(row++, fatigueText, new Text(position), new Text(name), new Text(height),
                new Text(threePtr), new Text(midRange), new Text(defense), new Text(rebound),
                new Text(passing), new Text(dunk)
        );
    }
    public void addTeam(BasketballSim game, int teamside)
    {
        update();
        this.teamside = teamside;
        this.game = game;
        this.team = game.getRoster(this.teamside);
        int numPlayers = team.getNumPlayers();
        for (int i = 0; i < numPlayers; i++)
        {
            Player player = team.getPlayer(i);
            String name = player.getName();
            String position = player.getPosistion().toString();
            String height = Integer.toString(player.getHeight());
            String threePtr = Integer.toString(player.getShooting(gameSim.shotType.SET_THREE));
            String defense = Integer.toString(player.getDefense());
            String dunk = Integer.toString(player.getShooting(gameSim.shotType.DUNK));
            String midRange = Integer.toString(player.getShooting(gameSim.shotType.SET_MID_RANGE));
            String passing = Integer.toString(player.getPassingAbility());
            String rebounding = Integer.toString(player.getReboundAbility());
            String fatigue = Integer.toString(player.getFatigue(this.game.getTime()));

            addPlayer(position, name, height, threePtr, midRange, defense, rebounding, passing, dunk, fatigue);
        }
    }
}
