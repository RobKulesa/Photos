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
import photos.app.Photos;
import photos.structures.Album;
import photos.structures.Photo;
/**
 * Controller for the part of the normal-user subsystem
 * that allows the user to search for photos using certain criteria
 * @author Robert Kulesa
 * @author Aaron Kan  
 */
public class PhotosSearchController extends Controller {

    /**
     * FXML RadioButton used to adjust the date range search criteria
     */
    @FXML
    private RadioButton radioButtonDateRange;

    /**
     * FXML RadioButton used to adjust the Tag value search criteria 
     */
    @FXML
    private RadioButton radioButtonTagValues;

    /**
     * FXML Pane that holds the functionality for picking date-range search criteria
     */
    @FXML
    private Pane paneDatePickers;

    /**
     * FXML DatePicker that allows user to pick the lower bound for the date-range search criteria
     */
    @FXML

        private DatePicker datePickerFrom;

    /**
     * FXML DatePicker that allows user to pick the upper bound for the date-range search criteria
     */
    @FXML
    private DatePicker datePickerTo;

    /**
     * FXML Pane that holds the functionality for picking the primary Tag search criteria
     */
    @FXML
    private Pane paneTagValues;

    /**
     * FXML Pane that holds the functionality for picking the secondary Tag search criteria
     */
    @FXML
    private Pane paneSecondaryTag;

    /**
     * FXML TextField that allows the user to input the Tag Name 
     * for the secondary Tag search criteria
     */
    @FXML
    private TextField fieldTagName2;

    /**
     * FXML TextField that allows the user to input the Tag Value 
     * for the secondary Tag search criteria
     */
    @FXML
    private TextField fieldTagValue2;

    /**
     * FXML ComboBox that determines how you want to combine
     * the primary and secondry Tags as search criteria
     */
    @FXML
    private ComboBox<String> comboBoxCombination;

    /**
     * FXML TextField that allows the user to input the Tag Name 
     * for the primary Tag search criteria
     */
    @FXML
    private TextField fieldTagName1;

    /**
     * FXML TextField that allows the user to input the Tag Value 
     * for the secondary Tag search criteria
     */
    @FXML
    private TextField fieldTagValue1;

    /**
     * FXML Button that confirms the user-provided search criteria and
     * executes the search
     */
    @FXML
    private Button buttonConfirmSearch;

    /**
     * FXML Button that cancels the search the application-user may have 
     * wanted to execute
     */
    @FXML
    private Button buttonCancelSearch;

    
    /** 
     * FXML Event handler that cancels the search the user is formatting
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Cancel Search button being clicked
     */
    @FXML
    void buttonCancelSearchClicked(MouseEvent event) {
        Photos.getInstance().goToAlbumList();
    }

    
    /** 
     * FXML Event handler that that confirms and executes the search the user has formatted
     * upon the appropriate event firing
     * 
     * @param event     Event that represents the Confirm Search button being clicked
     */
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
            gFrom = new GregorianCalendar(tFrom.getYear(), tFrom.getMonthValue()-1, tFrom.getDayOfMonth(), 0, 0, 0);
            gTo = new GregorianCalendar(tTo.getYear(), tTo.getMonthValue()-1, tTo.getDayOfMonth(), 23, 59, 59);
        }
        
        for(Album album : currentUserAlbums) {
            for(Photo photo : album.getPhotos()) {
                if(Photos.getInstance().getSearchResults().containsPhoto(photo)) break;
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
        if(Photos.getInstance().getSearchResults().getNumPhotos() <= 0) {
            errorDialog("No results found, please try again!");
            return;
        }
        Photos.getInstance().goToSearchResults();
    }

    
    /** 
     * FXML Event handler that quits out of the application upon the appropriate MenuItem being clicked 
     * 
     * @param event     Event that represents the Quit MenuItem being clicked
     */
    @Override
    @FXML
    void menuItemQuitClicked(ActionEvent event) {
        writeUsersAndQuit(event);
    }

    
    /** 
     * FXML Event handler that adjusts the date range search criteria 
     * upon the appropriate RadioButton being adjusted 
     * 
     * @param event     Event that represents the Date Range Selected RadiButton being adjusted
     */
    @FXML
    void radioButtonDateRangeSelected(MouseEvent event) {
        radioButtonTagValues.setSelected(false);
        paneDatePickers.setVisible(true);
        paneTagValues.setVisible(false);
    }

    
    /** 
     * FXML Event handler that adjusts the Tag values search criteria 
     * upon the appropriate RadioButton being adjusted 
     * 
     * @param event     Event that represents the Tag Values Selected RadiButton being adjusted
     */
    @FXML
    void radioButtonTagValuesSelected(MouseEvent event) {
        radioButtonDateRange.setSelected(false);
        paneDatePickers.setVisible(false);
        paneTagValues.setVisible(true);

    }

    
    /** 
     * FXML Event handler that adjusts the visibility of the pane
     * associated with the secondary Tag search criteria
     * upon the appropriate event being fired. 
     * 
     * @param event     The event that represents a combobox item being selected
     */
    @FXML
    void comboBoxCombinationSelected(ActionEvent event) {
        String combinationSelected = comboBoxCombination.getSelectionModel().getSelectedItem();
        if(combinationSelected.equals("none")) {
            paneSecondaryTag.setVisible(false);
        } else {
            paneSecondaryTag.setVisible(true);
        }
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
            writeUsersAndQuit(event);
        });
        comboBoxCombination.getItems().add("none");
        comboBoxCombination.getItems().add("and");
        comboBoxCombination.getItems().add("or");
        comboBoxCombination.getSelectionModel().select("none");
    }

}
