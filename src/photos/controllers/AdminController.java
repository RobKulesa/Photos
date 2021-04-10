package photos.controllers;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import photos.app.Photos;
import photos.structures.User;

/**
 * Controller for the admin subsystem.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class AdminController extends ListController<User> {

    /**
     * FXML label for the ListView.
     */
    @FXML
    private Label labelListUsers;

    /**
     * FXML label for the window.
     */
    @FXML
    private Label labelAdminPage;

    /**
     * FXML button to logout.
     */
    @FXML
    private Button buttonLogout;


    /**
     * FXML method used to logout when the button is clicked.
     * 
     * @param event
     */
    @FXML
    void buttonLogoutClicked(MouseEvent event) {
        if(paneConfirmCreate.isVisible()) {
            errorDialog("Please save pending changes before logging out.");
            return;
        }
        Photos.getInstance().setCurrentUser(-1);
        Photos.getInstance().goToLoginPage();
        writeUsers();
    }

    /**
     * FXML method used to quit the application when the button is clicked.
     * 
     * @param event
     */
    @FXML
    void buttonQuitClicked(MouseEvent event) {
        if(paneConfirmCreate.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsersAndQuit(event);
    }

    /**
     * FXML method used to quit the application when the menu item is clicked.
     */
    @Override
    @FXML
    void menuItemQuitClicked(ActionEvent event) {
        if(paneConfirmCreate.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsersAndQuit(event);
    }

    /**
     * Implemented method for setting the main stage for this controller.
     * Loads the users from filesystem, displays and displays them in the list.
     * 
     * @param stage    The stage to be set as main stage for this controller.
     */
    @Override
    public void setMainStage(Stage stage) {
        mainStage = stage;
        mainStage.setOnCloseRequest(event -> {
            if(paneConfirmCreate.isVisible()) {
                errorDialog("Please save pending changes before exiting.");
                event.consume();
                return;
            }
            writeUsersAndQuit(event);
        });
        readUsers();
        refreshList(null);
        //EventFilter used to stop selection when creating a new user
        listView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
                if(!allowSelect) event.consume();
			}
		});
    }

    public User newEntry(String fieldKey){
        return new User(fieldKey);
    }

    public ArrayList<User> getCollection(){
        return Photos.getInstance().getUserList().getUsers();
    }

    public void removeEntry(User t){
        Photos.getInstance().getUserList().removeUser(t);
    }

    public boolean isGoodEntry(User t){
        return !isRepeatEntry(t);
    }
}
