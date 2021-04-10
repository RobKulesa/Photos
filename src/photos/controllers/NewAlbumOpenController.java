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
import javafx.scene.control.menuItem;
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

public class NewAlbumOpenController {

    @FXML
    private MenuItem menuItemQuit;

    @FXML
    private ListView<?> listViewPhotos;

    @FXML
    private Pane paneConfirmCreateNewPhoto;

    @FXML
    private TextField fieldNewPhoto;

    @FXML
    private Button buttonConfirmNewPhoto;

    @FXML
    private Button buttonCancelNewPhoto;

    @FXML
    private Label labelInvalidAddition;

    @FXML
    private Label labelEntryField;

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
    private Button buttonAddPhoto;

    @FXML
    private Button buttonDeletePhoto;

    @FXML
    private Button buttonOpenSlideshow;

    @FXML
    private Button buttonLogout;

    @FXML
    private Button buttonBack;

    @FXML
    void buttonAddEditCaptionClicked(MouseEvent event) {

    }

    @FXML
    void buttonAddPhotoClicked(MouseEvent event) {

    }

    @FXML
    void buttonBackClicked(MouseEvent event) {

    }

    @FXML
    void buttonCancelNewCaptionClicked(MouseEvent event) {

    }

    @FXML
    void buttonCancelNewPhotoClicked(MouseEvent event) {

    }

    @FXML
    void buttonCancelNewTagClicked(MouseEvent event) {

    }

    @FXML
    void buttonConfirmNewCaptionClicked(MouseEvent event) {

    }

    @FXML
    void buttonConfirmNewPhotoClicked(MouseEvent event) {

    }

    @FXML
    void buttonConfirmNewTagClicked(MouseEvent event) {

    }

    @FXML
    void buttonCopyPhotoClicked(MouseEvent event) {

    }

    @FXML
    void buttonDeletePhotoClicked(MouseEvent event) {

    }

    @FXML
    void buttonLogoutClicked(MouseEvent event) {

    }

    @FXML
    void buttonMovePhotoClicked(MouseEvent event) {

    }

    @FXML
    void buttonOpenSlideshowClicked(MouseEvent event) {

    }

    @FXML
    void menuItemQuitClicked(ActionEvent event) {

    }

    @FXML
    void photosListViewSelected(MouseEvent event) {

    }

}