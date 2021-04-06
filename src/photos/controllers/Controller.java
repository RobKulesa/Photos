package photos.controllers;

import photos.structures.UserList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public abstract class Controller {

    protected Stage mainStage;
    protected UserList userList;

    @FXML
    private Menu menuFile;

    @FXML
    private MenuItem menuItemQuit;

    @FXML
    abstract void menuItemQuitClicked(ActionEvent event);

    abstract void setMainStage(Stage stage);

    public void errorDialog(String errorMsg) {
		Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle("Error! =(");
		ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.setContentText(errorMsg);
		dialog.getDialogPane().getButtonTypes().add(type);
		dialog.setResizable(true);
		dialog.showAndWait();
	}
}
