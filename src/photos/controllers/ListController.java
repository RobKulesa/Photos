package photos.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

import photos.structures.User;
/**
 * Abstract controller to be extended by concrete controllers that need
 * creation, deletion, and display functionality for a collection of items
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public abstract class ListController<T> extends Controller {

    /**
     * Static boolean for allowing items on the ListView listUsers to be selected.
     */
    protected boolean allowSelect = true;

    /**
     * FXML ListView for the generic type list.
     */
    @FXML
    protected ListView<T> listView;

    /**
     * FXML button to create a new user.
     */
    @FXML
    protected Button buttonCreate;

    /**
     * FXML pane for the group of items used to create a new user.
     */
    @FXML
    protected Pane paneConfirmCreate;

    /**
     * FXML TextField to input the username of the new user.
     */
    @FXML
    protected TextField fieldNewEntry;

    /**
     * FXML label that is associated with the fieldNewEntry TextField 
     */
    @FXML
    protected Label labelEntryField;

    /**
     * FXML button to confirm creation of new user.
     */
    @FXML
    protected Button buttonConfirm;

    /**
     * FXML button to cancel creation of new user.
     */
    @FXML
    protected Button buttonCancel;

    /**
     * FXML label for alerting the user of an invalid username.
     */
    @FXML
    protected Label labelInvalidAddition;

    /**
     * FXML button to delete the selected user.
     */
    @FXML
    protected Button buttonDelete;

    
    /** 
     * Event handler when the cancel-designated button is clicked
     * 
     * @param event     an event that fires when button is clicked
     */
    @FXML
    void buttonCancelClicked(MouseEvent event) {
        labelInvalidAddition.setVisible(false);
        allowSelect = true;
        paneConfirmCreate.setVisible(false);
        fieldNewEntry.setEditable(false);
    }

    
    /** 
     * Event handler when the create-designated button is clicked
     * 
     * @param event     an event that fires when button is clicked
     */
    @FXML
    void buttonCreateClicked(MouseEvent event){
        paneConfirmCreate.setVisible(true);
        fieldNewEntry.setEditable(true);
        fieldNewEntry.setText("");
        allowSelect = false;
    }

    
    /** 
     * Event handler when the confirm-designated button is clicked
     * 
     * @param event     an event that fires when button is clicked
     */
    @FXML 
    void buttonConfirmClicked(MouseEvent event){
        String inputEntryName = fieldNewEntry.getText();
        T createdEntry = newEntry(inputEntryName);
        if(inputEntryName.isBlank()){
            labelInvalidAddition.setVisible(true);
            return;
        }

        if(!isGoodEntry(createdEntry)){
            labelInvalidAddition.setVisible(true);
            return;
        }
        
        getCollection().add(createdEntry);

        
        refreshList(createdEntry);
        listView.getSelectionModel().select(createdEntry);
        allowSelect = true;

        paneConfirmCreate.setVisible(false);
        fieldNewEntry.setEditable(false);
        labelInvalidAddition.setVisible(false);
        writeUsers();
    }

    
    /** 
     * Event handler when the delete-designated button is clicked
     * 
     * @param event     an event that fires when button is clicked
     */
    @FXML
    void buttonDeleteClicked(MouseEvent event){
        T selectedEntry = listView.getSelectionModel().getSelectedItem();
        if(selectedEntry == null){
            if(listView.getItems().size()<1){
                errorDialog("No users left to remove!");
                return;
            } else {
                errorDialog("Please select an item to remove first.");
                return;
            }
        } else if(selectedEntry instanceof User) {
            User selectedUser = (User) selectedEntry;
            if(selectedUser.getUsername().equalsIgnoreCase("admin")) {
                errorDialog("Cannot remove admin.");
                return;
            } else if(selectedUser.getUsername().equalsIgnoreCase("stock")) {
                errorDialog("Cannot remove user stock.");
                return;
            }
        }

        removeEntry(selectedEntry);
        refreshList(null);
        writeUsers();
    }

    
    /** 
     * Method that reloads the ListView and the controls associated with it
     * as is appropriate given the conditions of the current view
     * 
     * @param t     The item in the collection that the ListView will have selected upon being refreshed
     */
    public void refreshList(T t) {
        listView.getSelectionModel().clearSelection();
        listView.getItems().clear();

        //Load items into list
        if(getCollection().size() > 0)
            listView.getItems().addAll(getCollection());

        if(t == null) listView.getSelectionModel().select(0);
        else listView.getSelectionModel().select(t);

        if(listView.getItems().isEmpty()){
            buttonDelete.setVisible(false);
            buttonDelete.setDisable(true);
        } else{
            buttonDelete.setVisible(true);
            buttonDelete.setDisable(false);
        }
        
    }

    
    /** 
     * Method that detects of an attempted new Entry is already
     * an element in the current <code>ListView</code> of type T.
     * 
     * @param t             The item to be analyzed by the algorithm
     * @return boolean      determines if the item is a repeat
     */
    public boolean isRepeatEntry(T t){
        for(T tInList : getCollection()){
            if(tInList.equals(t)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * An abstract method for instantiating a new entry into the <code>ListView</code> of type T.
     * and the collection it represents
     * 
     * @param fieldKey      the String that will identify and instantiate the new entry
     * @return T            the newly instantiated entry
     */
    public abstract T newEntry(String fieldKey);
    
    /**
     * An abstract method for retrieving the collection that represents the controller's <code>ListView</code> of type T.
     * 
     * @return ArrayList  the collection associated with the controller's <code>ListView</code> type T.
     */
    public abstract ArrayList<T> getCollection();
    
    /**
     * An abstract method that removes an entry from the collection that represents the controller's <code>ListView</code> of type T.
     * 
     * @param t     the entry to be deleted
     */
    public abstract void removeEntry(T t);
    

    /**
     * An abstract method that determines if an entry is valid enough to be inserted into the the controller's collection.
     * 
     * @param entry     the entry in question
     * @return boolean  the argument's validity for insertion purposes
     */
    public abstract boolean isGoodEntry(T entry);
    
} 
