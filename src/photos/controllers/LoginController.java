package photos.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photos.Debug;
import photos.app.Photos;
import photos.structures.User;

/**
 * Controller for the login subsystem.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class LoginController extends Controller {

    /**
     * FXML TextField for entering the username to log in with.
     */
    @FXML
    private TextField fieldUsername;

    /**
     * FXML button for logging in.
     */
    @FXML
    private Button buttonLogin;

    /**
     * FXML label to show to the user when logging in fails.
     */
    @FXML
    private Text textLoginFailed;

    /**
     * FXML method for logging in when the button is clicked.
     * Sends the user to the correct subsystem after a successful login.
     * 
     * @param event
     */
    @FXML
    void loginButtonClicked(MouseEvent event) {
        //1. Parse through the users.txt 
        
        readUsers();

        //2. Check to see if the field is contained within users.txt
        String loginAttempt = fieldUsername.getText();
        boolean goodLogin = false;
        for(int i = 0; i < Photos.getInstance().getUserList().getLength(); ++i) {
            User user = Photos.getInstance().getUserList().getUser(i);
            goodLogin = user.getUsername().equals(loginAttempt);
            if(goodLogin) {
                textLoginFailed.setVisible(false);
                if(Debug.debugControllers) System.out.println("LoginController User logged in as: \"" + user.toString() + "\"");

                Photos.getInstance().setCurrentUser(user);
                if(loginAttempt.equals("admin")){
                    Photos.getInstance().goToAdminPage();
                    break;
                } else {
                    //Navigate to normal user page
                    Photos.getInstance().goToAlbumList();
                    break;
                }
            }
        }
        if(!goodLogin){
            if(Debug.debugControllers) System.out.println("LoginController Attempt to login as \"" + loginAttempt + "\" failed");
            textLoginFailed.setVisible(true);
        } 
    }

    /**
     * FXML method for quitting the application when the button is clicked.
     * 
     * @param event
     */
    @Override
    @FXML
    public void menuItemQuitClicked(ActionEvent event) {
        writeUsersAndQuit(event);
    }

    /**
     * Implemented method for setting the main stage for this controller.
     * 
     * @param stage    The stage to be set as main stage for this controller.
     */
    @Override
    public void setMainStage(Stage stage) {
        mainStage = stage;
        mainStage.setOnCloseRequest(event -> {writeUsersAndQuit(event);});
    }   

}
