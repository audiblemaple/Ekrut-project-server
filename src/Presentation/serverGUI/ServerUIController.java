package Presentation.serverGUI;

import javafx.scene.control.cell.PropertyValueFactory;
import Application.Common.ConnectionToClient;
import Application.server.ServerController;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import java.util.ResourceBundle;
import javafx.stage.StageStyle;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import Data.Connection;
import java.net.URL;

public class ServerUIController extends Application implements Initializable {
    @FXML // fx:id="defaultButton"
    public Button disconnectButton; // Value injected by FXMLLoader
    @FXML
    public Button connectButton;
    @FXML
    protected TableView<Connection> connectionList;
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
    private Text connectedClientsNum;
    @FXML
    private TableColumn<Connection, String> ipColumn;
    @FXML
    private TableColumn<Connection, String> hostNameColumn;
    @FXML
    private TableColumn<Connection, String> connectionStatusColumn;

    private ArrayList clientList;
    private ObservableList<Connection> observableConnections = FXCollections.observableArrayList();
    private double xoffset;
    private double yoffset;


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
        connectedClientsNum.setText("Connected Clients: " + clientList.size());
    }

    @FXML
    private void quitApp(ActionEvent event) {
        if(!disconnectButton.isDisabled())
            serverController.disconnectFromDB();

        Platform.exit();
        System.exit(0);
    }

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

            // event handler for when the mouse is pressed on the scene to trigger the drag and move event
            pane.setOnMousePressed(event -> {
                xoffset = event.getSceneX();
                yoffset = event.getSceneY();
            });

            // event handler for when the mouse is pressed AND dragged to move the window
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

    public void addClientConnection(ConnectionToClient client){
        Connection connectionData;
        connectionData  = new Connection(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName() ,"Connected");
        connectionList.getItems().add(connectionData);
        setConnectedNum("Connected Clients: " + observableConnections.size());
    }

    public void removeClientConnection(ConnectionToClient client){
        Connection connectionData;
        for(Connection conn : observableConnections){
            if (client.getInetAddress().getHostAddress().equals(conn.getClientIP())){
                observableConnections.remove(conn);
                setConnectedNum("Connected Clients: " + observableConnections.size());
                return;
            }
        }
    }

    private void setConnectedNum(String str){
        connectedClientsNum.setText(str);
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