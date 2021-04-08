package photos.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.control.MenuItem;
import java.util.ArrayList;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import photos.Debug;
import javafx.scene.input.MouseEvent;

import photos.app.Photos;
import photos.structures.User;
import photos.structures.User;
import photos.structures.Album;
/**
 * Controller for the normal user subsystem.
 * 
 * @author Aaron Kan
 * @author Robert Kulesa 
 */

public class AlbumListController extends ListController<Album>{
    
    
    private static boolean allowSelect = true;
    
    @FXML
    private ListView<Album> listView;

    @FXML
    private Button buttonCreate;

    @FXML
    private Pane paneConfirmCreate;

    @FXML
    private TextField fieldNewEntry;

    @FXML
    private Label labelEntryField;

    @FXML
    private Button buttonConfirm;

    /**
     * FXML button to cancel creation of new user.
     */
    @FXML
    private Button buttonCancel;

    @FXML 
    private Label labelInvalidAddition;

    @FXML 
    private Button buttonDelete;

    @FXML
    private MenuItem menuItemQuit;

    /**
     * FXML Label used for displaying the current user's username
     */
    @FXML
    private Label usernameDisplay;
    
    @FXML
    private Button buttonAlbumOpen;

    @FXML
    private Button buttonAlbumSearch;

    @FXML
    private Button buttonLogout;

    @FXML
    private Button buttonQuit;
    
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
     * Refreshes the label used to display the current user's username
     */
    public void refreshUsernameDisplay(){
        usernameDisplay.setText(Photos.getInstance().getCurrentUser().getUsername());
    }

    /**
     * Implemented method for setting the main stage for this controller.
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
        listView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
                if(!allowSelect) event.consume();
			}
		});
        refreshUsernameDisplay();
    }   

    public Album newEntry(String fieldKey)
}
