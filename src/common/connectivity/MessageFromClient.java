package common.connectivity;

import java.io.Serializable;

public enum MessageFromClient implements Serializable {
    REQUEST_DISCONNECT_CLIENT,
    REQUEST_UPDATE_USER,
    REQUEST_TABLE_ORDER,
    REQUEST_DELETE_USER,
    REQUEST_TABLE_USER,
    REQUEST_ADD_USER,
    REQUEST_LOGOUT,
    REQUEST_LOGIN,


}
