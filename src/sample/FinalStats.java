package sample;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import basketballSim.BasketballSim;
import basketballSim.Player;
import basketballSim.Roster;
import basketballSim.gameSim;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;

public class FinalStats {
    @FXML GridPane statsGrid;
    BasketballSim basketballSim;
    Roster team;
    int teamSide;
    int row = 1;
    ArrayList<GridBoxPlayer> gridBoxPlayers = new ArrayList<>();

    public void start(BasketballSim basketballSim, int team)
    {
        this.teamSide = team;
        this.basketballSim = basketballSim;
        currentStats();
    }

    public void update() {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                System.out.println("Update");
                                for(int i = 0; i < gridBoxPlayers.size();i++)
                                {
                                    gridBoxPlayers.get(i).update();
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

    private void currentStats()
    {
        team = basketballSim.getRoster(this.teamSide);
        int numPlayers = team.getNumPlayers();
        for (int i = 0; i < numPlayers; i++) {
            Player player = team.getPlayer(i);
            String name = (player.getName());
            String position = (player.getPosistion().toString());
            String fga = (Integer.toString(player.getStatFGA()));
            String fgm = (Integer.toString(player.getStatFGM()));
            String threeFGA = (Integer.toString(player.getStatThreeFGA()));
            String threeFGM = (Integer.toString(player.getStatThreeFGM()));
            String fta = (Integer.toString(player.getStatFTA()));
            String ftm = (Integer.toString(player.getStatFTM()));
            String oRebounds = (Integer.toString(player.getStatORebounds()));
            String dRebounds = (Integer.toString(player.getStatDRebounds()));
            String assists = (Integer.toString(player.getStatAssists()));
            String steals = (Integer.toString(player.getStatSteals()));
            String blocks = (Integer.toString(player.getStatBlocks()));
            String turnovers = (Integer.toString(player.getStatTurnovers()));
            String points = (Integer.toString(player.getPoints()));

            addPlayer(position, name, fga, fgm, threeFGA, threeFGM,
                    oRebounds, dRebounds, assists, fta, ftm, steals,
                    blocks, turnovers, points);
        }
        update();
    }
    private void addPlayer(String position, String name, String fga, String fgm, String threeFGA,
                           String threeFGM, String oRebounds, String dRebounds, String assists, String fta,
                           String ftm, String steals, String blocks, String turnovers, String points)
    {
        Text positionText = new Text(position);
        Text nameText = new Text(name);
        Text fgaText = new Text(fga);
        Text fgmText = new Text(fgm);
        Text threeFGAText = new Text(threeFGA);
        Text threeFGMText = new Text(threeFGM);
        Text ftaText = new Text(fta);
        Text ftmText = new Text(ftm);
        Text oReboundText = new Text(oRebounds);
        Text dReboundText = new Text(dRebounds);
        Text assistsText = new Text(assists);
        Text blocksText = new Text(blocks);
        Text stealsText = new Text(steals);
        Text turnoversText = new Text(turnovers);
        Text pointsText = new Text(points);

        gridBoxPlayers.add(new GridBoxPlayer(row - 1,pointsText, positionText, nameText, fgaText, fgmText,
                threeFGAText, threeFGMText, oReboundText, dReboundText, assistsText, ftaText, ftmText,
                stealsText, blocksText, turnoversText));

        statsGrid.addRow(row++, nameText, pointsText, fgaText, fgmText,
                threeFGAText, threeFGMText, ftaText, ftmText,
                oReboundText, dReboundText, assistsText, stealsText,
                blocksText, turnoversText
        );
    }

    public class GridBoxPlayer
    {
        public int index;
        public Text points;
        public Text position;
        public Text name;
        public Text fga;
        public Text fgm;
        public Text threeFGA;
        public Text threeFGM;
        public Text offensiveRebounds;
        public Text defensiveRebounds;
        public Text assists;
        public Text fta;
        public Text ftm;
        public Text steals;
        public Text blocks;
        public Text turnovers;


        GridBoxPlayer(int index, Text points, Text position, Text name, Text fga,
                      Text fgm, Text threeFGA, Text threeFGM,
                      Text offensiveRebounds, Text defensiveRebounds, Text assists, Text fta, Text ftm,
                      Text steals, Text blocks, Text turnovers)
        {
            this.index = index;
            this.points = points;
            this.position = position;
            this.name = name;
            this.fga = fga;
            this.fgm = fgm;
            this.fta = fta;
            this.ftm = ftm;
            this.threeFGA = threeFGA;
            this.threeFGM = threeFGM;
            this.offensiveRebounds = offensiveRebounds;
            this.defensiveRebounds = defensiveRebounds;
            this.assists = assists;
            this.steals = steals;
            this.blocks = blocks;
            this.turnovers = turnovers;
        }

        public void update()
        {
            this.points.setText(Integer.toString(team.getPlayer(index).getPoints()));
            this.fga.setText(Integer.toString(team.getPlayer(index).getStatFGA()));
            this.fgm.setText(Integer.toString(team.getPlayer(index).getStatFGM()));
            this.threeFGA.setText(Integer.toString(team.getPlayer(index).getStatThreeFGA()));
            this.threeFGM.setText(Integer.toString(team.getPlayer(index).getStatFGM()));
            this.offensiveRebounds.setText(Integer.toString(team.getPlayer(index).getStatORebounds()));
            this.defensiveRebounds.setText(Integer.toString(team.getPlayer(index).getStatDRebounds()));
            this.assists.setText(Integer.toString(team.getPlayer(index).getStatAssists()));
            this.steals.setText(Integer.toString(team.getPlayer(index).getStatSteals()));
            this.blocks.setText(Integer.toString(team.getPlayer(index).getStatBlocks()));
            this.turnovers.setText(Integer.toString(team.getPlayer(index).getStatTurnovers()));
        }
    }

}
