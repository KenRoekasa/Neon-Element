package graphics.userInterface.controllers;

import javafx.fxml.FXML;

/**
 * Controller for mode.fxml which contains online and local modes
 */
/*Mode board: online/local*/
public class ModeController extends UIController{
    /** Handle the action when press local button which direct to the local_setup.fxml
     */
   //local -> local_setup
    @FXML
    public void handleLocalBtn(){
        String fxmlPath = "../fxmls/local_setup.fxml";
        String stageTitle = "Local Mode Configuration";
        String fileException ="Local Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    /** Handle the action when press online button which direct to the local_setup.fxml
     */
    //online->choose host/ join
    @FXML
    public void handleOnlineBtn(){
        String fxmlPath ="../fxmls/online_mode.fxml";
        String stageTitle ="Online Mode Selection" ;
        String fileException ="Online Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }


    /** Handle the action of clicking back button which will go back to the menu.fxml
     */
    //back->mode board
    @FXML
    public void handleBackBtn(){
        String fxmlPath ="../fxmls/menu.fxml";
        String stageTitle ="Menu" ;
        String fileException ="Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

}
