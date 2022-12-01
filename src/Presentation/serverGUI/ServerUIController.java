package Presentation.serverGUI;

import Application.Common.ConnectionToClient;
import Application.server.ServerController;
import Data.Connection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.stage.StageStyle;

import javax.tools.Tool;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ServerUIController extends Application implements Initializable {
    @FXML // fx:id="defaultButton"
    public Button disconnectButton; // Value injected by FXMLLoader
    @FXML
    public Button connectButton;
    @FXML
    protected TableView<Connection> connectionList;
    private ArrayList clientList;
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
    @FXML
    private TableColumn<Connection, String> ipColumn;
    @FXML
    private TableColumn<Connection, String> hostNameColumn;
    @FXML
    private TableColumn<Connection, String> connectionStatusColumn;
    private ObservableList<Connection> observableConnections = FXCollections.observableArrayList();
    private double xoffset;
    private double yoffset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize tableView to display connections
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ClientIP"));
        hostNameColumn.setCellValueFactory(new PropertyValueFactory<>("HostName"));
        connectionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("ConnectionStatus"));
        connectionList.setItems(observableConnections);
        disconnectButton.setDisable(true);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // constructing our scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Presentation/serverGUI/ServerUI.fxml"));
            Pane pane = loader.load();
            pane.setOnMousePressed(event -> {
                xoffset = event.getSceneX();
                yoffset = event.getSceneY();
            });

            pane.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX()-xoffset);
                primaryStage.setY(event.getScreenY()-yoffset);
            });
            Scene scene = new Scene( pane );
            scene.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());

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

    public void refreshList(ArrayList<ConnectionToClient> clientList){
        connectionList.getItems().removeAll();
        Connection connectionData = new Connection("", "" ,"");
        for(ConnectionToClient conn : clientList){
            // TODO: REDO this part the to string ios not very optimal
            if (conn.toString().equals("null"))
                continue;

            connectionData.setClientIP(conn.getInetAddress().getHostAddress());
            connectionData.setHostName(conn.getInetAddress().getHostName());
            connectionData.setConnectionStatus("Connected");

            connectionList.getItems().add(connectionData);
        }
    }

    // TODO: get database name
    @FXML
    private void insertDefaultValues(ActionEvent event) {
        ipField.setText("localhost");
        portField.setText("5555");
        usernameField.setText("root");
        passwordField.setText("Aa123456");
        dbNameField.setText("Ekrut");
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
        serverController.setUI(this);
        if(serverController.run(Integer.parseInt(portField.getText()), ipField.getText(), usernameField.getText(), passwordField.getText())){
            // disable default and connect button
            defaultButton.setDisable(true);
            connectButton.setDisable(true);
            disconnectButton.setDisable(false);
            return;
        }
        Alert a = new Alert(Alert.AlertType.ERROR, "Could not listen for clients!");
        a.setTitle("Server Error");
        a.show();
    }

    @FXML
    private void disconnectServer(){
        serverController.disconnectFromDB();
        defaultButton.setDisable(false);
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
    }

    @FXML
    private void quitApp(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

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
}