package graphics.userInterface.controllers;

import client.ClientGameState;
import graphics.enums.UIColour;
import graphics.userInterface.resources.style.Shadow;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/* Menu buttons:
1. Play
2. Options
3. Help
4. Exit
*/
//Gonna replace old MenuController once everything goes well
public class MenuNewController extends UIController implements Initializable{
    @FXML
    public Text alien;
    @FXML
    public Button play,help,option,exit;
    @FXML
    public Label play_label,help_label,option_label,exit_label;
    @FXML
    VBox background;

    private Color outline;
    private ClientGameState gameState;


    // play -> mode selection
    @FXML
    public void handlePlayBtn(ActionEvent actionEvent) {

        // create game rules
        // todo make this configurable
        //select mode
        String fxmlPath = "../fxmls/mode.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }

    @FXML
    public void handleOptionBtn(ActionEvent actionEvent){
        String fxmlPath ="../fxmls/option.fxml";
        String stageTitle ="Option Setup" ;
        String fileException ="Option";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }



    @FXML
    public void handleHelpBtn(ActionEvent actionEvent){
        String fxmlPath ="../fxmls/help.fxml";
        String stageTitle ="Tutorial" ;
        String fileException ="Help";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);

    }

    @FXML
    public void handleExitBtn(ActionEvent actionEvent){
        stage.close();

        // todo make this graceful

        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //get color from UIColour
        background.setBackground(new Background(new BackgroundFill(UIColour.BACKGROUND.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        outline = Color.WHITE; //TODO:get border colour from UIColour

        alien.setFill(UIColour.HEAD_TEXT.getColor());

        play_label.setEffect(blend);
        help_label.setEffect(blend);
        option_label.setEffect(blend);
        exit_label.setEffect(blend);
    }
}
