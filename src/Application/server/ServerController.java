package Application.server;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import Application.Common.AbstractServer;
import Application.Common.ConnectionToClient;
import Presentation.serverGUI.ServerUIController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ServerController extends AbstractServer {
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  private static ServerController serverController = null;
  private static ServerUIController serverUI;
  private MysqlController sqlcontroller;
  private HashMap<ConnectionToClient, String> loggedInClients;
  private ArrayList<ConnectionToClient>  clients;

  //Constructors ****************************************************

  private ServerController(int port, String IP, String username, String password) {
      super(port);
      loggedInClients = new HashMap<>();
      sqlcontroller = MysqlController.getSQLInstance(IP, username, password);
      clients = new ArrayList<>();
  }

  public static ServerController getServerInstance(int port, String IP, String username, String password){
      if (serverController == null)
          return new ServerController(port, IP, username, password);
      return serverController;
  }

  public void setUI(ServerUIController ui){
      serverUI = ui;
  }

    public boolean run(int arg, String IP, String username, String password) {
      int port = 0; //Port to listen on

      try {
          port = arg; //Get port from command line
      }
      catch(Throwable t) {
          port = DEFAULT_PORT; //Set port to 5555
      }

      serverController = getServerInstance(port, IP, username, password);

      try {
          serverController.listen(); //Start listening for connections
      }
      catch (Exception ex) {
          System.out.println("ERROR - Could not listen for clients!");
          return false;
      }
      return true;
    }

    public void closeConnection(){
      sqlcontroller.disconnect();
    }

    @Override
    public void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        if(!this.clients.contains(client)){
            this.clients.add(client);
            serverUI.refreshList(this.clients);
        }
    }

    // TODO: check if this works after the guys implement the closeConnection on client side
    // TODO: check why it was synchronized
    @Override
    public void clientDisconnected(ConnectionToClient client) {
        super.clientDisconnected(client);
        this.clients.remove(client);
        System.out.println("Client: " + client + " disconnected.");
//        if (this.client.equals(client)){
//            System.out.println("disconnected bish");
//        }
    }

    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }

    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client);

        String message = (String)msg;
        String[] queryArgs = message.split(" ");


        switch (queryArgs[0]){
            case "newUser":
                if(sqlcontroller.checkUserExists(queryArgs[3])){
                    sendMessageToClient(client, "exists");
                    return;
                }
                if(sqlcontroller.addUser(queryArgs[1], queryArgs[2], queryArgs[3],queryArgs[4],queryArgs[5],queryArgs[6])){
                    sendMessageToClient(client, "true");
                    return;
                }
                sendMessageToClient(client, "bad");
                return;

            case "deleteUser":
                if (sqlcontroller.deleteUser(queryArgs[1], queryArgs[2], queryArgs[3])){
                    sendMessageToClient(client, "true");
                    return;
                }
                sendMessageToClient(client, "Error deleting user.");
                return;

                // Non-functional for now
            case "login":
                if(sqlcontroller.checkUserExists(queryArgs[1])){
                    sendMessageToClient(client, sqlcontroller.getAllDB());
                    return;
                }
                sendMessageToClient(client, "false");
                return;

            case "updateUser":
                if(sqlcontroller.checkUserExists(queryArgs[1])){
                    sqlcontroller.updateUser(queryArgs[1], queryArgs[2], queryArgs[3]);
                    sendMessageToClient(client,"true");
                    return;
                }
                sendMessageToClient(client,"none");
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

    // getter for arrayList of clients
    public ArrayList getclientList(){
        return this.clients;
    }

    // get number of currently connected clients.
    public int getNumOfClients(){
        return this.getNumberOfClients();
    }

    // add a new connected client to client map
    protected void addLoggedClient(ConnectionToClient client, String ID){
        loggedInClients.put(client, ID);
    }

    protected String getDatabaseName(){
        return sqlcontroller.getname();
    }
}




//End of EchoServer class





// newUser ID username password name lastname phonenumber email
// checkuserexists ID username password
// deleteUser ID username password







// spare

//  public static void main(String[] args) {
//    int port = 0; //Port to listen on
//
//    try {
//        port = Integer.parseInt(args[0]); //Get port from command line
//    }
//    catch(Throwable t) {
//        port = DEFAULT_PORT; //Set port to 5555
//    }
//
//    ServerController sv = new ServerController(port);
//
//
//    try {
//        sv.listen(); //Start listening for connections
//    }
//    catch (Exception ex) {
//        System.out.println("ERROR - Could not listen for clients!");
//    }
//  }















