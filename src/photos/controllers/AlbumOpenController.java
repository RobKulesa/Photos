package photos.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import photos.app.Photos;
import photos.structures.Photo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AlbumOpenController extends ListController<Photo> implements Initializable{
    
    @FXML
    private Button buttonLogout;

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
        return Photos.getInstance().getCurrentAlbum().getPhotoAlbum();
    }

    public void removeEntry(Photo t){
        this.getCollection().remove(t);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Photo> photoObservableList = FXCollections.observableArrayList(this.getCollection());
        listView.setItems(photoObservableList);
        listView.setCellFactory(new ListCell<Photo>() {
            final ImageView imgView = new ImageView();
            imgView.setFitHeight(200);
            imgView.setFitWidth(200);

            ListCell<Photo> cell = new ListCell<Photo>(){
                @Override
                public void updateItem(Photo p, boolean empty){
                    if(item != null && !empty){
                        imgView.setImage(new Image(p.getPath()));
                    }
                }
            };
            cell.setGraphic(imgView);
            return cell
        });


    }
    

}
