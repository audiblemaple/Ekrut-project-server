package Application.server;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * @author Lior Jigalo
 * This class is the main class of the server
 */
public class ServerUI extends Application {
    private static ServerController serverController;
    private static MysqlController mysqlController;

    /**
     * @param args
     * This is the main method.
     */
    public static void main(String[] args) {
        launch(args);
    }

    // starting the server
    /**
     * @param IP
     * @param name
     * @param username
     * @param password
     * @return responses from database connection initialization
     */
    public static String startServer(String IP, String name, String username, String password){
        String serverResponse = "";
        String dataBaseResponse = "";
        mysqlController = MysqlController.getSQLInstance();
        mysqlController.setDataBaseIP(IP);
        mysqlController.setDataBaseName(name);
        mysqlController.setDataBaseUsername(username);
        mysqlController.setDataBasePassword(password);
        dataBaseResponse = mysqlController.connectDataBase();

        if(dataBaseResponse.contains("SQL connection succeed")){
            try{
                serverController.listen();
                serverResponse += "Connected to server.";
                serverResponse += "\nListening for conections on port " + serverController.getPort();
            }catch (IOException exception){
                serverResponse += "\nError, failed to start server";
                exception.printStackTrace();
            }
        }
        return dataBaseResponse + serverResponse;
    }

    // shutting down the server

    /**
     *This method closes connection to the database
     */
    public static void shutdownServer() {
        try{
            serverController.close();

        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    // starting the whole application

    /**
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     * This method creates a new object of type ServerController which initializes the database connection and loads the serverUIController class.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        serverController = new ServerController(5555, primaryStage);
    }
}















