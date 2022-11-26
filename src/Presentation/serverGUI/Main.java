package serverGUI;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// constructing our scene
		    URL url = getClass().getResource("ServerGUI.fxml");
		    Pane pane = FXMLLoader.load( url );
		    Scene scene = new Scene( pane );
		    // setting the stage
		    primaryStage.setScene( scene );
		    primaryStage.setTitle( "EKRut Server" );
		    primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
