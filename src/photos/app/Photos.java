package photos.app;

import photos.controllers.LoginController;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Photos extends Application {

    Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        mainStage.setTitle("Photo Albums =)");
        
        try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/view/loginpage.fxml"));
			VBox vbox = (VBox)loader.load();
			
            LoginController controller = (LoginController) loader.getController();
            controller.setMainStage(mainStage);

            Scene scene = new Scene(vbox);
            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
