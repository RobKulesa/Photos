package photos.app;
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
     * Main method to launch the Photos application.
     * 
     * @param args    an array of command-line arguments for the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

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
     * Index of the current {@link Album}.
    */
    private int currentAlbumIndex;

    /**
     * Reference to album resulting from search results
     */
    private Album searchResults;

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
     * Set the search results to an album.
     * 
     * @param album    Album to set the search results to.
     */
    public void setSearchResults(Album album) {
        instance.searchResults = album;
    }

    /**
     * Get the search results album.
     * 
     * @return    Search results album.
     */
    public Album getSearchResults() {
        return instance.searchResults;
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
     * @param currentUserIndex    The index of the user to be set as currently logged in.
     */
    public void setCurrentUser(int currentUserIndex) {
        instance.currentUserIndex = currentUserIndex;
    }


    /**
     * Method for retrieving the serialized list of users
     * 
     * @return UserList     The application's current list of users.
     */
    public UserList getUserList() {
        return instance.userList;
    }


    /**
     * Method for setting the program's list of users.
     * 
     * @param userList      The UserList object to be set as the application's serializable list of users.
     */
    public void setUserList(UserList userList) {
        instance.userList = userList;
    }

    /**
     * Method for getting the album the application is wants to open in the AlbumOpen page
     * Achieved by retrieving the album from the serialized user at a known index
     * 
     * @return Album    The Album the application has opened/will open 
     */
    public Album getCurrentAlbum() {
        return instance.getCurrentUser().getAlbum(instance.currentAlbumIndex);
    }

    /**
     * Changes the album that the program wants to open
     * Accomplished through adjusting the known album index
     * 
     * @param currentAlbumIndex     the new index from which the currentAlbum will be pulled
     */
    public void setCurrentAlbum(int currentAlbumIndex) {
        instance.currentAlbumIndex = currentAlbumIndex;
    }

    /**
     * Set the current album.
     * 
     * @param album The album to be set as currently used.
     */
    public void setCurrentAlbum(Album album) {
        for(Album a : instance.getCurrentUser().getAlbumList()) {
            if(a.equals(album)) 
                instance.currentAlbumIndex = instance.getCurrentUser().getAlbumList().indexOf(a);
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
        } catch (Exception ex) {
            return;
        }
    }

    /**
     * Method for sending the user to the login page. Can be called with
     * <code>Photos.getInstance().goToAlbumList()</code>
     */
    public void goToAlbumList(){
        try{
            replaceSceneContent("/resources/view/albumlist.fxml");
        } catch (Exception ex){
            return;
        }
    }

    /**
     * Method for sending the user to the admin page. Can be called with
     * <code>Photos.getInstance().goToAdminPage()</code>
     */
    public void goToAdminPage() {
        try {
            replaceSceneContent("/resources/view/adminpage.fxml");
        } catch (Exception ex) {
        }
    }

    /**
     * Method for sending the user to the albumopen page. Can be called with
     * <code>Photos.getInstance().goToAlbumOpen()</code> 
     */
    public void goToAlbumOpen(){
        try {
            replaceSceneContent("/resources/view/albumopen.fxml");
        } catch (Exception ex) {
            return;
        }
    }

    /**
     * Method for sending the user to the photossearch. Can be called with
     * <code>Photos.getInstance().goToPhotosSearch()</code> 
     */
    public void goToPhotosSearch() {
        try {
            replaceSceneContent("/resources/view/photossearchpage.fxml");
        } catch (Exception ex) {
            return;
        }
    }

    /**
     * Method for sending the user to the search results page. Can be called with
     * <code>Photos.getInstance().goToSearchResults()</code>
     */
    public void goToSearchResults() {
        try{
            replaceSceneContent("/resources/view/searchresultsopen.fxml");
        } catch(Exception ex){
            return;
        }
    }

    /**
     * Method for sending the user to the slideshow page. Can be called with
     * <code>Photos.getInstance().goToSlideshow()</code>
     */
    public void goToSlideShow(){
        try{
            replaceSceneContent("/resources/view/slideshow.fxml");
        } catch(Exception ex){
            return;
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
			loader.setLocation(getClass().getResource(fxmlLocation));
            VBox vbox = (VBox)loader.load();
			
            Controller controller = loader.getController();
            controller.setMainStage(mainStage);

            Scene scene = new Scene(vbox);
            mainStage.setScene(scene);
		} catch (IOException e) {
			return;
		}
    }
}
