package photos.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import photos.app.Photos;
import photos.structures.User;

public class AdminController extends Controller {

    private static boolean allowSelect = true;

    @FXML
    private MenuItem menuItemQuit;

    @FXML
    private ListView<User> listUsers;

    @FXML
    private Label labelListUsers;

    @FXML
    private Label labelAdminPage;

    @FXML
    private Button buttonCreateUser;

    @FXML
    private Pane paneNewUserGroup;

    @FXML
    private Button buttonConfirm;

    @FXML
    private Button buttonCancel;

    @FXML
    private TextField fieldUsername;

    @FXML
    private Label labelUsername;

    @FXML
    private Label labelInvalidUsername;

    @FXML
    private Button buttonDeleteUser;

    @FXML
    private Button buttonLogout;

    @FXML
    private Button buttonQuit;

    @FXML
    void buttonCancelClicked(MouseEvent event) {
        labelInvalidUsername.setVisible(false);
        allowSelect = true;
        paneNewUserGroup.setVisible(false);
        fieldUsername.setEditable(false);
    }

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

    @FXML
    void buttonCreateUserClicked(MouseEvent event) {
        paneNewUserGroup.setVisible(true);
        fieldUsername.setEditable(true);
        fieldUsername.setText("");
        allowSelect = false;
    }

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

    @FXML
    void buttonQuitClicked(MouseEvent event) {
        if(paneNewUserGroup.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsersAndQuit(event);
    }

    @Override
    @FXML
    void menuItemQuitClicked(ActionEvent event) {
        if(paneNewUserGroup.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsersAndQuit(event);
    }

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
