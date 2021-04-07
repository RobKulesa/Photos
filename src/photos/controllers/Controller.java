package photos.controllers;

import photos.Debug;
import photos.structures.UserList;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

/**
 * Abstract class with base functionality of every controller. 
 * Provides functionality for reading users
 */
public abstract class Controller {

  protected Stage mainStage;
  protected UserList userList;

    @FXML
    private Menu menuFile;

    @FXML
    private MenuItem menuItemQuit;

    @FXML
    abstract void menuItemQuitClicked(ActionEvent event);

    public abstract void setMainStage(Stage stage);

    public void errorDialog(String errorMsg) {
        Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle("Error! =(");
		ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.setContentText(errorMsg);
		dialog.getDialogPane().getButtonTypes().add(type);
  		dialog.setResizable(true);
	  	dialog.showAndWait();
	  }

    protected void readUsers() {
      try {
        userList = UserList.readUserList();
      } catch (Exception e) {
          e.printStackTrace();
      }
      if(Debug.debugControllers) System.out.println("LoginController Read User List: " + userList.toString());
    }

    protected void writeUsers() {
      try {
        if(Debug.debugControllers) System.out.println("LoginController Write User List: " + userList.toString());
        UserList.writeUserList(userList);
      } catch (IOException e) { 
        errorDialog("An unexpected error occured. Please try again");
      }
    }

    protected void writeUsersAndQuit(Event event) {
      try {
        if(Debug.debugControllers) System.out.println("LoginController Write User List: " + userList.toString());
        UserList.writeUserList(userList);
        mainStage.close();
      } catch (Exception e) { 
        errorDialog("An unexpected error occured. Please try again");
        event.consume();
        return;
      }
    }
}
