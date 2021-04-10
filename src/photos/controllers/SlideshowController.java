package photos.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import photos.app.Photos;
import photos.structures.Photo;
public class SlideshowController extends Controller implements Initializable {

    int currentIndex;

    @FXML
    private Button buttonNext;

    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonPrev;

    @FXML
    private ImageView imageviewCurrentImage;

    @FXML
    void menuItemQuitClicked(ActionEvent event){
        writeUsersAndQuit(event);
    }

    @FXML
    @Override
    public void setMainStage(Stage stage) {
        // TODO Auto-generated method stub
        mainStage = stage;
        mainStage.setOnCloseRequest(event -> {writeUsersAndQuit(event);});
        
    }

    @FXML
    void buttonBackClicked(MouseEvent event) {
        Photos.getInstance().goToAlbumOpen();
    }

    @FXML
    void buttonNextClicked(MouseEvent event) {
        currentIndex+=1;
    }

    @FXML
    void buttonPrevClicked(MouseEvent event) {
        currentIndex -=1;
    }

    public void changeImageViewImage() throws FileNotFoundException {
        Photo currentPhoto = Photos.getInstance().getCurrentAlbum().getPhotos().get(currentIndex);
        String absolutePath = FileSystems.getDefault().getPath(currentPhoto.getPath()).normalize().toAbsolutePath().toString();
        System.out.println(absolutePath);
        InputStream inputStream = new FileInputStream(absolutePath);
        Image img = new Image(inputStream);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        currentIndex = 0;
        buttonPrev.setDisable(true);

        if(Photos.getInstance().getCurrentAlbum().getNumPhotos() == 1){
            buttonNext.setVisible(false);
            buttonNext.setDisable(true);
        }
    }
}
