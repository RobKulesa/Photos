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

/**
 * SlideshowController is the controller which allows the user to interact with the slideshow view
 * to control the album slideshow.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan  
 */
public class SlideshowController extends Controller implements Initializable {

    /**
     * Current index of photo in album being shown in the slideshow.
     */
    int currentIndex;

    /**
     * Size of album being shown in the slideshow.
     */
    int albumSize;

    /**
     * Button to move to the next image in the slideshow.
     */
    @FXML
    private Button buttonNext;

    /**
     * Button to go back to the album open view.
     */
    @FXML
    private Button buttonBack;

    /**
     * Button to move to the previous image in the slideshow.
     */
    @FXML
    private Button buttonPrev;

    /**
     * ImageView for the current image in the slideshow.
     */
    @FXML
    private ImageView imageviewCurrentImage;

    /** 
     * Write the userlist and cleanly quit the application.
     * 
     * @param event     The event associated with the quit menuItem being clicked
     */
    @FXML
    void menuItemQuitClicked(ActionEvent event){
        writeUsersAndQuit(event);
    }

    
    /**
     * Implemented method for setting the main stage for this controller.
     * 
     * @param stage    The stage to be set as main stage for this controller.
     */
    @FXML
    @Override
    public void setMainStage(Stage stage) {
        mainStage = stage;
        mainStage.setOnCloseRequest(event -> {writeUsersAndQuit(event);});
        
    }

    /** 
     * Method for going back to the album open view when the back button is clicked.
     * 
     * @param event     Event caused by the back button being clicked.
     */
    @FXML
    void buttonBackClicked(MouseEvent event) {
        Photos.getInstance().goToAlbumOpen();
    }

    /** 
     * Method for moving to the next photo in the slideshow.
     * 
     * @param event     Event caused by the next button being clicked.
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
            errorDialog(ex.getMessage());
        }
        
    }

    
    /** 
     * Method for moving to the previous photo in the slideshow.
     * 
     * @param event     Event caused by the previous button being clicked.
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
            errorDialog(ex.getMessage());
        }
    }

    
    /** 
     * Change the image shown in the slideshow.
     * 
     * @throws FileNotFoundException    Thrown if image is not found on filesystem.
     */
    public void changeImageViewImage() throws FileNotFoundException {
        try{
            Photo currentPhoto = Photos.getInstance().getCurrentAlbum().getPhotos().get(currentIndex);
            String absolutePath = FileSystems.getDefault().getPath(currentPhoto.getPath()).normalize().toAbsolutePath().toString();
            InputStream inputStream = new FileInputStream(absolutePath);
            Image img = new Image(inputStream);
            imageviewCurrentImage.setImage(img);
            inputStream.close();
        } catch(Exception ex){
            errorDialog(ex.getMessage());
        }
        
    }

    
    /** 
     * Method that is called when the slideshow page and its controller are initialized
     * Used to assign default settings and values to certain controls
     * 
     * @param location          The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources         The resources used to localize the root object, or null if the root object was not localized.
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
