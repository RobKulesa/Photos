package photos.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import photos.Debug;
import photos.app.Photos;
import photos.structures.Album;
import photos.structures.Photo;

public class PhotosSearchController extends Controller {

    @FXML
    private RadioButton radioButtonDateRange;

    @FXML
    private RadioButton radioButtonTagValues;

    @FXML
    private Pane paneDatePickers;

    @FXML
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;

    @FXML
    private Pane paneTagValues;

    @FXML
    private Pane paneSecondaryTag;

    @FXML
    private TextField fieldTagName2;

    @FXML
    private TextField fieldTagValue2;

    @FXML
    private ComboBox<String> comboBoxCombination;

    @FXML
    private TextField fieldTagName1;

    @FXML
    private TextField fieldTagValue1;

    @FXML
    private Button buttonConfirmSearch;

    @FXML
    private Button buttonCancelSearch;

    @FXML
    void buttonCancelSearchClicked(MouseEvent event) {
        Photos.getInstance().goToAlbumList();
    }

    @FXML
    void buttonConfirmSearchClicked(MouseEvent event) {
        ArrayList<Album> currentUserAlbums = Photos.getInstance().getCurrentUser().getAlbumList();
        Photos.getInstance().setSearchResults(new Album("temp search results"));
        
        String tagname1 = fieldTagName1.getText();
        String tagvalue1 = fieldTagValue1.getText();
        String tagname2 = fieldTagName2.getText();
        String tagvalue2 = fieldTagValue2.getText();
        LocalDate tFrom = datePickerFrom.getValue();
        LocalDate tTo = datePickerTo.getValue();
        
        GregorianCalendar gFrom = new GregorianCalendar();
        GregorianCalendar gTo = new GregorianCalendar();

        String selectedCombo = comboBoxCombination.getSelectionModel().getSelectedItem();


        if(radioButtonTagValues.isSelected()) {
            if(tagname1 == null || tagname1.isBlank() ) {
                errorDialog("Field Tag Name 1 cannot be blank!");
                return;
            }
            if(tagvalue1 == null || tagvalue1.isBlank() ) {
                errorDialog("Field Tag Value 1 cannot be blank!");
                return;
            }
            if(!selectedCombo.equals("none")) {
                if(tagname2 == null || tagname2.isBlank() ) {
                    errorDialog("Field Tag Name 2 cannot be blank!");
                    return;
                }
                if(tagvalue2 == null || tagvalue2.isBlank() ) {
                    errorDialog("Field Tag Value 2 cannot be blank!");
                    return;
                }
            }
        } else if(radioButtonDateRange.isSelected()) {
            if(tFrom == null ){
                errorDialog("DatePicker From cannot be blank!");
                return;
            }
            if(tTo == null ){
                errorDialog("DatePicker To cannot be blank!");
                return;
            }
            gFrom = new GregorianCalendar(tFrom.getYear(), tFrom.getMonthValue(), tFrom.getDayOfMonth(), 0, 0, 0);
            gTo = new GregorianCalendar(tTo.getYear(), tTo.getMonthValue(), tTo.getDayOfMonth(), 23, 59, 59);
        }
        
        for(Album album : currentUserAlbums) {
            for(Photo photo : album.getPhotos()) {
                if(radioButtonTagValues.isSelected()) {
                    if(selectedCombo.equals("none") && photo.hasTag(tagname1, tagvalue1)) {
                        Photos.getInstance().getSearchResults().addPhoto(photo);
                    } else if(selectedCombo.equals("or") && (photo.hasTag(tagname1, tagvalue1) || photo.hasTag(tagname2, tagvalue2))) {
                        Photos.getInstance().getSearchResults().addPhoto(photo);
                    } else if(selectedCombo.equals("and") && (photo.hasTag(tagname1, tagvalue1) && photo.hasTag(tagname2, tagvalue2))) {
                        Photos.getInstance().getSearchResults().addPhoto(photo);
                    }
                } else if(radioButtonDateRange.isSelected()) {
                    if(photo.isInDateRange(gFrom, gTo)) Photos.getInstance().getSearchResults().addPhoto(photo);
                }
            }
        }

        if(Debug.debugControllers) System.out.println("PhotosSearchController Created Search Results Album with " + Photos.getInstance().getSearchResults().getNumPhotos() + " photos");
        Photos.getInstance().goToSearchResults();
    }

    @Override
    @FXML
    void menuItemQuitClicked(ActionEvent event) {
        writeUsersAndQuit(event);
    }

    @FXML
    void radioButtonDateRangeSelected(MouseEvent event) {
        radioButtonTagValues.setSelected(false);
        paneDatePickers.setVisible(true);
        paneTagValues.setVisible(false);
    }

    @FXML
    void radioButtonTagValuesSelected(MouseEvent event) {
        radioButtonDateRange.setSelected(false);
        paneDatePickers.setVisible(false);
        paneTagValues.setVisible(true);

    }

    @FXML
    void comboBoxCombinationSelected(ActionEvent event) {
        String combinationSelected = comboBoxCombination.getSelectionModel().getSelectedItem();
        if(combinationSelected.equals("none")) {
            paneSecondaryTag.setVisible(false);
        } else {
            paneSecondaryTag.setVisible(true);
        }
    }

    @Override
    public void setMainStage(Stage stage) {
        mainStage = stage;
        mainStage.setOnCloseRequest(event -> {
            writeUsersAndQuit(event);
        });
        comboBoxCombination.getItems().add("none");
        comboBoxCombination.getItems().add("and");
        comboBoxCombination.getItems().add("or");
        comboBoxCombination.getSelectionModel().select("none");
    }

}
