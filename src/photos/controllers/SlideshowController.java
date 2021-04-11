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
    int albumSize;

    @FXML
    private Button buttonNext;

    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonPrev;

    @FXML
    private ImageView imageviewCurrentImage;

    
    /** 
     * @param event
     */
    @FXML
    void menuItemQuitClicked(ActionEvent event){
        writeUsersAndQuit(event);
    }

    
    /** 
     * @param stage
     */
    @FXML
    @Override
    public void setMainStage(Stage stage) {
        mainStage = stage;
        mainStage.setOnCloseRequest(event -> {writeUsersAndQuit(event);});
        
    }

    
    /** 
     * @param event
     */
    @FXML
    void buttonBackClicked(MouseEvent event) {
        Photos.getInstance().goToAlbumOpen();
    }

    
    /** 
     * @param event
     */
    @FXML
    void buttonNextClicked(MouseEvent event) {
        currentIndex+=1;
        if(currentIndex == albumSize -1){
            buttonNext.setVisible(false);
            buttonNext.setDisable(true);
        }
        if(currentIndex!= 0){
            buttonPrev.setVisible(true);
            buttonPrev.setDisable(false);
        }
        try{
            changeImageViewImage();;
        } catch(Exception ex){
            ex.printStackTrace();
        }
        
    }

    
    /** 
     * @param event
     */
    @FXML
    void buttonPrevClicked(MouseEvent event) {
        currentIndex -=1;
        if(currentIndex == 0){
            buttonPrev.setVisible(false);
            buttonPrev.setDisable(true);
        }
        if(currentIndex!= albumSize-1){
            buttonNext.setVisible(true);
            buttonNext.setDisable(false);
        }
        try{
            changeImageViewImage();;
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    
    /** 
     * @throws FileNotFoundException
     */
    public void changeImageViewImage() throws FileNotFoundException {
        try{
            Photo currentPhoto = Photos.getInstance().getCurrentAlbum().getPhotos().get(currentIndex);
            String absolutePath = FileSystems.getDefault().getPath(currentPhoto.getPath()).normalize().toAbsolutePath().toString();
            System.out.println(absolutePath);
            InputStream inputStream = new FileInputStream(absolutePath);
            Image img = new Image(inputStream);
            imageviewCurrentImage.setImage(img);
            inputStream.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        
    }

    
    /** 
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        currentIndex = 0;
        albumSize = Photos.getInstance().getCurrentAlbum().getNumPhotos();
        buttonPrev.setDisable(true);
        buttonPrev.setVisible(false);
        try{
            changeImageViewImage();;
        } catch(Exception ex){
            ex.printStackTrace();
        }

        if(albumSize == 1){
            buttonNext.setVisible(false);
            buttonNext.setDisable(true);
        }
    }
}
