package photos.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import photos.Debug;
import photos.app.Photos;
import photos.structures.Album;
import photos.structures.Photo;

public class AlbumOpenController extends ListController<Photo> implements Initializable {

    @FXML
    private MenuItem menuItemQuit;

    @FXML
    private ImageView imageView;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelCaption;

    @FXML
    private Button buttonAddEditCaption;

    @FXML
    private AnchorPane paneAddEditCaption;

    @FXML
    private Button buttonAddTag;

    @FXML
    private Button buttonRemoveTag;

    @FXML
    private TextField fieldNewCaption;

    @FXML
    private Button buttonConfirmNewCaption;

    @FXML
    private Button buttonCancelNewCaption;

    @FXML
    private ListView<String> listViewTags;

    @FXML
    private AnchorPane paneAddTag;

    @FXML
    private TextField fieldTagName;

    @FXML
    private Button buttonConfirmNewTag;

    @FXML
    private Button buttonCancelNewTag;

    @FXML
    private TextField fieldTagValue;

    @FXML
    private ListView<Album> listViewAlbums;

    @FXML
    private AnchorPane paneMoveCopy;

    @FXML
    private Button buttonMovePhoto;

    @FXML
    private Button buttonCopyPhoto;

    @FXML
    private Label labelCopySuccessful;

    @FXML
    private Label labelMoveSuccessful;

    @FXML
    private Button buttonOpenSlideshow;

    @FXML
    private Button buttonBack;

    @FXML
    void buttonAddEditCaptionClicked(MouseEvent event) {
        allowSelect = false;
        paneAddEditCaption.setVisible(true);
        fieldNewCaption.setEditable(true);
        buttonConfirmNewCaption.setDisable(false);
        buttonCancelNewCaption.setDisable(false);
    }

    @FXML
    void buttonCancelNewCaptionClicked(MouseEvent event) {
        allowSelect = true;
        buttonCancelNewCaption.setDisable(true);
        buttonConfirmNewCaption.setDisable(true);
        fieldNewCaption.setEditable(false);
        paneAddEditCaption.setVisible(false);
    }

    @FXML
    void buttonAddTagClicked(MouseEvent event) {
        allowSelect = false;
        paneAddTag.setVisible(true);
        fieldTagName.setEditable(true);
        fieldTagValue.setEditable(true);
        buttonConfirmNewTag.setDisable(false);
        buttonCancelNewTag.setDisable(false);
    }

    @FXML
    void buttonCancelNewTagClicked(MouseEvent event) {
        allowSelect = true;
        buttonCancelNewTag.setDisable(true);
        buttonConfirmNewTag.setDisable(true);
        fieldTagValue.setEditable(false);
        fieldTagName.setEditable(false);
        paneAddTag.setVisible(false);
    }

    @FXML
    void buttonBackClicked(MouseEvent event){
        if(paneConfirmCreate.isVisible() || paneAddEditCaption.isVisible() || paneAddTag.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsers();
        Photos.getInstance().goToAlbumList();
    }

    @FXML
    void buttonConfirmNewCaptionClicked(MouseEvent event) {
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        String newCaption = fieldNewCaption.getText();
        if(newCaption == null){
            newCaption = "";
        }
        
        selectedPhoto.setCaption(newCaption);
        refreshList(selectedPhoto);

        allowSelect = true;

        buttonCancelNewCaption.setDisable(true);
        buttonConfirmNewCaption.setDisable(true);
        fieldNewCaption.setEditable(false);
        paneAddEditCaption.setVisible(false);
    }

    @FXML
    void buttonConfirmNewTagClicked(MouseEvent event) {
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        String newTagName = fieldTagName.getText();
        String newTagVal = fieldTagValue.getText();
        if(newTagName.isEmpty() || newTagVal.isEmpty()){
            errorDialog("New Tag Fields Cannot Be Empty!");
            return;
        }
        
        selectedPhoto.addTag(newTagName, newTagVal);
        refreshList(selectedPhoto);
        refreshTagsList();

        allowSelect = true;

        buttonCancelNewTag.setDisable(true);
        buttonConfirmNewTag.setDisable(true);
        fieldTagValue.setEditable(false);
        fieldTagName.setEditable(false);
        paneAddTag.setVisible(false);
    }

    @FXML
    void buttonCopyPhotoClicked(MouseEvent event) {
        Album selectedAlbum = listViewAlbums.getSelectionModel().getSelectedItem();
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        if(selectedAlbum == null) {
            errorDialog("Please Select an Album First!");
            return;
        }
        if(selectedPhoto == null) {
            errorDialog("Please select a Photo First!");
            return;
        }
        ArrayList<Album> userAlbums = new ArrayList<Album>(listViewAlbums.getItems());
        for(Album a : userAlbums) {
            if(selectedAlbum.equals(a)) {
                errorDialog("Please select an album other than the currently opened one.");
                return;
            }
            for(Photo p : a.getPhotos()) {
                if(selectedPhoto.equals(p)) {
                    errorDialog("This photo is already in this album!");
                    return;
                }
            }
        }
        selectedAlbum.addPhoto(selectedPhoto);
    }


    @FXML
    void buttonMovePhotoClicked(MouseEvent event) {
        Album selectedAlbum = listViewAlbums.getSelectionModel().getSelectedItem();
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        if(selectedAlbum == null) {
            errorDialog("Please Select an Album First!");
            return;
        }
        if(selectedPhoto == null) {
            errorDialog("Please select a Photo First!");
            return;
        }
        ArrayList<Album> userAlbums = new ArrayList<Album>(listViewAlbums.getItems());
        for(Album a : userAlbums) {
            if(selectedAlbum.equals(a)) {
                errorDialog("Please select an album other than the currently opened one.");
                return;
            }
            for(Photo p : a.getPhotos()) {
                if(selectedPhoto.equals(p)) {
                    errorDialog("This photo is already in this album!");
                    return;
                }
            }
        }
        selectedAlbum.addPhoto(selectedPhoto);
        Photos.getInstance().getCurrentAlbum().deletePhoto(selectedPhoto);
    }

    

    @FXML
    void buttonRemoveTagClicked(MouseEvent event) {
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        String[] tag = listViewTags.getSelectionModel().getSelectedItem().split(",", 2);
        String name = tag[0].substring(1);
        String val = tag[1].substring(0, tag[1].length()-2);

        selectedPhoto.removeTag(name, val);
        refreshTagsList();
    }

    @FXML
    void buttonOpenSlideshowClicked(MouseEvent event){
        if(Photos.getInstance().getCurrentAlbum().getNumPhotos() <= 0)
            errorDialog("Cannot show slideshow of album with zero photos");
        errorDialog("to the slideshow we go");
    }

    @Override
    @FXML
    void menuItemQuitClicked(ActionEvent event) {
        if(paneConfirmCreate.isVisible() || paneAddEditCaption.isVisible() || paneAddTag.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsersAndQuit(event);
    }

    @FXML
    void photosListViewSelected(MouseEvent event) {

    }
    
    @Override
    public void setMainStage(Stage stage) {
        mainStage = stage;
        mainStage.setOnCloseRequest(event -> {
            if(paneConfirmCreate.isVisible() || paneAddEditCaption.isVisible() || paneAddTag.isVisible()) {
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

    public void refreshTagsList() {
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        if(selectedPhoto == null) {
            return;
        }
        listViewTags.getSelectionModel().clearSelection();
        listViewTags.getItems().clear();

        //Load items into list
        listViewTags.getItems().addAll(selectedPhoto.getTagStrings());

        if(Debug.debugControllers) System.out.println("AlbumOpenController Got Generic List: " + listViewTags.getItems());

        listViewTags.getSelectionModel().select(0);
        if(listViewTags.getItems().isEmpty()){
            buttonRemoveTag.setVisible(false);
            buttonRemoveTag.setDisable(true);
        } else{
            buttonRemoveTag.setVisible(true);
            buttonRemoveTag.setDisable(false);
        }
        
    }

    public void refreshAlbumsList() {
        ArrayList<Album> usersAlbums = Photos.getInstance().getCurrentUser().getAlbumList();
        listViewAlbums.getSelectionModel().clearSelection();
        listViewAlbums.getItems().clear();

        //Load items into list
        listViewAlbums.getItems().addAll(usersAlbums);

        if(Debug.debugControllers) System.out.println("AlbumOpenController Got Generic List: " + listViewTags.getItems());

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

    @Override
    public Photo newEntry(String fieldKey){
        return new Photo(fieldKey);
    }

    @Override
    public ArrayList<Photo> getCollection(){
        return Photos.getInstance().getCurrentAlbum().getPhotos();
    }

    @Override
    public void removeEntry(Photo t){
        this.getCollection().remove(t);
    }

    @Override
    public boolean isGoodEntry(Photo t){
        try{
            String extension = "";
            String absolutePath = FileSystems.getDefault().getPath(t.getPath()).normalize().toAbsolutePath().toString();
            System.out.println(absolutePath);
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<Photo> photoObservableList = FXCollections.observableArrayList(this.getCollection());
        listView.setItems(photoObservableList);
        listView.setCellFactory(param -> new ImageCell());
    }
    
    private class ImageCell extends ListCell<Photo>{
        final ImageView imageView = new ImageView();
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
                    System.out.println(absolutePath);
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
                e.printStackTrace();
                System.out.println("Bad filepath lololz");
                return;
            }
            
        }
    }
    
}
