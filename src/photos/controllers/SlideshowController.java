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

    }

    @FXML
    void buttonNextClicked(MouseEvent event) {

    }

    @FXML
    void buttonPrevClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        buttonPrev.setDisable(true);

        if(Photos.getInstance().getCurrentAlbum().getNumPhotos() == 1){
            buttonNext.setVisible(false);
            buttonNext.setDisable(true);
        }
    }
}
