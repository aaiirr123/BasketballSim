package sample;

import basketballSim.TeamReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.io.IOException;

public class CreateTeamController {
    @FXML TextField teamName;
    @FXML TextField coachName;
    @FXML TextField numPlayers;

    public void createTeam(ActionEvent event) throws IOException {
        if (Integer.parseInt(numPlayers.getText()) > 9) {
            TeamReader.writeRandomTeam(teamName.getText(), coachName.getText(), Integer.parseInt(numPlayers.getText()));
        }
    }
}
