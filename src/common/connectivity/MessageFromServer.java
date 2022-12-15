package common.connectivity;

import java.io.Serializable;

public enum MessageFromServer implements Serializable {
    ERROR_IMPORTING_ALL_MACHINE_PRODUCTS,   // V
    IMPORT_MACHINE_PRODUCTS_SUCCESSFUL,     // V
    ERROR_IMPORTING_MACHINE_PRODUCTS,       // V
    IMPORT_ORDER_TABLE_UNSUCCESSFUL,        // TODO
    LOGIN_FAILED_ALREADY_LOGGED_IN,         // V
    ERROR_GETTING_MACHINE_PRODUCTS,         // V
    IMPORT_USER_TABLE_SUCCESSFUL,           // TODO
    LOGOUT_FAILED_NOT_LOGGED_IN,            // V
    IMPORT_ORDER_TABLE_COMPLETE,            // TODO
    DELETE_USER_UNSUCCESSFUL,               // TODO
    IMPORT_USER_UNSUCCESSFUL,               // TODO
    ERROR_ADDING_USER_EXISTS,               // V
    USER_ADDED_SUCCESSFULLY,                // V
    ERROR_ADDING_USER,                      // V
    LOGOUT_SUCCESSFUL,                      // V
    LOGIN_SUCCESSFUL,                       // V
    LOGOUT_ERROR,                           // V
    LOGIN_ERROR,                            // V
    UNKNOWN_TASK                            // V
}