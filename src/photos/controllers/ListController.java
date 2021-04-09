package photos.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

import photos.Debug;
import photos.structures.User;

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
        T createdEntry = newEntry(inputEntryName);
        if(inputEntryName.isBlank()){
            labelInvalidAddition.setVisible(true);
            return;
        }

        for(T tInList : getCollection()){
            if(tInList.equals(createdEntry)){
                labelInvalidAddition.setVisible(true);
                return;
            }
        }

        getCollection().add(createdEntry);
        refreshList();
        listView.getSelectionModel().select(createdEntry);
        allowSelect = true;

        paneConfirmCreate.setVisible(false);
        fieldNewEntry.setEditable(false);
        labelInvalidAddition.setVisible(false);
    }

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
        refreshList();
    }

    public void refreshList() {
        listView.getSelectionModel().clearSelection();
        listView.getItems().clear();

        //Load items into list
        listView.getItems().addAll(getCollection());

        if(Debug.debugControllers) System.out.println("ListController Got Generic List: " + listView.getItems());

        listView.getSelectionModel().select(0);
        if(listView.getItems().isEmpty()){
            buttonDelete.setVisible(false);
            buttonDelete.setDisable(true);
        } else{
            buttonDelete.setVisible(true);
            buttonDelete.setDisable(false);
        }
        
    }
    
    public abstract T newEntry(String fieldKey);
    public abstract ArrayList<T> getCollection();
    public abstract void removeEntry(T t);
}
