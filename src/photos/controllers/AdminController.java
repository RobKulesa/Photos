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

/**
 * Controller for the admin subsystem.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class AdminController extends Controller {

    /**
     * Static boolean for allowing items on the ListView listUsers to be selected.
     */
    private static boolean allowSelect = true;

    /**
     * FXML ListView for the {@link UserList}.
     */
    @FXML
    private ListView<User> listUsers;

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
    private Button buttonCreateUser;

    /**
     * FXML pane for the group of items used to create a new user.
     */
    @FXML
    private Pane paneNewUserGroup;

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
    private TextField fieldUsername;

    /**
     * FXML label for the username field.
     */
    @FXML
    private Label labelUsername;

    /**
     * FXML label for alerting the user of an invalid username.
     */
    @FXML
    private Label labelInvalidUsername;

    /**
     * FXML button to delete the selected user.
     */
    @FXML
    private Button buttonDeleteUser;

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
     * FXML method to cancel creation of new user when button is clicked.
     * 
     * @param event
     */
    @FXML
    void buttonCancelClicked(MouseEvent event) {
        labelInvalidUsername.setVisible(false);
        allowSelect = true;
        paneNewUserGroup.setVisible(false);
        fieldUsername.setEditable(false);
    }

    /**
     * FXML method used to confirm creation of new user when button is clicked.
     * 
     * @param event
     */
    @FXML
    void buttonConfirmClicked(MouseEvent event) {
        String inputUsername = fieldUsername.getText();
        if(inputUsername.isBlank()) {
            labelInvalidUsername.setVisible(true);
            return;
        }
        for(User userInList : userList.getUsers()) {
            if(userInList.getUsername().equalsIgnoreCase(inputUsername)) {
                labelInvalidUsername.setVisible(true);
                return;
            }
        }
        User createdUser = new User(inputUsername);
        userList.addUser(createdUser);
        refreshList();
        listUsers.getSelectionModel().select(createdUser);
        allowSelect = true;

        paneNewUserGroup.setVisible(false);
        fieldUsername.setEditable(false);
    }

    /**
     * FXML method used to begin creating a new user when the button is clicked.
     * 
     * @param event
     */
    @FXML
    void buttonCreateUserClicked(MouseEvent event) {
        paneNewUserGroup.setVisible(true);
        fieldUsername.setEditable(true);
        fieldUsername.setText("");
        allowSelect = false;
    }

    /**
     * FXML method used to delete the selected user when the button is clicked.
     * 
     * @param event
     */
    @FXML
    void buttonDeleteUserClicked(MouseEvent event) {
        User selectedUser = listUsers.getSelectionModel().getSelectedItem();
        if(selectedUser == null) {
            if(listUsers.getItems().size() < 1) {
                errorDialog("No users left to remove!");
                return;
            } else {
                errorDialog("Please select an item to remove first.");
                return;
            }
        } else if(selectedUser.getUsername().equals("admin")) {
            errorDialog("Cannot remove admin.");
            return;
        }
        userList.removeUser(selectedUser);
        refreshList();
    }

    /**
     * FXML method used to logout when the button is clicked.
     * 
     * @param event
     */
    @FXML
    void buttonLogoutClicked(MouseEvent event) {
        if(paneNewUserGroup.isVisible()) {
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
        if(paneNewUserGroup.isVisible()) {
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
        if(paneNewUserGroup.isVisible()) {
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
            if(paneNewUserGroup.isVisible()) {
                errorDialog("Please save pending changes before exiting.");
                event.consume();
                return;
            }
            writeUsersAndQuit(event);
        });
        readUsers();
        refreshList();
        //EventFilter used to stop selection when creating a new user
        listUsers.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
                if(!allowSelect) event.consume();
			}
		});
    }

    /**
     * Update the ListView to display the current {@link UserList}.
     */
    public void refreshList() {
        listUsers.getSelectionModel().clearSelection();
        listUsers.getItems().clear();

        //Load users into list
        listUsers.getItems().addAll(userList.getUsers());
        listUsers.getSelectionModel().select(0);
        if(listUsers.getItems().isEmpty()) {
            buttonDeleteUser.setVisible(false);
            buttonDeleteUser.setDisable(true);
        }
    }
}
