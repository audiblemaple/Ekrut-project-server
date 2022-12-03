package Application.server;
import Application.Common.AbstractServer;
import Application.Common.ConnectionToClient;
import Presentation.serverGUI.ServerUIController;
import java.io.IOException;
import java.util.ArrayList;

public class ServerController extends AbstractServer {
  //Class fields *************************************************

  final public static int DEFAULT_PORT = 5555;
  private static ServerController serverController = null;
  private static ServerUIController serverUI;
  private MysqlController sqlcontroller;
  private ArrayList<ConnectionToClient>  clients;

  //Constructors ****************************************************

  private ServerController(int port, String IP, String username, String password) {
      super(port);
      sqlcontroller = MysqlController.getSQLInstance(IP, username, password);
      clients = new ArrayList<>();
  }

  public static ServerController getServerInstance(int port, String IP, String username, String password){
      if (serverController == null)
          return new ServerController(port, IP, username, password);
      return serverController;
  }

  //Overriden method ***********************************************
    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        this.clients.add(client);
        serverUI.addClientConnection(client);
    }

    private void disconnectClient(ConnectionToClient client){
        this.clients.remove(client);
        serverUI.removeClientConnection(client);
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
                sendMessageToClient(client, sqlcontroller.getAllDB());
                return;

            case "updateUser":
                if(sqlcontroller.checkUserExists(queryArgs[1])){
                    sqlcontroller.updateUser(queryArgs[1], queryArgs[2], queryArgs[3]);
                    sendMessageToClient(client,"true");
                    return;
                }
                sendMessageToClient(client,"none");
                return;

            case "disconnect":
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

    // getter for arrayList of clients
    public ArrayList getclientList(){
        return this.clients;
    }

    // get number of currently connected clients.
    public int getNumOfClients(){
        return this.getNumberOfClients();
    }

    public String getDatabaseName(){
        return sqlcontroller.getname();
    }
    public void setUI(ServerUIController ui){
        serverUI = ui;
    }

    public void disconnectFromDB(){
      try{
          this.close();
      }catch (IOException exception){
          exception.printStackTrace();
      }
    }
}