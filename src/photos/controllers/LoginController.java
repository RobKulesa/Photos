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


public class LoginController extends Controller {

    @FXML
    private TextField fieldUsername;

    @FXML
    private Button buttonLogin;

    @FXML
    private Text textLoginFailed;

    @FXML
    void loginButtonClicked(MouseEvent event) {
        //1. Parse through the users.txt 
        
        readUsers();

        //2. Check to see if the field is contained within users.txt
        String loginAttempt = fieldUsername.getText();
        boolean goodLogin = false;
        for(int i = 0; i < userList.getLength(); ++i) {
            User user = userList.getUser(i);
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
                    errorDialog("Yay we are not an admin");
                    break;
                }
            }
        }
        if(!goodLogin){
            if(Debug.debugControllers) System.out.println("LoginController Attempt to login as \"" + loginAttempt + "\" failed");
            textLoginFailed.setVisible(true);
        } 
    }

    @Override
    @FXML
    public void menuItemQuitClicked(ActionEvent event) {
        writeUsersAndQuit(event);
    }

    @Override
    public void setMainStage(Stage stage) {
        mainStage = stage;
        mainStage.setOnCloseRequest(event -> {writeUsersAndQuit(event);});
    }   

}
