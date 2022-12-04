package Application.server;

import OCSF.AbstractServer;
import OCSF.ConnectionToClient;
import Presentation.serverGUI.ServerUIController;
import javafx.stage.Stage;
import java.io.IOException;

public class ServerController extends AbstractServer {
  //Class fields *************************************************
  public ServerUIController serverUI;
  private MysqlController sqlController;

  //Constructors ****************************************************
  public ServerController(int port, Stage primaryStage) {
      super(port);
      sqlController = MysqlController.getSQLInstance();
      serverUI = new ServerUIController();
      try{
          this.serverUI.start(primaryStage);
          System.out.println("dsd");
      }catch (Exception exception){
          exception.printStackTrace();
      }
  }

  protected void serverStarted() {
      System.out.println("Server listening for connections on port " + getPort());
  }

  protected void serverStopped() {
      try{
          close();
          System.out.println("Server has stopped listening for connections.");

      }catch (IOException exception){
          exception.printStackTrace();
      }
  }

    // listen for client connections, this method is called when a client is connected
    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        this.serverUI.addClientConnection(client);
    }

    // this method
    private void disconnectClient(ConnectionToClient client){
        this.serverUI.removeClientConnection(client);
    }

    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client);

        String message = (String)msg;
        String[] queryArgs = message.split(" ");

        switch (queryArgs[0]){
            case "newUser":
                if(sqlController.checkUserExists(queryArgs[3])){
                    sendMessageToClient(client, "exists");
                    return;
                }
                if(sqlController.addUser(queryArgs[1], queryArgs[2], queryArgs[3],queryArgs[4],queryArgs[5],queryArgs[6])){
                    sendMessageToClient(client, "true");
                    return;
                }
                sendMessageToClient(client, "bad");
                return;

            case "deleteUser":
                if (sqlController.deleteUser(queryArgs[1], queryArgs[2], queryArgs[3])){
                    sendMessageToClient(client, "true");
                    return;
                }
                sendMessageToClient(client, "Error deleting user.");
                return;

            // Non-functional for now
            case "login":
                sendMessageToClient(client, sqlController.getAllDB());
                return;

            case "updateUser":
                if(sqlController.checkUserExists(queryArgs[1])){
                    sqlController.updateUser(queryArgs[1], queryArgs[2], queryArgs[3]);
                    sendMessageToClient(client,"true");
                    return;
                }
                sendMessageToClient(client,"none");
                return;

            case "disconnect":
                sendMessageToClient(client, "disconnected");
                disconnectClient(client);
                return;

            case "?":
                sendMessageToClient(client, "newUser name lastname ID phonenumber email creditcardnumber\n" +
                                                    "update ID creditcardnumber subscribernumber" +
                                                    "login ID\n" +
                                                    "deleteUser ID");
                return;

            default:
                sendMessageToClient(client, "Unknown command.");
        }
        //this.sendToAllClients(msg);
    }
    private void sendMessageToClient(ConnectionToClient client , String message){
        try{
            client.sendToClient(message);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}