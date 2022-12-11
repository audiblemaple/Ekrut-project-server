package Application.server;

import OCSF.ConnectionToClient;
import common.connectivity.Message;

public class MessageHandler {
    MysqlController mysqlcontroller = MysqlController.getSQLInstance();
    public void handleMessage(Object clientMessage, ConnectionToClient client){
        Message message = (Message) clientMessage;
        switch(message.getTask()){
            case LOGIN_REQUEST:

                break;
        }
    }
}
