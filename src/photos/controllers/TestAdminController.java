package photos.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import photos.app.Photos;
import photos.structures.User;
import java.util.ArrayList;

/**
 * Controller for the admin subsystem.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class TestAdminController extends ListController<User>{

    /**
     * Static boolean for allowing items on the ListView listUsers to be selected.
     */
    private static boolean allowSelect = true;

    /**
     * FXML ListView for the {@link UserList}.
     */
    @FXML
    private ListView<User> listView;

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
     * FXML button to create a new user.
     */
    @FXML
    private Button buttonCreate;

    /**
     * FXML pane for the group of items used to create a new user.
     */
    @FXML
    private Pane paneConfirmCreate;

    /**
     * FXML button to confirm creation of new user.
     */
    @FXML
    private Button buttonConfirm;

    /**
     * FXML button to cancel creation of new user.
     */
    @FXML
    private Button buttonCancel;

    /**
     * FXML TextField to input the username of the new user.
     */
    @FXML
    private TextField fieldNewEntry;

    /**
     * FXML label for the username field.
     */
    @FXML
    private Label labelUsername;

    /**
     * FXML label for alerting the user of an invalid username.
     */
    @FXML
    private Label labelInvalidAddition;

    /**
     * FXML button to delete the selected user.
     */
    @FXML
    private Button buttonDelete;

    /**
     * FXML button to logout.
     */
    @FXML
    private Button buttonLogout;

    /**
     * FXML button to quit the application.
     */
    @FXML
    private Button buttonQuit;


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
        Photos.getInstance().setCurrentUser(null);
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
        refreshList();
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
        return userList.getUsers();
    }

    public void removeEntry(User t){
        userList.removeUser(t);
    }
}
