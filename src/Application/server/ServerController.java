package Application.server;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the Application.server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class ServerController extends AbstractServer {
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  private mysqlController sqlcontroller;
  private LoginController loginController;
  private HashMap<ConnectionToClient, String> loggedInClients;
  private ConnectionToClient client;

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo Application.server.
   *
   * @param port The port number to connect on.
   */
  public ServerController(int port) {
      super(port);
      loggedInClients = new HashMap<>();
  }

  
  //Instance methods ************************************************
  
  /**
   * This method is responsible for the creation of
   * the Application.server instance (there is no UI in this phase).
   *
   //* @param args[0] The port number to listen on.  Defaults to 5555
   *          if no argument is entered.
   */
  public boolean run(int arg, String IP, String username, String password) {
    int port = 0; //Port to listen on

    try {
        port = arg; //Get port from command line
    }
    catch(Throwable t) {
        port = DEFAULT_PORT; //Set port to 5555
    }

    ServerController sv = new ServerController(port);
    sqlcontroller = mysqlController.getSQLInstance(IP, username, password);
    loginController = new LoginController();

    try {
        sv.listen(); //Start listening for connections
    }
    catch (Exception ex) {
        System.out.println("ERROR - Could not listen for clients!");
        return false;
    }
    return true;
  }

  protected void closeConnection(){
      sqlcontroller.disconnect();
  }


    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        this.client = client;
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        super.clientDisconnected(client);
        if (this.client.equals(client)){
            System.out.println("disconnected bish");
        }
    }

    /**
   * This method overrides the one in the superclass.  Called
   * when the Application.server starts listening for connections.
   */

  protected void serverStarted() {
      System.out.println("Server listening for connections on port " + getPort());
  }
  
  //Class methods ***************************************************
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the Application.server stops listening for connections.
   */
  protected void serverStopped() {
    System.out.println("Server has stopped listening for connections.");
  }

    /**
     * This method handles any messages received from the client.
     *
     * @param msg The message received from the client.
     * @param client The connection from which the message originated.
     */
    // TODO: add check if arguments are null or the index doesnt exist
    // TODO: add client array or map so the server could communicate back with the correct client

    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client);

        String message = (String)msg;
        String[] queryArgs = message.split(" ");

        switch (queryArgs[0]){
            case "newUser":
                if(sqlcontroller.checkUserExists(queryArgs[2], queryArgs[3]).equals("")){
                    sendMessageToClient(client, "Failed to add user, user already in database.");
                    return;
                }
                if(sqlcontroller.addUser(queryArgs[1], queryArgs[2], queryArgs[3],queryArgs[4],queryArgs[5],queryArgs[6],queryArgs[7])){
                    sendMessageToClient(client, "user added successfully.");
                    break;
                }
                sendMessageToClient(client, "failed to add user to database.");
                break;

            case "deleteUser":
                if (sqlcontroller.deleteUser(queryArgs[1], queryArgs[2], queryArgs[3])){
                    sendMessageToClient(client, "User deleted successfully.");
                    break;
                }
                sendMessageToClient(client, "Error deleting user.");
                break;

            case "login":
                String UID = loginController.authenticate(queryArgs[1], queryArgs[2]);
                if(!UID.equals("")){
                    addLoggedClient(client, UID);
                    sendMessageToClient(client, "true");
                    break;
                }
                sendMessageToClient(client, "false");
                break;

            case "updateUser":
                sqlcontroller.checkUserExists(queryArgs[1], queryArgs[2]);
                break;

            case "?":
                sendMessageToClient(client, "newUser ID username password name lastname phonenumber email\n" +
                                                    "login username password\n" +
                                                    "deleteUser ID username password");
                break;

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















