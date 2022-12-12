package Application.server;

import OCSF.ConnectionToClient;
import common.connectivity.User;
import common.connectivity.Message;
import common.connectivity.MessageFromServer;


import java.io.IOException;
import java.util.ArrayList;

public class MessageHandler {
    private static MysqlController mysqlcontroller = MysqlController.getSQLInstance();
    private static ArrayList<String> userLogInCredentials;
    private static User userData;
    public static void handleMessage(Object clientMessage, ConnectionToClient client){
        Message message = (Message) clientMessage;
        switch(message.getTask().name()){
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
                    sendMessageToClient(client, new Message(null, MessageFromServer.LOGIN_ERROR));
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
