package photos.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.GregorianCalendar;
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
    private TextField fieldNewCaption;

    @FXML
    private Button buttonConfirmNewCaption;

    @FXML
    private Button buttonCancelNewCaption;

    @FXML
    private ListView<?> listViewTags;

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
    private ListView<?> listViewAlbums;

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
    void buttonCancelNewCaptionClicked(MouseEvent event) {

    }

    @FXML
    void buttonCancelNewTagClicked(MouseEvent event) {

    }

    @FXML
    void buttonConfirmNewCaptionClicked(MouseEvent event) {

    }

    @FXML
    void buttonConfirmNewTagClicked(MouseEvent event) {

    }

    @FXML
    void buttonCopyPhotoClicked(MouseEvent event) {

    }


    @FXML
    void buttonMovePhotoClicked(MouseEvent event) {

    }

    @FXML
    void buttonOpenSlideshowClicked(MouseEvent event){
        if(Photos.getInstance().getCurrentAlbum().getNumPhotos() <= 0){
            errorDialog("Cannot show slideshow of album with zero photos");
            return;
        }
        Photos.getInstance().goToSlideShow();
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


    public void refreshImageView(){
        try{
            Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
            String absolutePath = FileSystems.getDefault().getPath(selectedPhoto.getPath()).normalize().toAbsolutePath().toString();
            System.out.println(absolutePath);
            InputStream inputStream = new FileInputStream(absolutePath);
            Image img = new Image(inputStream);
            imageView.setImage(img);
            inputStream.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        
    }

    public void refreshCaption(){
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        labelCaption.setText(selectedPhoto.getCaption());
    }

    public void refreshDate(){
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        fmt.setCalendar(selectedPhoto.getLastModified());
        String formattedDate = fmt.format(selectedPhoto.getLastModified().getTime());
        labelDate.setText(formattedDate);
    }

    @FXML
    void photosListViewSelected(MouseEvent event) {
        Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();
        refreshImageView();
        refreshCaption();
        refreshDate();
        //refreshTagsList();        
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
        refreshList();
        
        listView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
                if(!allowSelect) event.consume();
			}
		});
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
        listView.getSelectionModel().select(0);
        refreshDate();
        refreshCaption();
        refreshImageView();
        //refreshTagList();
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
