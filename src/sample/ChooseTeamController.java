package sample;

import basketballSim.TeamReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseTeamController implements Initializable {
    @FXML
    ChoiceBox<String> userTeam;
    @FXML
    ChoiceBox<String> cpuTeam;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void playGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/startSim.fxml"));
        Parent root = loader.load();
        GameSimController gameSimController = loader.getController();
        gameSimController.initTeams(TeamReader.getTeam("src/teams/" + userTeam.getValue()), TeamReader.getTeam("src/teams/" + cpuTeam.getValue()));
        loadScreen(root, event);
    }

    private void loadScreen(Parent root, ActionEvent event)
    {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> results = new ArrayList<String>();

        File[] files = new File("src/teams").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        userTeam.getItems().add("Random Team");
        cpuTeam.getItems().add("Random Team");

        for (File file : files) {
            if (file.isFile() && file != null) {
                results.add(file.getName());
                userTeam.getItems().add(file.getName());
                cpuTeam.getItems().add(file.getName());
            }
        }
    }
}
