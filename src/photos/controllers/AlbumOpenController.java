package photos.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import photos.app.Photos;
import photos.structures.Photo;
import javafx.scene.image.Image;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
public class AlbumOpenController extends ListController<Photo>  implements Initializable{
    
    @FXML
    private Button buttonLogout;

    @FXML
    private Button buttonBack;

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

    @FXML
    void buttonBackClicked(MouseEvent event){
        if(paneConfirmCreate.isVisible()) {
            errorDialog("Please save pending changes before exiting.");
            return;
        }
        writeUsers();
        Photos.getInstance().goToAlbumList();
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


    }

    public Photo newEntry(String fieldKey){
        return new Photo(fieldKey);
    }

    public ArrayList<Photo> getCollection(){
        return Photos.getInstance().getCurrentAlbum().getPhotos();
    }

    public void removeEntry(Photo t){
        this.getCollection().remove(t);
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
                    System.out.println("*******TESTING THE URL********");
    
                    String absolutePath = FileSystems.getDefault().getPath(item.getPath()).normalize().toAbsolutePath().toString();
                    System.out.println(absolutePath);
                    InputStream inputStream = new FileInputStream(absolutePath);
    
    
                    Image img = new Image(inputStream);
                    imageView.setFitHeight(50.0);
                    imageView.setFitWidth(50.0);
                    imageView.setImage(img);
                    setText(item.toString());
                    setGraphic(imageView);
                }
            } catch(IOException e){
                e.printStackTrace();
                System.out.println("Bad filepath lololz");
                return;
            }
            
        }
    }
    
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
                    return false;
                }
            if(isRepeatEntry(t))
                return false;
            //BMP, GIF, JPEG, PNG
        } catch(Exception ex){
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Photo> photoObservableList = FXCollections.observableArrayList(this.getCollection());
        listView.setItems(photoObservableList);
        listView.setCellFactory(param -> new ImageCell());
    }
    

}
