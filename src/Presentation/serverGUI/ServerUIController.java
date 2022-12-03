package Presentation.serverGUI;

import Application.server.ServerUI;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import Application.Common.ConnectionToClient;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import java.util.ResourceBundle;
import javafx.stage.StageStyle;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import Data.UserConnection;
import java.net.URL;

public class ServerUIController extends Application implements Initializable {
    public static ObservableList<UserConnection> observableUserConnections = FXCollections.observableArrayList();
    @FXML // fx:id="defaultButton"
    public Button disconnectButton; // Value injected by FXMLLoader
    @FXML
    public Button connectButton;
    @FXML
    private TableView<UserConnection> connectionList;
    @FXML
    private TextArea console;
    @FXML // fx:id="dbNameField"
    private TextField dbNameField; // Value injected by FXMLLoader
    @FXML // fx:id="ipField"
    private TextField ipField; // Value injected by FXMLLoader
    @FXML // fx:id="passwordField"
    private TextField passwordField; // Value injected by FXMLLoader
    @FXML // fx:id="usernameField"
    private TextField usernameField; // Value injected by FXMLLoader
    @FXML // fx:id="quitButton"
    private Button quitButton; // Value injected by FXMLLoader
    @FXML // fx:id="defaultButton"
    private Button defaultButton; // Value injected by FXMLLoader
    @FXML
    private TableColumn<UserConnection, String> ipColumn;
    @FXML
    private TableColumn<UserConnection, String> hostNameColumn;
    @FXML
    private TableColumn<UserConnection, String> connectionStatusColumn;
    private double xoffset;
    private double yoffset;

    @FXML
    private void insertDefaultValues(ActionEvent event) {
        ipField.setText("localhost");
        usernameField.setText("root");
        passwordField.setText("Aa123456");
        dbNameField.setText("ekrutdatabase");
    }
    @FXML
    private void connectServer(){
        if(ipField.getText().equals("") ||  usernameField.getText().equals("") || passwordField.getText().equals("") || dbNameField.getText().equals("")){
            Alert a = new Alert(Alert.AlertType.WARNING, "All fields need to be filled in order to log int to server.");
            a.setTitle("Connection Error");
            a.show();
            return;
        }
        String consoleOut = ServerUI.startServer(this.ipField.getText(), this.dbNameField.getText(), this.usernameField.getText(), this.passwordField.getText());
        consolePrint(consoleOut + "\n");
        this.defaultButton.setDisable(true);
        this.connectButton.setDisable(true);
        this.disconnectButton.setDisable(false);
        this.dbNameField.setDisable(true);
        this.ipField.setDisable(true);
        this.usernameField.setDisable(true);
        this.passwordField.setDisable(true);
    }

    @FXML
    private void disconnectServer(){
        ServerUI.shutdownServer();
        consolePrint("Server has stopped listening for connections");
        defaultButton.setDisable(false);
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
        observableUserConnections.clear();

        this.defaultButton.setDisable(false);
        this.connectButton.setDisable(false);
        this.disconnectButton.setDisable(true);
        this.dbNameField.setDisable(false);
        this.ipField.setDisable(false);
        this.usernameField.setDisable(false);
        this.passwordField.setDisable(false);
    }

    @FXML
    private void quitApp(ActionEvent event) {
        if(!disconnectButton.isDisabled())
            ServerUI.shutdownServer();
        consolePrint("Server has stopped listening for connections");
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize tableView to display connections
        this.ipColumn.setCellValueFactory(new PropertyValueFactory<>("ClientIP"));
        this.hostNameColumn.setCellValueFactory(new PropertyValueFactory<>("HostName"));
        this.connectionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("ConnectionStatus"));
        this.connectionList.setItems(observableUserConnections);
        this.disconnectButton.setDisable(true);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // constructing our scene
            Parent root = FXMLLoader.<Parent>load(getClass().getResource("/Presentation/serverGUI/ServerUI.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());

            // event handler for when the mouse is pressed on the scene to trigger the drag and move event
            root.setOnMousePressed(event -> {
                xoffset = event.getSceneX();
                yoffset = event.getSceneY();
            });

            // event handler for when the mouse is pressed AND dragged to move the window
            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX()-xoffset);
                primaryStage.setY(event.getScreenY()-yoffset);
            });

            // setting the stage
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene( scene );
            primaryStage.setTitle( "EKRut Server" );
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addClientConnection(ConnectionToClient client){
        UserConnection userConnectionData;
        userConnectionData = new UserConnection(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName() ,"Connected");
        observableUserConnections.add(userConnectionData);
    }

    public void removeClientConnection(ConnectionToClient client){
        UserConnection userConnectionData;
        for(UserConnection conn : observableUserConnections){
            if (client.getInetAddress().getHostAddress().equals(conn.getClientIP())){
                observableUserConnections.remove(conn);
                return;
            }
        }
    }

    private void consolePrint(String msg){
        String txt = console.getText();
        console.setText(txt + msg);
    }
}







//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            // constructing our scene
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Presentation/serverGUI/ServerUI.fxml"));
//            Pane pane = loader.load();
//
//            // event handler for when the mouse is pressed on the scene to trigger the drag and move event
//            pane.setOnMousePressed(event -> {
//                xoffset = event.getSceneX();
//                yoffset = event.getSceneY();
//            });
//
//            // event handler for when the mouse is pressed AND dragged to move the window
//            pane.setOnMouseDragged(event -> {
//                primaryStage.setX(event.getScreenX()-xoffset);
//                primaryStage.setY(event.getScreenY()-yoffset);
//            });
//            Scene scene = new Scene( pane );
//            scene.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());
//
//            // setting the stage
//            primaryStage.initStyle(StageStyle.UNDECORATED);
//            primaryStage.setScene( scene );
//            primaryStage.setTitle( "EKRut Server" );
//            primaryStage.setResizable(false);
//            primaryStage.show();
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }











//    @FXML
//    private void connectServer(){
//        if(ipField.getText().equals("") || portField.getText().equals("") ||  usernameField.getText().equals("") || passwordField.getText().equals("") || dbNameField.getText().equals("")){
//            Alert a = new Alert(Alert.AlertType.WARNING, "All fields need to be filled in order to log int to server.");
//            a.setTitle("Connection Error");
//            a.show();
//            return;
//        }
//        // serverController = ServerController.getServerInstance(Integer.parseInt(portField.getText()), ipField.getText(), usernameField.getText(), passwordField.getText());   // new ServerController(Integer.parseInt(portField.getText()));
//        serverController = ServerController.getServerInstance(Integer.parseInt(portField.getText()));
//        serverController.setUI(this);
//        if(serverController.run(Integer.parseInt(portField.getText()))){
//            // disable default and connect button
//            defaultButton.setDisable(true);
//            connectButton.setDisable(true);
//            disconnectButton.setDisable(false);
//            return;
//        }
//        Alert a = new Alert(Alert.AlertType.ERROR, "Could not listen for clients!");
//        a.setTitle("Server Error");
//        a.show();
//    }