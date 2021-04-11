package photos.app;
import photos.Debug;
import photos.controllers.Controller;
import photos.structures.User;
import photos.structures.UserList;
import photos.structures.Album;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Photos is the class which starts and runs the application. It knows which user
 * is currently logged in, if any, and provides methods to direct the user to
 * the different pages in the application.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan  
 */
public class Photos extends Application {

    /**
     * Field for the main stage that will be shown to the user.
     */
    Stage mainStage;

    /**
     * Field for the currently logged in user, if any.
     */
    private int currentUserIndex;

    /**
     * Reference to the current {@link UserList}.
     */
    private UserList userList;

    /**
     * Reference to the current album (if any)
     */
    private int currentAlbumIndex;

    /**
     * Static field that holds the reference to the current instance of the application.
     */
    private static Photos instance;

    /**
     * Constructor that sets the instance field to the current instance of the application.
     */
    public Photos() {
        instance = this;
        instance.currentUserIndex = -1;
        instance.currentAlbumIndex = -1;
    }

    /**
     * Method for getting the current instance of the application.
     * 
     * @return Photos    The current instance of the application.
     */
    public static Photos getInstance() {
        return instance;
    }

    /**
     * Method for getting the currently logged in user, if any.
     * 
     * @return User    The currently logged in user.
     */
    public User getCurrentUser() {
        return instance.getUserList().getUser(currentUserIndex);
    }
    
    /**
     * Method for setting the currently logged in user.
     * 
     * @param user    The user to be set as currently logged in.
     */
    public void setCurrentUser(int currentUserIndex) {
        instance.currentUserIndex = currentUserIndex;
    }

    public UserList getUserList() {
        return instance.userList;
    }

    public void setUserList(UserList userList) {
        instance.userList = userList;
    }

    public Album getCurrentAlbum() {
        return instance.getUserList().getUser(instance.currentUserIndex).getAlbum(instance.currentAlbumIndex);
    }

    public void setCurrentAlbum(int currentAlbumIndex) {
        instance.currentAlbumIndex = currentAlbumIndex;
    }

    public void setCurrentAlbum(Album album) {
        for(Album a : instance.getUserList().getUser(instance.currentUserIndex).getAlbumList()) {
            if(a.equals(album)) 
                instance.currentAlbumIndex = instance.getUserList().getUser(instance.currentUserIndex).getAlbumList().indexOf(a);
        }
    }

    /**
     * Implements the start method of superclass Application. Starts the application
     * and directs the user to the login page.
     */
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.setTitle("Photo Albums =)");
        goToLoginPage();
        mainStage.setResizable(false);
        mainStage.show();
    }
    
    /**
     * Method for sending the user to the login page. Can be called with
     * <code>Photos.getInstance().goToLoginPage()</code>
     */
    public void goToLoginPage() {
        try {
            replaceSceneContent("/resources/view/loginpage.fxml");
            //if(Debug.debugPhotos) System.out.println("Photos Sending user (" + instance.getUserList().getUser(currentUserIndex) + ") to login page");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToAlbumList(){
        try{
            
            
            replaceSceneContent("/resources/view/albumlist.fxml");
            if(Debug.debugPhotos) System.out.println("Photos Sending user (" + instance.getUserList().getUser(currentUserIndex) + ") to normal album list");
            
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Method for sending the user to the admin page. Can be called with
     * <code>Photos.getInstance().goToAdminPage()</code>
     */
    public void goToAdminPage() {
        try {
            replaceSceneContent("/resources/view/adminpage.fxml");
            if(Debug.debugPhotos) System.out.println("Photos Sending user (" + instance.getUserList().getUser(currentUserIndex) + ") to admin page");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToAlbumOpen(){
        try {
            replaceSceneContent("/resources/view/albumopen.fxml");
            if(Debug.debugPhotos) System.out.println("Photos Sending user (" + instance.getUserList().getUser(currentUserIndex) + ") to admin page");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToPhotosSearch() {
        try {
            replaceSceneContent("/resources/view/photossearchpage.fxml");
            if(Debug.debugPhotos) System.out.println("Photos Sending user (" + instance.getUserList().getUser(currentUserIndex) + ") to photos search page");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToSlideShow(){
        try{
            replaceSceneContent("/resources/view/slideshow.fxml");
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }


    /**
     * Method for replacing the scene with new content.
     * 
     * @param fxmlLocation    Location of the fxml file to be loaded.
     */
    private void replaceSceneContent(String fxmlLocation) {
        try {

			FXMLLoader loader = new FXMLLoader();
            System.out.println(fxmlLocation);
			loader.setLocation(getClass().getResource(fxmlLocation));
            VBox vbox = (VBox)loader.load();
			
            Controller controller = loader.getController();
            controller.setMainStage(mainStage);

            Scene scene = new Scene(vbox);
            mainStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * Main method to launch the Photos application.
     * 
     * @param args    an array of command-line arguments for the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
