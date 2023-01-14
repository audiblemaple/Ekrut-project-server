package Presentation.serverGUI;

import Application.server.ServerUI;
import OCSF.ConnectionToClient;
import common.UserConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import misc.Console;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Lior Jigalo
 *  * this class controls all the server's gui behavior
 */
public class ServerUIController extends Application implements Initializable {
    public static ObservableList<UserConnection> observableUserConnections = FXCollections.observableArrayList();
    @FXML // fx:id="defaultButton"
    public Button disconnectButton; // Value injected by FXMLLoader
    @FXML
    public Button connectButton;
    @FXML
    private TextArea console;
    @FXML
    private Pane pane;
    @FXML
    private TableView<UserConnection> connectionList;
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

    private PrintStream consoleOutput;
    private double xoffset;
    private double yoffset;


    /**
     * This method initializes system output to the console box.
     */
    void initConsole(){
        this.consoleOutput = new PrintStream((OutputStream) new Console(this.console));
        System.setOut(consoleOutput);
        System.setErr(consoleOutput);
    }

    /**
     * @param msg String a message to display in the servers console.
     * This method allows us to print output to the server's console.
     */
    public void consolePrint(String msg){
        this.console.appendText(msg);
    }

    /**
     * @param event ActionEvent fires when clicking on defaultButton.
     * This method inserts default values to the input fields.
     */
    @FXML
    private void insertDefaultValues(ActionEvent event) {
        ipField.setText("localhost");
        usernameField.setText("root");
        passwordField.setText("Aa123456");
        dbNameField.setText("ekrutdatabase");
    }

    /**
     * This method connects the server to the database, for this it needs four key parameters: database username, password, ip and name (schema name).
     * This method also enables disconnectButton, disables all input fields, connectButton and defaultButton.
     */
    @FXML
    void connectServer() {
        if(ipField.getText().equals("") ||  usernameField.getText().equals("") || passwordField.getText().equals("") || dbNameField.getText().equals("")){
            Alert a = new Alert(Alert.AlertType.WARNING, "All fields need to be filled in order to log int to server.");
            a.setTitle("Connection Error");
            a.show();
            return;
        }
        String consoleOut = ServerUI.startServer(this.ipField.getText(), this.dbNameField.getText(), this.usernameField.getText(), this.passwordField.getText());
        consolePrint(consoleOut + "\n");
        if (consoleOut.contains("VendorError")){
            return;
        }
        this.defaultButton.setDisable(true);
        this.connectButton.setDisable(true);
        this.disconnectButton.setDisable(false);
        this.dbNameField.setDisable(true);
        this.ipField.setDisable(true);
        this.usernameField.setDisable(true);
        this.passwordField.setDisable(true);
    }

    /**
     *This method disconnects the server from the database, it is also disables disconnectButton, enables all input fields, connectButton and defaultButton.
     */
    @FXML
    private void disconnectServer(){
        ServerUI.shutdownServer();
        defaultButton.setDisable(false);
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
        observableUserConnections.clear();
        consolePrint("Server has stopped listening for connections.\n");

        ServerUI.disconnect();
        this.defaultButton.setDisable(false);
        this.connectButton.setDisable(false);
        this.disconnectButton.setDisable(true);
        this.dbNameField.setDisable(false);
        this.ipField.setDisable(false);
        this.usernameField.setDisable(false);
        this.passwordField.setDisable(false);
    }

    /**
     * @param event ActionEvent fires when the exit button is clicked
     * This method closes connection with the database and quits the application.
     */
    @FXML
    private void quitApp(ActionEvent event) {
        if(!disconnectButton.isDisabled())
            ServerUI.shutdownServer();
        consolePrint("Server has stopped listening for connections\n");
        Platform.exit();
        System.exit(0);
    }

    /**
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     * This method initializes the tableview and empties server console on screen load
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize tableView to display connections
        initConsole();
        this.ipColumn.setCellValueFactory(new PropertyValueFactory<>("ClientIP"));
        this.hostNameColumn.setCellValueFactory(new PropertyValueFactory<>("HostName"));
        this.connectionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("ConnectionStatus"));
        this.connectionList.setItems(observableUserConnections);
        this.disconnectButton.setDisable(true);
        this.connectionList.setPlaceholder(new Label(""));
    }

    /**
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * This method loads the fxml, starts listening for click and drag events on the main program screen.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // constructing our scene
            Parent root = FXMLLoader.<Parent>load(getClass().getResource("/Presentation/serverGUI/ServerUI.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());

            // event handler for when the mouse is pressed on the scene to later trigger the drag event
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

    /**
     * @param client
     * This method adds a client connection to the list of connected clients.
     */
    public void addClientConnection(ConnectionToClient client){
        UserConnection userConnectionData;
        String ip = "";
        for(UserConnection conn : observableUserConnections){
            ip = conn.getClientIP();
            if (client.getInetAddress().getHostAddress().equals(ip)){
                userConnectionData = new UserConnection(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName() ,"Connected");
                observableUserConnections.remove(conn);
                observableUserConnections.add(userConnectionData);
                return;
            }
        }
        userConnectionData = new UserConnection(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName() ,"Connected");
        observableUserConnections.add(userConnectionData);
    }

    /**
     * @param client
     * * This method removes a client connection from the list.
     */
    public void removeClientConnection(ConnectionToClient client){
        UserConnection userConnectionData;
        String ip = "";
        //UserConnection connection = new UserConnection(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName() ,"disconnected");
        for(UserConnection conn : observableUserConnections){
            ip = conn.getClientIP();
            if (client.getInetAddress().getHostAddress().equals(ip)){
                conn.setConnectionStatus("disconnected");
                observableUserConnections.remove(conn);
                observableUserConnections.add(conn);
            }
        }
    }

    public TextArea getConsole(){
        return this.console;
    }



//    /**
//     * @param client
//     * * This method removes a client connection from the list.
//     */
//    public void removeClientConnection(ConnectionToClient client){
//        UserConnection userConnectionData;
//        String ip = "";
//        UserConnection connection = new UserConnection(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName() ,"disconnected");
//        for(UserConnection conn : observableUserConnections){
//            ip = conn.getClientIP();
//            if (client.getInetAddress().getHostAddress().equals(ip)){
//                observableUserConnections.remove(conn);
//                observableUserConnections.add(connection);
//            }
//        }
//    }
}