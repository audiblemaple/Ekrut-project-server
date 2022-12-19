package common.connectivity;

import java.io.Serializable;

public enum MessageFromClient implements Serializable {
    REQUEST_ALL_MACHINE_PRODUCTS,   // V
    REQUEST_DISCONNECT_CLIENT,      // TODO
    REQUEST_MACHINE_PRODUCTS,       // V
    REQUEST_ALL_PRODUCTS,           // TODO
    REQUEST_UPDATE_USER,            // TODO
    REQUEST_TABLE_ORDER,            // TODO
    REQUEST_DELETE_USER,            // V
    REQUEST_MACHINE_IDS,             // V
    REQUEST_TABLE_USER,             // TODO
    REQUEST_DISCONNECT,             // V
    REQUEST_ADD_USER,               // TODO
    REQUEST_LOGOUT,                 // V
    REQUEST_LOGIN,                  // V
}
