package Application.server;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class ServerUI extends Application {
    private static ServerController serverController;
    private static MysqlController mysqlController;
    public static void main(String[] args) {
        launch(args);
    }

    // starting the server
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
    public static void shutdownServer() {
        try{
            serverController.close();

        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    // starting the whole application
    @Override
    public void start(Stage primaryStage) throws Exception{
        serverController = new ServerController(5555, primaryStage);
    }
}















