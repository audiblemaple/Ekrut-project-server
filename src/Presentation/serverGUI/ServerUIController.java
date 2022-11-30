package Presentation.serverGUI;

import Application.server.ServerController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Application;


public class ServerUIController extends Application{
    @FXML // fx:id="defaultButton"
    public Button disconnectButton; // Value injected by FXMLLoader
    @FXML
    public Button connectButton;
    @FXML
    protected TableView<?> connectionList;
    private ServerController serverController;
    @FXML // fx:id="dbNameField"
    private TextField dbNameField; // Value injected by FXMLLoader
    @FXML // fx:id="ipField"
    private TextField ipField; // Value injected by FXMLLoader
    @FXML // fx:id="passwordField"
    private TextField passwordField; // Value injected by FXMLLoader
    @FXML // fx:id="portField"
    private TextField portField; // Value injected by FXMLLoader
    @FXML // fx:id="usernameField"
    private TextField usernameField; // Value injected by FXMLLoader
    @FXML // fx:id="quitButton"
    private Button quitButton; // Value injected by FXMLLoader
    @FXML // fx:id="defaultButton"
    private Button defaultButton; // Value injected by FXMLLoader

    public String getIpField() {
        return ipField.getText();
    }

    public String getPasswordField() {
        return passwordField.getText();
    }

    public String getPortField() {
        return portField.getText();
    }

    public String getUsernameField() {
        return usernameField.getText();
    }

    public TextField getDbNameField() {
        return dbNameField;
    }


    @Override
    public void start(Stage primaryStage) {
        try {

            // constructing our scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Presentation/serverGUI/ServerUI.fxml"));
            Pane pane = loader.load();
            Scene scene = new Scene( pane );
            scene.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());
            // setting the stage
            primaryStage.setScene( scene );
            primaryStage.setTitle( "EKRut Server" );
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void insertDefaultValues(ActionEvent event) {
        ipField.setText("localhost");
        portField.setText("5555");
        usernameField.setText("root");
        passwordField.setText("Aa123456");
        dbNameField.setText("Ekrut");
    }

    public void refreshList(){
    }

    @FXML
    private void connectServer(){
        if(ipField.getText().equals("") || portField.getText().equals("") ||  usernameField.getText().equals("") || passwordField.getText().equals("") || dbNameField.getText().equals("")){
            Alert a = new Alert(Alert.AlertType.WARNING, "All fields need to be filled in order to log int to server.");
            a.setTitle("Connection Error");
            a.show();
            return;
        }
        serverController = ServerController.getServerInstance(Integer.parseInt(portField.getText()), ipField.getText(), usernameField.getText(), passwordField.getText());   // new ServerController(Integer.parseInt(portField.getText()));
        if(serverController.run(Integer.parseInt(portField.getText()), ipField.getText(), usernameField.getText(), passwordField.getText())){
            defaultButton.setDisable(true);
            return;
        }
        Alert a = new Alert(Alert.AlertType.ERROR, "Could not listen for clients!");
        a.setTitle("Server Error");
        a.show();
    }

    @FXML
    private void disconnectServer(){
        serverController.stopListening();
        serverController.closeConnection();
        defaultButton.setDisable(false);
    }

    @FXML
    private void quitApp(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}




// spare

//    public static  void runServer(String port){
//        int portint = Integer.parseInt(port);
//        serverController = new ServerController(portint);
//        serverController.run(portint);
//    }


//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            // constructing our scene
//            //URL url = getClass().getResource("C:/methodologies project/prototype/prototype/src/Presentation/ServerUI.fxml");
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/Presentation/ServerUI.fxml"));
//            Pane pane = FXMLLoader.load( url );
//            Scene scene = new Scene( pane );
//            // setting the stage
//            primaryStage.setScene( scene );
//            primaryStage.setTitle( "EKRut Server" );
//            primaryStage.show();
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }