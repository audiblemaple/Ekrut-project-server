package Application.server;

import Presentation.serverGUI.ServerUIController;
import javafx.application.Application;
import javafx.stage.Stage;


public class ServerUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        ServerUIController serverUI = new ServerUIController();
        serverUI.start(primaryStage);
    }
}
