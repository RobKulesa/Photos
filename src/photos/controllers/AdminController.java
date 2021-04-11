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

    
    /**
     * Implemented method for instantiating a new <code>User</code> entry into the <code>ListView<T></code>
     * and the <code>User</code> collection it represents
     * 
     * @param fieldKey          the String that will identify and instantiate the new entry
     * @return User             the newly instantiated <code>User</code> entry
     */
    public User newEntry(String fieldKey){
        return new User(fieldKey);
    }

    
    /** 
     * Implemented method for retrieving the collection that 
     * represents the controller's <code>ListView<User></code>
     * 
     * @return ArrayList<User>
     */
    public ArrayList<User> getCollection(){
        return Photos.getInstance().getUserList().getUsers();
    }

    
    /** 
     * Implemented method that removed an entry from the collection that represents the controller's <code>ListView<User></code>
     * 
     * @param t     the entry to be deleted
     */
    public void removeEntry(User t){
        Photos.getInstance().getUserList().removeUser(t);
    }

    
    /** 
     * Implemented method that determines if an entry is valid enough to be inserted into the the controller's collection
     * 
     * @param entry     the entry in question
     * @return boolean  the argument's validity for insertion purposes
     */
    public boolean isGoodEntry(User t){
        return !isRepeatEntry(t);
    }
}
