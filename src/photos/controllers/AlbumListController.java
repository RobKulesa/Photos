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
 * Controller for the normal user subsystem.
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
    
    @FXML
    private Button buttonAlbumOpen;

    @FXML
    private Button buttonPhotosSearch;

    @FXML
    private Button buttonRename;

    @FXML
    private Button buttonLogout;
    
    boolean createNotRename;

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

    @FXML
    void buttonPhotosSearchClicked(MouseEvent event){
        if(paneConfirmCreate.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        Photos.getInstance().goToPhotosSearch();
        return;
    }


    @Override
    @FXML
    void buttonCreateClicked(MouseEvent event) {
        // TODO Auto-generated method stub
        super.buttonCreateClicked(event);
        createNotRename = true;
    }


    @FXML 
    void buttonRenameClicked(MouseEvent event){
        createNotRename = false;
        paneConfirmCreate.setVisible(true);
        fieldNewEntry.setEditable(true);
        fieldNewEntry.setText("");
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

    public Album newEntry(String fieldKey){
        return new Album(fieldKey);
    }

    public ArrayList<Album> getCollection() {
        return Photos.getInstance().getCurrentUser().getAlbumList();
    }

    public void removeEntry(Album t){
        this.getCollection().remove(t);
    }

    public boolean isGoodEntry(Album t){
        return !isRepeatEntry(t) && !t.getName().equals("");
    }
}
