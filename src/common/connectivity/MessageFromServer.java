package common.connectivity;

import java.io.Serializable;

/**
 * @author Lior Jigalo
 */
public enum MessageFromServer implements Serializable {
    IMPORT_ORDER_BY_ORDER_ID_AND_CUSTOMER_ID_SUCCESSFUL,    // V
    ERROR_IMPORTING_ALL_MACHINE_PRODUCTS,   // V
    IMPORT_WAREHOUSE_PRODUCTS_SUCCESSFUL,   // V
    IMPORT_MACHINE_LOCATIONS_SUCCESSFUL,    // V
    IMPORT_INVENTORY_REPORT_SUCCESSFUL,     // V
    IMPORT_MACHINE_PRODUCTS_SUCCESSFUL,     // V
    ERROR_IMPORTING_WAREHOUSE_PRODUCTS,     // V
    ERROR_IMPORTING_MACHINE_LOCATIONS,      // V
    LOG_IN_ERROR_USER_DOES_NOT_EXIST,       // V
    ERROR_IMPORTING_MACHINE_PRODUCTS,       // V
    ERROR_IMPORTING_INVENTORY_REPORT,       // V
    IMPORT_ORDER_TABLE_UNSUCCESSFUL,        // TODO
    LOGIN_FAILED_ALREADY_LOGGED_IN,         // V
    ERROR_GETTING_MACHINE_PRODUCTS,         // V
    IMPORT_USER_TABLE_SUCCESSFUL,           // TODO
    IMPORT_MACHINE_ID_SUCCESSFUL,           // V
    ERROR_IMPORTING_MACHINE_IDS,            // V
    LOGOUT_FAILED_NOT_LOGGED_IN,            // V
    IMPORT_ORDER_TABLE_COMPLETE,            // TODO
    ADD_NEW_ORDER_SUCCESSFUL,               // V
    ERROR_ADDING_USER_EXISTS,               // V
    USER_ADDED_SUCCESSFULLY,                // V
    DELETE_USER_SUCCESSFUL,                 // V
    ERROR_ADDING_NEW_ORDER,                 // V
    ERROR_IMPORTING_ORDER,                  // V
    ERROR_DELETING_USER,                    // V
    ERROR_ADDING_USER,                      // V
    LOGOUT_SUCCESSFUL,                      // V
    LOGIN_SUCCESSFUL,                       // V
    LOGOUT_ERROR,                           // V
    UNKNOWN_TASK,                           // V
}