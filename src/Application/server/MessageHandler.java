package Application.server;

import OCSF.ConnectionToClient;
import Presentation.serverGUI.ServerUIController;
import common.connectivity.User;
import common.connectivity.Message;
import common.connectivity.MessageFromServer;
import common.orders.Product;


import java.io.IOException;
import java.util.ArrayList;

public class MessageHandler {
    private static MysqlController mysqlcontroller = MysqlController.getSQLInstance();
    private static ArrayList<String> userLogInCredentials;
    private static User userData;
    public static void handleMessage(Object clientMessage, ConnectionToClient client){
        Message message = (Message) clientMessage;
        ArrayList<Product> productList = null;
        switch(message.getTask().name()){ // TODO: add disconnect message to set client connection status to disconnected
            case "REQUEST_LOGIN":
                if(message == null) {
                    sendMessageToClient(client, new Message(null, MessageFromServer.LOGIN_ERROR));
                    break;
                }
                userLogInCredentials = (ArrayList<String>)message.getData();
                if (mysqlcontroller.isLoggedIn(userLogInCredentials)){
                    sendMessageToClient(client, new Message(null, MessageFromServer.LOGIN_FAILED_ALREADY_LOGGED_IN));
                    break;
                }
                userData = mysqlcontroller.logUserIn(userLogInCredentials);
                if (userData == null){
                    sendMessageToClient(client, new Message(null, MessageFromServer.LOG_IN_ERROR_USER_DOES_NOT_EXIST));
                    break;
                }
                sendMessageToClient(client ,new Message(userData, MessageFromServer.LOGIN_SUCCESSFUL));
                break;

            case "REQUEST_LOGOUT":
                if(message == null){
                    sendMessageToClient(client, new Message(null, MessageFromServer.LOGOUT_ERROR));
                    break;
                }
                if (mysqlcontroller.logUserOut((ArrayList<String>)message.getData())){
                    sendMessageToClient(client, new Message(null, MessageFromServer.LOGOUT_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message(null, MessageFromServer.LOGOUT_FAILED_NOT_LOGGED_IN));
                break;

            case "REQUEST_MACHINE_PRODUCTS":
                if(message == null){
                    sendMessageToClient(client, new Message(null, MessageFromServer.ERROR_GETTING_MACHINE_PRODUCTS));
                    break;
                }
                productList = mysqlcontroller.getAllProductsForMachine((String)message.getData());
                if(productList != null){
                    sendMessageToClient(client, new Message(productList, MessageFromServer.IMPORT_MACHINE_PRODUCTS_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message(productList, MessageFromServer.ERROR_IMPORTING_MACHINE_PRODUCTS));
                break;

            case "REQUEST_ALL_MACHINE_PRODUCTS":
                if(message == null){
                    sendMessageToClient(client, new Message(null, MessageFromServer.ERROR_GETTING_MACHINE_PRODUCTS));
                    break;
                }
                productList = mysqlcontroller.getAllProductsForAllMachines();
                if(productList != null){
                    sendMessageToClient(client, new Message(productList, MessageFromServer.IMPORT_MACHINE_PRODUCTS_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message(productList, MessageFromServer.ERROR_IMPORTING_MACHINE_PRODUCTS));
                break;

            case "REQUEST_ADD_USER":

                if(message == null){
                    sendMessageToClient(client, new Message(null, MessageFromServer.ERROR_ADDING_USER));
                    break;
                }
                String result = mysqlcontroller.dataExists((User) message.getData());
                if(!result.equals("")){
                    sendMessageToClient(client, new Message(result, MessageFromServer.ERROR_ADDING_USER_EXISTS));
                    break;
                }
                if(mysqlcontroller.addUser((User) message.getData())){
                    sendMessageToClient(client, new Message(null, MessageFromServer.USER_ADDED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message(null, MessageFromServer.ERROR_ADDING_USER));
                break;

            case "REQUEST_DELETE_USER":
                if(message == null){
                    sendMessageToClient(client, new Message(null, MessageFromServer.ERROR_DELETING_USER));
                    break;
                }
                if(mysqlcontroller.deleteUser((String)message.getData())){
                    sendMessageToClient(client, new Message(null, MessageFromServer.DELETE_USER_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message(null, MessageFromServer.ERROR_DELETING_USER));
                break;




            default:
                sendMessageToClient(client, new Message(null, MessageFromServer.UNKNOWN_TASK));
        }
    }


    private static void sendMessageToClient(ConnectionToClient client, Message message){
        try {
            client.sendToClient(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
