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
import javafx.stage.Stage;

import javax.swing.*;

public class Controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private void loadScreen(Parent root, ActionEvent event)
    {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void playGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/chooseTeam.fxml"));
        Parent root = loader.load();
        //GameSimController gameSimController = loader.getController();
        loadScreen(root, event);
    }

    public void createTeamButtonPress(ActionEvent event) throws IOException
    {
        switchToCreatePlayer(event);
    }
    public void switchToCreatePlayer(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/createPlayer.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToCreateTeam(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("createTeam.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
