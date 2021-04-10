package photos.controllers;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import photos.app.Photos;

import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.net.URL;

public class SlideshowController extends Controller implements Initializable{

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

    public void changeImageViewImage(){
        String absolutePath = FileSystems.getDefault().getPath(item.getPath()).normalize().toAbsolutePath().toString();
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
