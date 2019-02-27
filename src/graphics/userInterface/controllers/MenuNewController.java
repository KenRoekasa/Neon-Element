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
    public Text alien,play,help,option,exit;
    @FXML
    VBox background;

    private Color outline;
    private ClientGameState gameState;


    // play -> mode selection
    @FXML
    public void handlePlayBtn(MouseEvent actionEvent) {

        // create game rules
        // todo make this configurable

        //select mode
        String fxmlPath = "../fxmls/mode_board.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }

    @FXML
    public void handleOptionBtn(MouseEvent actionEvent){
        String fxmlPath ="../fxmls/option.fxml";
        String stageTitle ="Option Setup" ;
        String fileException ="Option";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);

//        //select mode
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/option.fxml"));
//
//        try {
//            Pane root = loader.load();
//            stage.getScene().setRoot(root);
//            SettingController controller = loader.getController();
//            controller.setStage(stage);
//            stage.setTitle("Mode");
//
//        } catch (IOException e) {
//            System.out.println("crush in loading setting board ");
//            e.printStackTrace();
//        }
    }



    @FXML
    public void handleHelpBtn(MouseEvent actionEvent){
        String fxmlPath ="../fxmls/help.fxml";
        String stageTitle ="Tutuorial" ;
        String fileException ="Help";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);

    }

    @FXML
    public void handleExitBtn(MouseEvent actionEvent){
        stage.close();

        // todo make this graceful

        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //get color from UIColour
        background.setBackground(new Background(new BackgroundFill(UIColour.BACKGROUND.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        outline= Color.WHITE;

        alien.setFill(UIColour.HEAD_TEXT.getColor());

        play.setFill(outline);
        help.setFill(outline);
        option.setFill(outline);
        exit.setFill(outline);

        //Set effect
        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);

        DropShadow ds = new DropShadow();
        ds.setColor(Color.WHITE);
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setRadius(5);
        ds.setSpread(0.2);

        blend.setBottomInput(ds);

        DropShadow ds1 = new DropShadow();
        ds1.setColor((Color) UIColour.NEON_BORDER.getColor());
        ds1.setRadius(10);
        ds1.setSpread(0.2);

        Blend blend2 = new Blend();
        blend2.setMode(BlendMode.MULTIPLY);

        InnerShadow is = new InnerShadow();
        is.setColor((Color) UIColour.NEON_BORDER.getColor());
        is.setRadius(2);
        is.setChoke(0.8);
        blend2.setBottomInput(is);

        InnerShadow is1 = new InnerShadow();
        is1.setColor((Color) UIColour.NEON_BORDER.getColor());
        //	#55185F dark purple

        is1.setRadius(2);
        is1.setChoke(0.4);
        blend2.setTopInput(is1);

        Blend blend1 = new Blend();
        blend1.setMode(BlendMode.MULTIPLY);
        blend1.setBottomInput(ds1);
        blend1.setTopInput(blend2);
        blend.setTopInput(blend1);

        play.setEffect(blend);
        help.setEffect(blend);
        option.setEffect(blend);
        exit.setEffect(blend);

    }
}
