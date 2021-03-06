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
import photos.structures.Album;

/**
 * Controller for the part of normal user subsystem
 * that displays the user's list of albums.
 * 
 * @author Aaron Kan
 * @author Robert Kulesa 
 */

public class AlbumListController extends ListController<Album> {

    /**
     * FXML Label used for displaying the current user's username
     */
    @FXML
    private Label usernameDisplay;
    
    /**
     * FXML Button used for navigating to the albumopen page
     */
    @FXML
    private Button buttonAlbumOpen;

    /**
     * FXML Button used for navigating to the photossearch page
     */
    @FXML
    private Button buttonPhotosSearch;

    /**
     * FXML Button used for renaming an album
     */
    @FXML
    private Button buttonRename;

    /**
     * FXML Button used for logging out and returning to the login screen
     */
    @FXML
    private Button buttonLogout;
    
    /**
     * A boolean that tells the application whether or not an edit 
     * is a creation operation or a renaming operation
     */
    boolean createNotRename;

    
    /** 
     * Event handler that navigates to the albumopen page 
     * when the associated event is fired under appropriate circumstances
     * 
     * @param event     The event where the <code>buttonAlbumOpen</code> is clicked
     */
    @FXML
    void buttonAlbumOpenClicked(MouseEvent event){
        if(paneConfirmCreate.isVisible()) {
            errorDialog("Please save pending changes before leaving page.");
            return;
        }

        Album selectedEntry = listView.getSelectionModel().getSelectedItem();
        if(selectedEntry == null){
            errorDialog("Please select an album to open first.");
            return;
        }

        Photos.getInstance().setCurrentAlbum(selectedEntry);
        Photos.getInstance().goToAlbumOpen();
        return;
    }

    
    /** 
     * Event handler that navigates the the photossearch page
     * when the associated event is fired and the appropriate conditions are met
     * 
     * @param event     The event where <code>buttonPhotosSearch</code> is clicked
     */
    @FXML
    void buttonPhotosSearchClicked(MouseEvent event){
        if(paneConfirmCreate.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsers();
        Photos.getInstance().goToPhotosSearch();
    }

    /** 
     * An overriden implementation of the buttonCreateClicked event handler from the
     * <code>ListController</code> superclass.
     * 
     * @param event    The event caused by the create button being clicked.
     */
    @Override
    @FXML
    void buttonCreateClicked(MouseEvent event) {
        super.buttonCreateClicked(event);
        createNotRename = true;
    }

    /** 
     * Event handler that handles the rename-button being clicked.
     * Sets the appropriate controls to visible in response to the event.
     * 
     * @param event     The event in which the rename-button is clicked.
     */
    @FXML 
    void buttonRenameClicked(MouseEvent event){
        createNotRename = false;
        paneConfirmCreate.setVisible(true);
        fieldNewEntry.setEditable(true);
        fieldNewEntry.setText("");
    }

    /**
     * FXML method used to quit the application when the menu item is clicked.
     * @param event     The event in which the quit-menuItem is clicked
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
     * @param event     The event in which the logout button is clicked.
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
     * @param event     The event in which the quit button is clicked. 
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
     * Overriden FXML method used to confirma user's proposed change to a particular album
     * 
     * @param event     The event in which the confirm button is clicked.
     */
    @Override
    @FXML
    void buttonConfirmClicked(MouseEvent event){
        String inputEntryName = fieldNewEntry.getText();
        Album selectedEntry;
        if(createNotRename){
            selectedEntry = newEntry(inputEntryName);
            if(inputEntryName.isBlank()){
                labelInvalidAddition.setVisible(true);
                return;
            }

            if(!isGoodEntry(selectedEntry)){
                labelInvalidAddition.setVisible(true);
                return;
            }
            getCollection().add(selectedEntry);
        } else {
            selectedEntry = listView.getSelectionModel().getSelectedItem();
            if(selectedEntry == null){
                errorDialog("An album must be selected before confirming rename");
                return;
            }
            if(inputEntryName.equals("")){
                errorDialog("Cannot rename album with empty string");
                return;
            }
            selectedEntry.setName(inputEntryName);
        }
        refreshList(selectedEntry);
        allowSelect = true;

        paneConfirmCreate.setVisible(false);
        fieldNewEntry.setEditable(false);
        labelInvalidAddition.setVisible(false);
        writeUsers();
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
        refreshList(null);
        
        listView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
                if(!allowSelect) event.consume();
			}
		});
        refreshUsernameDisplay();
    }   

    /**
     * Implemented for instantiating a new entry into the <code>ListView</code> of Albums
     * and the collection it represents
     * 
     * @param fieldKey      the String that will identify and instantiate the new Album
     * @return T            the newly instantiated entry
     */
    public Album newEntry(String fieldKey){
        return new Album(fieldKey);
    }

    /**
     * Implemented method for retrieving the collection that represents the controller's <code>ListView</code> of Albums.
     * 
     * @return ArrayList  the collection associated with the controller's <code>ListView</code> of Albums.
     */
    public ArrayList<Album> getCollection() {
        return Photos.getInstance().getCurrentUser().getAlbumList();
    }
    
    /**
     * Implemented method that removed an entry from the collection that represents the controller's <code>ListView</code> of Albums.
     * 
     * @param t     the Album to be deleted
     */
    public void removeEntry(Album t){
        this.getCollection().remove(t);
    }

    
    /**
     * Implemented method that determines if an Album is valid enough to be inserted into the the controller's collection
     * 
     * @param t     the entry in question
     * @return boolean  the argument's validity for insertion purposes
     */
    public boolean isGoodEntry(Album t){
        return !isRepeatEntry(t) && !t.getName().equals("");
    }
}
