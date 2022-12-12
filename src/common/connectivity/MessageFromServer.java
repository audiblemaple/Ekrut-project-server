package common.connectivity;

import java.io.Serializable;

public enum MessageFromServer implements Serializable {
    IMPORT_ORDER_TABLE_UNSUCCESSFUL,// TODO
    LOGIN_FAILED_ALREADY_LOGGED_IN, // V
    IMPORT_USER_TABLE_SUCCESSFUL,   // TODO
    LOGOUT_FAILED_NOT_LOGGED_IN,    // V
    IMPORT_ORDER_TABLE_COMPLETE,    // TODO
    DELETE_USER_UNSUCCESSFUL,       // TODO
    IMPORT_USER_UNSUCCESSFUL,       // TODO
    DELETE_USER_SUCCESSFUL,         // TODO
    ADD_USER_UNSUCCESSFUL,          // TODO
    UPDATE_UNSUCCESSFUL,            // TODO
    ADD_USER_SUCCESSFUL,            // TODO
    UPDATE_SUCCESSFUL,              // TODO
    LOGOUT_SUCCESSFUL,              // V
    LOGIN_SUCCESSFUL,               // V
    LOGOUT_ERROR,                   // V
    LOGIN_ERROR,                    // V
    UNKNOWN_TASK                    // V
}