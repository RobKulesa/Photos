package photos.controllers;

import java.io.IOException;
import java.util.ArrayList;

import photos.structures.User;
import photos.structures.UserList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;


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
        try {
            userList = UserList.readUserList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //2. Check to see if the field is contained within users.txt
        System.out.println(userList.toString());
        User user;
        String loginAttempt = fieldUsername.getText();
        boolean goodLogin = false;
        for(int i = 0; i < userList.getLength(); ++i) {
            user = userList.getUser(i);
            goodLogin = user.getUsername().equals(loginAttempt);
            if(goodLogin){
                if(loginAttempt.equals("admin")){
                    //Navigate to admin page
                    errorDialog("Yay we are an admin");
                    break;
                } else {
                    //Navigate to normal user page
                    errorDialog("Yay we are not an admin");
                    break;
                }
            }
        }
        if(!goodLogin){
            textLoginFailed.setVisible(true);
        } 
    }

    @Override
    public void menuItemQuitClicked(ActionEvent event) {
        try {
            UserList.writeUserList(userList);
        } catch (IOException e) { 
            errorDialog("An unexpected error occured. Please try again");
        }
        mainStage.close();
    }

    @Override
    public void setMainStage(Stage stage) {
        mainStage = stage;
    }   

}
