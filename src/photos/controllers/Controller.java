package photos.controllers;

import photos.Debug;
import photos.app.Photos;
import photos.structures.UserList;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

/**
 * Abstract class with base functionality and fields of every controller.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public abstract class Controller {

    /**
     * Reference the main stage.
     */
    protected Stage mainStage;

    /**
     * FXML File Menu > Quit Menu Item
     */
    @FXML
    private MenuItem menuItemQuit;

    /**
     * Abstract FXML method for pressing the Quit Menu Item.
     * 
     * @param event
     */
    @FXML
    abstract void menuItemQuitClicked(ActionEvent event);

    /**
     * Abstract method for setting the main stage for this controller.
     * 
     * @param stage    The stage to be set as main stage for this controller.
     */
    public abstract void setMainStage(Stage stage);

    /**
     * Open an error popup dialog with the provided error message.
     * 
     * @param errorMsg    Error message to be shown in popup dialog.
     */
    protected void errorDialog(String errorMsg) {
        Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle("Error! =(");
		ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.setContentText(errorMsg);
		dialog.getDialogPane().getButtonTypes().add(type);
		dialog.setResizable(true);
        dialog.showAndWait();
	}

    /**
     * Read the userList from filesystem using {@link UserList}.readUserList().
     */
    protected void readUsers() {
        try {
            Photos.getInstance().setUserList(UserList.readUserList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Debug.debugControllers) System.out.println("LoginController Read User List: " + Photos.getInstance().getUserList().toString());
    }

    /**
     * Write the userList to filesystem using {@link UserList}.writeUserList(userList).
     */
    protected void writeUsers() {
        try {
            if(Debug.debugControllers) System.out.println("LoginController Write User List: " + Photos.getInstance().getUserList().toString());
            UserList.writeUserList(Photos.getInstance().getUserList());
        } catch (IOException e) { 
            errorDialog("An unexpected error occured. Please try again");
        }
    }

    /**
     * Write the userList to filesystem using {@link UserList}.writeUserList(userList) and quit application.
     * 
     * @param event    The event causing the quit request.
     */
    protected void writeUsersAndQuit(Event event) {
        try {
            if(Debug.debugControllers && Photos.getInstance().getUserList() != null) System.out.println("LoginController Write User List: " + Photos.getInstance().getUserList().toString());
            UserList.writeUserList(Photos.getInstance().getUserList());
            mainStage.close();
        } catch (Exception e) { 
            errorDialog(e.getMessage());
            e.printStackTrace();
            event.consume();
            return;
        }
    }
}
