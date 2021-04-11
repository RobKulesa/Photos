package photos.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.text.SimpleDateFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photos.app.Photos;
import photos.structures.Album;
import photos.structures.Photo;
/**
 * Controller that allows the user to interact with the search results
 * from their search.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class SearchResultsOpenController extends ListController<Photo> implements Initializable {
    
    /**
     * FXML MenuItem used to quit out of the program
     */
    @FXML
    private MenuItem menuItemQuit;
    
    /**
     * FXML ImageView used to display the current selected Photo
     */
    @FXML
    private ImageView imageView;

    /**
     * FXML label used to display the current selected Photo's date
     */
    @FXML
    private Label labelDate;

    /**
     * FXML label used to display the current selected Photo's caption
     */
    @FXML 
    private Label labelCaption;

    /**
     * FXML button used to add or edit a photo's caption
     */
    @FXML
    private Button buttonAddEditCaption;

    /**
     * FXML AnchorPane used to hold the controls needed to add/edit a caption
     */
    @FXML
    private AnchorPane paneAddEditCaption;

    /**
     * FXML Button used to add a Tag to a photo
     */
    @FXML
    private Button buttonAddTag;

    /**
     * FXML Button used to remove a tag from a photo
     */
    @FXML
    private Button buttonRemoveTag;

    /**
     * FXML TextField used to enter a new caption for a photo
     */
    @FXML
    private TextField fieldNewCaption;

    /**
     * FXML Button used to confirm a caption edit
     */
    @FXML
    private Button buttonConfirmNewCaption;

    /**
     * FXML Button used to cancel a caption edit
     */
    @FXML
    private Button buttonCancelNewCaption;

    /**
     * FXML ListView used to display all the tags of the currently selected Photo
     */
    @FXML
    private ListView<String> listViewTags;

    /**
     * FXML Anchorpane used to hold the controls needed for Tag creation functionality
     */
    @FXML
    private AnchorPane paneAddTag;

    /**
     * FXML TextField used to input a name for a new Tag
     */
    @FXML
    private TextField fieldTagName;

    /**
     * FXML Button used to confirm the creation of a new Tag
     */
    @FXML
    private Button buttonConfirmNewTag;

    /**
     * FXML Button used to cancel the creation of a new Tag
     */
    @FXML
    private Button buttonCancelNewTag;

    /**
     * FXML TextField used to input a value for a new Tag
     */
    @FXML
    private TextField fieldTagValue;

    /**
     * FXML ListView used to display all the albums a user owns
     */
    @FXML
    private ListView<Album> listViewAlbums;

    /**
     * FXML AnchorPane used to hold and view the controls needed to move and copy photos to another album
     */
    @FXML
    private AnchorPane paneMoveCopy;

    /**
     * FXML Button used to move a Photo from one album to another
     */
    @FXML
    private Button buttonMovePhoto;

    /**
     * FXML Button used to add a copy of the currently selected photo to another Album
     */
    @FXML
    private Button buttonCopyPhoto;

    /**
     * FXML Label used to show if a copy operation was successful
     */
    @FXML
    private Label labelCopySuccessful;

    /**
     * FXML Label used to show if a Photo move operation was successful
     */
    @FXML
    private Label labelMoveSuccessful;

    /**
     * FXML Button used to take the user back to the albumlist page
     */
    @FXML
    private Button buttonBack;

    /**
     * FXML AnchorPane holds components used to create album from search results.
     */
    @FXML
    private AnchorPane paneCreateAlbum;

    /**
     * FXML TextField for the album name that will be created from search results.
     */
    @FXML
    private TextField fieldAlbumName;

    /**
     * FXML Button for confirming adding the new album from the search results.
     */
    @FXML
    private Button buttonConfirmNewAlbum;

    /**
     * FXML Button for canceling adding the new album from the search results.
     */
    @FXML
    private Button buttonCancelNewAlbum;

    /**
     * FXML Button to begin creating album from the search results.
     */
    @FXML
    private Button buttonCreateNewAlbum;

    
    /** 
     * Method for when the user clicks the create album from search results button.
     * 
     * @param event     Event that represents the create album from search reuslts button being clicked
     */
    @FXML
    void buttonCreateNewAlbumClicked(MouseEvent event) {
        paneCreateAlbum.setVisible(true);
        fieldAlbumName.setEditable(true);
        buttonConfirmNewAlbum.setDisable(false);
        buttonCancelNewAlbum.setDisable(false);
    }

    
    /** 
     * Method for when the user clicks the Confirm New Album button
     * 
     * @param event     Event that represents the Confirm New Album button being clicked
     */
    @FXML
    void buttonConfirmNewAlbumClicked(MouseEvent event) {
        if(paneConfirmCreate.isVisible() || paneAddEditCaption.isVisible() || paneAddTag.isVisible()) {
            errorDialog("Please save pending changes before adding album.");
            return;
        }

        if(fieldAlbumName.getText().isEmpty()) {
            errorDialog("Album Name cannot be empty!");
            return;
        }

        if(listView.getItems().size() <= 0) {
            errorDialog("Please select a photo first!");
            return;
        }

        for(Album a : Photos.getInstance().getCurrentUser().getAlbumList()) {
            if(a.equals(fieldAlbumName.getText())) {
                errorDialog("Album Name cannot be the same as one of your other albums!");
                return;
            }
        }
        Photos.getInstance().getSearchResults().setName(fieldAlbumName.getText());
        Photos.getInstance().getCurrentUser().getAlbumList().add(Photos.getInstance().getSearchResults());
        Photos.getInstance().setCurrentAlbum(Photos.getInstance().getSearchResults());
        paneCreateAlbum.setVisible(false);
        fieldAlbumName.setEditable(false);
        fieldAlbumName.setText("");
        buttonConfirmNewAlbum.setDisable(true);
        buttonCancelNewAlbum.setDisable(true);
        writeUsers();
        Photos.getInstance().goToAlbumOpen();
    }

    
    /** 
     * Method for when the user clicks the Cancel New Album button
     * 
     * @param event     Event that represents the Cancel New Album button being clicked
     */
    @FXML
    void buttonCancelNewAlbumClicked(MouseEvent event) {
        paneCreateAlbum.setVisible(false);
        fieldAlbumName.setEditable(false);
        fieldAlbumName.setText("");
        buttonConfirmNewAlbum.setDisable(true);
        buttonCancelNewAlbum.setDisable(true);
    }

    /** 
     * Event handler that opens the caption addition/edit functionality
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the add/edit caption button being clicked
     */
    @FXML
    void buttonAddEditCaptionClicked(MouseEvent event) {
        if(listView.getSelectionModel().getSelectedItem() == null) {
            errorDialog("Please select a photo first!");
            return;
        }
        allowSelect = false;
        paneAddEditCaption.setVisible(true);
        fieldNewCaption.setEditable(true);
        buttonConfirmNewCaption.setDisable(false);
        buttonCancelNewCaption.setDisable(false);
    }

    
    /** 
     * FXML Event handler that cancels the creation of a new caption
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the CancelNewCaption button being clicked
     */
    @FXML
    void buttonCancelNewCaptionClicked(MouseEvent event) {
        allowSelect = true;
        buttonCancelNewCaption.setDisable(true);
        buttonConfirmNewCaption.setDisable(true);
        fieldNewCaption.setEditable(false);
        paneAddEditCaption.setVisible(false);
    }

    
    /** 
     * FXML Event handler that adds a tag to the currently selected photo 
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Add Tag button being clicked
     */
    @FXML
    void buttonAddTagClicked(MouseEvent event) {
        if(listView.getSelectionModel().getSelectedItem() == null) {
            errorDialog("Please select a photo first!");
            return;
        }
        allowSelect = false;
        paneAddTag.setVisible(true);
        fieldTagName.setEditable(true);
        fieldTagValue.setEditable(true);
        buttonConfirmNewTag.setDisable(false);
        buttonCancelNewTag.setDisable(false);
    }

    
    /** 
     * FXML Event handler that cancels the creation of a new Tag 
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Cancel New Tag button being clicked
     */
    @FXML
    void buttonCancelNewTagClicked(MouseEvent event) {
        allowSelect = true;
        buttonCancelNewTag.setDisable(true);
        buttonConfirmNewTag.setDisable(true);
        fieldTagValue.setEditable(false);
        fieldTagName.setEditable(false);
        paneAddTag.setVisible(false);
    }

    
    /** 
     * FXML Event handler that navigates to the photossearch page 
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Back button being clicked
     */
    @FXML
    void buttonBackClicked(MouseEvent event){
        if(paneConfirmCreate.isVisible() || paneAddEditCaption.isVisible() || paneAddTag.isVisible() || paneCreateAlbum.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsers();
        Photos.getInstance().goToAlbumList();
    }

    
    /** 
     * FXML Event handler that saves the selected Photo's caption edit 
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Confirm New Caption button being clicked
     */
    @FXML
    void buttonConfirmNewCaptionClicked(MouseEvent event) {
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        String newCaption = fieldNewCaption.getText();
        if(newCaption == null){
            newCaption = "";
        }
        
        selectedPhoto.setCaption(newCaption);
        refreshList(null);
        refreshImageView();
        refreshDate();
        refreshCaption();
        refreshTagsList();
        refreshAlbumsList();

        allowSelect = true;

        buttonCancelNewCaption.setDisable(true);
        buttonConfirmNewCaption.setDisable(true);
        fieldNewCaption.setEditable(false);
        paneAddEditCaption.setVisible(false);
    }

    
    /** 
     * FXML Event handler that saves the creation of a new Tag 
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Confirm New Tag button being clicked
     */
    @FXML
    void buttonConfirmNewTagClicked(MouseEvent event) {
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        String newTagName = fieldTagName.getText();
        String newTagVal = fieldTagValue.getText();
        if(newTagName.isEmpty() || newTagVal.isEmpty()){
            errorDialog("New Tag Fields Cannot Be Empty!");
            return;
        }
        
        try {
            selectedPhoto.addTag(newTagName, newTagVal);
        } catch(IllegalArgumentException e) {
            errorDialog(e.getMessage());
            return;
        }
        refreshList(null);
        refreshImageView();
        refreshDate();
        refreshCaption();
        refreshTagsList();
        refreshAlbumsList();

        allowSelect = true;

        buttonCancelNewTag.setDisable(true);
        buttonConfirmNewTag.setDisable(true);
        fieldTagValue.setEditable(false);
        fieldTagName.setEditable(false);
        paneAddTag.setVisible(false);
    }

    
    /** 
     * FXML Event handler that copies the slected photo to another album 
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Copy Photo button being clicked
     */
    @FXML
    void buttonCopyPhotoClicked(MouseEvent event) {
        Album selectedAlbum = listViewAlbums.getSelectionModel().getSelectedItem();
        Album currentAlbum = Photos.getInstance().getSearchResults();
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        if(selectedAlbum == null) {
            errorDialog("Please Select an Album First!");
            return;
        }
        if(selectedPhoto == null) {
            errorDialog("Please select a Photo First!");
            return;
        }

        if(selectedAlbum.equals(currentAlbum)) {
            errorDialog("Please select an album other than the currently opened one.");
            return;
        }

        if(selectedAlbum.containsPhoto(selectedPhoto)) {
            errorDialog("This photo is already in that album!");
            return;
        }
        selectedAlbum.addPhoto(selectedPhoto);
        successDialog("Copied Photo!");
        refreshList(null);
        refreshImageView();
        refreshDate();
        refreshCaption();
        refreshTagsList();
        refreshAlbumsList();
    }


    
    /** 
     * FXML Event handler that moves the selected Photo to the selected album
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Move Photo button being clicked
     */
    @FXML
    void buttonMovePhotoClicked(MouseEvent event) {
        Album selectedAlbum = listViewAlbums.getSelectionModel().getSelectedItem();
        Album currentAlbum = Photos.getInstance().getSearchResults();
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        if(selectedAlbum == null) {
            errorDialog("Please Select an Album First!");
            return;
        }
        if(selectedPhoto == null) {
            errorDialog("Please select a Photo First!");
            return;
        }

        if(selectedAlbum.equals(currentAlbum)) {
            errorDialog("Please select an album other than the currently opened one.");
            return;
        }

        if(selectedAlbum.containsPhoto(selectedPhoto)) {
            errorDialog("This photo is already in this album!");
            return;
        }
        selectedAlbum.addPhoto(selectedPhoto);
        Photos.getInstance().getSearchResults().deletePhoto(selectedPhoto);
        successDialog("Moved Photo!");
        refreshList(null);
        refreshImageView();
        refreshDate();
        refreshCaption();
        refreshTagsList();
        refreshAlbumsList();
    }
 
    
    /** 
     * FXML Event handler that removes the selected tag
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Remove Tag button being clicked
     */
    @FXML
    void buttonRemoveTagClicked(MouseEvent event) {
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        if(selectedPhoto == null) {
            errorDialog("Please select a photo first!");
            return;
        }
        String[] tag = listViewTags.getSelectionModel().getSelectedItem().split(",", 2);
        String name = tag[0].substring(1);
        String val = tag[1].substring(1, tag[1].length()-1);
        selectedPhoto.removeTag(name, val);
        refreshList(null);
        refreshImageView();
        refreshDate();
        refreshCaption();
        refreshTagsList();
        refreshAlbumsList();
    }

    
    /** 
     * FXML Event handler that quits the application
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Quit MenuItem being clicked
     */
    @Override
    @FXML
    void menuItemQuitClicked(ActionEvent event) {
        if(paneConfirmCreate.isVisible() || paneAddEditCaption.isVisible() || paneAddTag.isVisible() || paneCreateAlbum.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsersAndQuit(event);
    }

    /**
     * Method that reloads the ImageView to display 
     * the image of currently selected Photo
     */
    public void refreshImageView(){
        try{
            Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
            String absolutePath = FileSystems.getDefault().getPath(selectedPhoto.getPath()).normalize().toAbsolutePath().toString();
            InputStream inputStream = new FileInputStream(absolutePath);
            Image img = new Image(inputStream);
            imageView.setImage(img);
            inputStream.close();
        } catch(Exception ex){
            errorDialog(ex.getMessage());
        }
        
    }

    /**
     * Method that reloads the caption label to display 
     * the caption of currently selected Photo
     */
    public void refreshCaption(){
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        labelCaption.setText(selectedPhoto.getCaption());
    }

    /**
     * Method that reloads the date label to display 
     * the date of currently selected Photo
     */
    public void refreshDate(){
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        fmt.setCalendar(selectedPhoto.getLastModified());
        String formattedDate = fmt.format(selectedPhoto.getLastModified().getTime());
        labelDate.setText(formattedDate);
    }

    
    /** 
     * FXML Event handler that refreshes the appropriate controls
     * upon a new Photo being selected
     * 
     * @param event     The event in which a new Photo in the Photo ListView is selected
     */
    @FXML
    void photosListViewSelected(MouseEvent event) {
        refreshImageView();
        refreshCaption();
        refreshDate();
        refreshTagsList();        
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
            if(paneConfirmCreate.isVisible() || paneAddEditCaption.isVisible() || paneAddTag.isVisible() || paneCreateAlbum.isVisible()) {
                errorDialog("Please save pending changes before exiting.");
                event.consume();
                return;
            }
            writeUsersAndQuit(event);
        });
        readUsers();
        refreshList(null);
        refreshTagsList();
        refreshAlbumsList();
        
        listView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
                if(!allowSelect) event.consume();
			}
		});

        listViewTags.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!allowSelect) event.consume();
            }
        });
    }

    /**
     * Method that reloads the current Tags ListView with all the tags
     * associated with the currently selected Photo
     */
    public void refreshTagsList() {
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        if(selectedPhoto == null) {
            return;
        }
        listViewTags.getSelectionModel().clearSelection();
        listViewTags.getItems().clear();

        //Load items into list
        listViewTags.getItems().addAll(selectedPhoto.getTagStrings());

        listViewTags.getSelectionModel().select(0);
        if(listViewTags.getItems().isEmpty()){
            buttonRemoveTag.setVisible(false);
            buttonRemoveTag.setDisable(true);
        } else{
            buttonRemoveTag.setVisible(true);
            buttonRemoveTag.setDisable(false);
        }
        
    }

    /**
     * Method that refreshes the list of albums displayed in its corresponding ListView
     */
    public void refreshAlbumsList() {
        ArrayList<Album> usersAlbums = Photos.getInstance().getCurrentUser().getAlbumList();
        listViewAlbums.getSelectionModel().clearSelection();
        listViewAlbums.getItems().clear();

        //Load items into list
        listViewAlbums.getItems().addAll(usersAlbums);

        listViewAlbums.getSelectionModel().select(0);
        if(listViewAlbums.getItems().isEmpty()) {
            buttonMovePhoto.setVisible(false);
            buttonMovePhoto.setDisable(true);
            buttonCopyPhoto.setVisible(false);
            buttonCopyPhoto.setDisable(true);
        } else{
            buttonMovePhoto.setVisible(true);
            buttonMovePhoto.setDisable(false);
            buttonCopyPhoto.setVisible(true);
            buttonCopyPhoto.setDisable(false);
        }
        
    }

    
    /**
     * Implemented method for instantiating a new Photo into the <code>ListView</code> of type Photo
     * and the collection it represents
     * 
     * @param fieldKey      the String that will identify and instantiate the new Photo
     * @return Photo        The newly instantiated Photo
     */
    @Override
    public Photo newEntry(String fieldKey){
        return new Photo(fieldKey);
    }

    
    /**
     * Implemented method for retrieving the collection that represents the controller's <code>ListView</code> of type Photo
     * 
     * @return ArrayList of type Photo  the collection associated with the controller's <code>ListView</code> of type Photo
     */
    @Override
    public ArrayList<Photo> getCollection(){
        return Photos.getInstance().getSearchResults().getPhotos();
    }

    
    /**
     * Implemented method that removes an photo from the collection that represents the controller's <code>ListView</code> of type Photo
     * 
     * @param t     the Photo to be deleted
     */
    @Override
    public void removeEntry(Photo t){
        this.getCollection().remove(t);
    }

    
    /**
     * Overridden method that determines if an entry is valid enough to be inserted into the the controller's collection
     * 
     * @param t     the entry in question
     * @return      boolean  the argument's validity for insertion purposes
     */
    @Override
    public boolean isGoodEntry(Photo t){
        try{
            String extension = "";
            String absolutePath = FileSystems.getDefault().getPath(t.getPath()).normalize().toAbsolutePath().toString();
            InputStream inputStream = new FileInputStream(absolutePath);
            
            int index = t.getPath().lastIndexOf('.');
            if (index > 0) {
                extension = t.getPath().substring(index + 1);
            }
            if(!extension.equals("bmp") && !extension.equals("gif") && !extension.equals("jpg")
                && !extension.equals("jpeg") && !extension.equals("png")){
                    inputStream.close();
                    return false;
                }
            if(isRepeatEntry(t)) {
                inputStream.close();
                return false;
            }
            //BMP, GIF, JPEG, PNG
            inputStream.close();
        } catch(Exception ex){
            return false;
        }
        return true;
    }

    
    /**
     * Method that is called when the searchresultsopen page and its controller are initialized
     * Used to assign default settings and values to certain controls
     * 
     * @param arg0      The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param arg1      The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<Photo> photoObservableList = FXCollections.observableArrayList(this.getCollection());
        listView.setItems(photoObservableList);
        listView.setCellFactory(param -> new ImageCell());
        listView.getSelectionModel().select(0);
        refreshDate();
        refreshCaption();
        refreshImageView();
        refreshTagsList();
    }
    /**
     * Custom ListCell that displays both the image associated with a given Photo
     * as well as its String representation
     * 
     * @author Robert Kulesa
     * @author Aaron Kan 
     */
    private class ImageCell extends ListCell<Photo> {
        
        /**
         * ImageView that will display the appropriate image in the ListCell
         */
        final ImageView imageView = new ImageView();

        /**
         * An overridden method that is called when a listcell is updated
         * 
         * @param item      The photo associated with the cell
         * @param empty     Boolean that indicates if a cell is empty
         */
        @Override
        protected void updateItem(Photo item, boolean empty){
            
            try{
                super.updateItem(item, empty);
                if(empty || item == null){
                    imageView.setImage(null);
                    setGraphic(null);
                    setText(null);
                } else {    
                    String absolutePath = FileSystems.getDefault().getPath(item.getPath()).normalize().toAbsolutePath().toString();
                    InputStream inputStream = new FileInputStream(absolutePath);
    
    
                    Image img = new Image(inputStream);
                    imageView.setFitHeight(50.0);
                    imageView.setFitWidth(50.0);
                    imageView.setImage(img);
                    setText(item.toString());
                    setGraphic(imageView);
                    inputStream.close();
                }
            } catch(IOException e){
                errorDialog(e.getMessage());
                return;
            }
            
        }
    }
    
}
