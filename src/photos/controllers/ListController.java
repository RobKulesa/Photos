package photos.controllers;

//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import photos.app.Photos;
//import photos.structures.User;
import java.util.ArrayList;
import javafx.scene.control.MenuItem;

public abstract class ListController<T> extends Controller{
    private static boolean allowSelect = true;
    
    @FXML
    private ListView<T> listView;

    @FXML
    private Button buttonCreate;

    @FXML
    private Pane paneConfirmCreate;

    @FXML
    private TextField fieldNewEntry;

    @FXML
    private Label labelEntryField;

    @FXML
    private Button buttonConfirm;

    /**
     * FXML button to cancel creation of new user.
     */
    @FXML
    private Button buttonCancel;

    @FXML 
    private Label labelInvalidAddition;

    @FXML 
    private Button buttonDelete;

    @FXML
    private MenuItem menuItemQuit;

    

    /**
     * Abstract method for setting the main stage for this controller.
     * 
     * @param stage    The stage to be set as main stage for this controller.
     */
    public abstract void setMainStage(Stage stage);


    @FXML
    void buttonCancelClicked(MouseEvent event) {
        labelInvalidAddition.setVisible(false);
        allowSelect = true;
        paneConfirmCreate.setVisible(false);
        fieldNewEntry.setEditable(false);
    }

    @FXML
    void buttonCreateClicked(MouseEvent event){
        paneConfirmCreate.setVisible(true);
        fieldNewEntry.setEditable(true);
        fieldNewEntry.setText("");
        allowSelect = false;
    }

    @FXML void buttonConfirmClicked(MouseEvent event){
        String inputEntryName = fieldNewEntry.getText();
        if(inputEntryName.isBlank()){
            labelInvalidAddition.setVisible(true);
            return;
        }

        for(T tInList: getCollection()){
            if(tInList.toString().equalsIgnoreCase(inputEntryName)){
                labelInvalidAddition.setVisible(true);
                return;
            }
        }

        T createdEntry = newEntry(inputEntryName);
        getCollection().add(createdEntry);
        refreshList();
        listView.getSelectionModel().select(createdEntry);
        allowSelect = true;

        paneConfirmCreate.setVisible(false);
        fieldNewEntry.setEditable(false);
    }

    @FXML
    void buttonDeleteClicked(MouseEvent event){
        T selectedEntry = listView.getSelectionModel().getSelectedItem();
        if(selectedEntry == null){
            if(listView.getItems().size()<1){
                errorDialog("No users left to remove!");
                return;
            }else {
                errorDialog("Please select an item to remove first.");
                return;
            }
        } 

        removeEntry(selectedEntry);
        refreshList();
    }

    public void refreshList(){
        listView.getSelectionModel().clearSelection();
        listView.getItems().clear();

        //Load items into list
        listView.getItems().addAll(getCollection());
        listView.getSelectionModel().select(0);
        if(listView.getItems().isEmpty()){
            buttonDelete.setVisible(false);
            buttonDelete.setDisable(true);
        }
        
    }
    
    public abstract T newEntry(String fieldKey);
    public abstract ArrayList<T> getCollection();
    public abstract void removeEntry(T t);
}
