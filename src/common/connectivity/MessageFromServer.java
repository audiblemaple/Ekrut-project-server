package common.connectivity;

import java.io.Serializable;

/**
 * @author Lior Jigalo
 */
public enum MessageFromServer implements Serializable {
    IMPORT_ORDER_BY_ORDER_ID_AND_CUSTOMER_ID_SUCCESSFUL,    // V
    SUCCESSFULLY_GENERATED_MONTHLY_INVENTORY_REPORT,        // V
    IMPORT_ALL_MACHINES_MONTHLY_REPORT_SUCCESSFUL,          // V
    ERROR_IMPORTING_ALL_MACHINES_MONTHLY_REPORT,            // V
    PRODUCT_ADDED_SUCCESSFULLY,                             // V
    ERROR_GENERATING_MONTHLY_INVENTORY_REPORT,              // V
    WAREHOUSE_PRODUCTS_UPDATED_SUCCESSFULLY,                // V
    SUCCESSFULLY_IMPORTED_REFILL_ORDERS,                    // V
    ERROR_IMPORTING_REFILL_ORDERS,                          // V
    ERROR_IMPORTING_CUSTOMERS_FROM_USER_TABLE,              // TODO
    IMPORTING_CUSTOMERS_FROM_USER_TABLE_SUCCESSFUL,         // TODO
    ERROR_ADDING_PRODUCT,                                   // V
    ERROR_IMPORTING_ALL_MACHINE_PRODUCTS,                   // V
    MACHINE_PRODUCT_UPDATED_SUCCESSFULLY,                   // V
    CHECK_IF_CUSTOMER_IS_SUB_SUCCESSFUL,                    // V
    ERROR_CHECKING_IF_CUSTOMER_IS_SUB,                      // V
    ERROR_UPDATING_FIRST_TIME_BUY_AS_SUB,                   // TODO
    UPDATE_FIRST_TIME_AS_SUB_SUCCESSFUL,                    // TODO
    ERROR_UPDATING_MACHINE_AMOUNT,                          // V
    SUCCESSFULLY_UPDATED_AMOUNT_IN_MACHINE,                 // V
    ERROR_REMOVING_REFILL_ORDER,                            // V
    IMPORT_WAREHOUSE_PRODUCTS_SUCCESSFUL,   // V
    SUCCESSFULLY_ASSIGNED_EMPLOYEE_TO_REFILL_REQUEST,       // V
    ERROR_ASSIGNING_EMPLOYEE_TO_REFILL_REQUEST,             // V
    ERROR_GETTING_CUSTOMER_DATA,                            // V
    CUSTOMER_DATA_IMPORTED_SUCCESSFULLY,                    // V
    CREDIT_CARD_VERIFIED_SUCCESSFULLY,                      // V
    ERROR_VERIFYING_CREDIT_CARD,                            // V
    IMPORT_MACHINE_LOCATIONS_SUCCESSFUL,    // V
    ERROR_UPDATING_USERS_STATUSES,          // TODO
    USERS_STATUSES_UPDATED_SUCCESSFULLY,    // TODO
    SUCCESSFULLY_IMPORTED_CLIENT_REPORT,    // V
    IMPORT_INVENTORY_REPORT_SUCCESSFUL,     // V
    IMPORT_MACHINE_PRODUCTS_SUCCESSFUL,     // V
    ERROR_IMPORTING_WAREHOUSE_PRODUCTS,     // V
    ERROR_IMPORTING_MACHINE_LOCATIONS,      // V
    LOG_IN_ERROR_USER_DOES_NOT_EXIST,       // V
    ERROR_IMPORTING_MACHINE_PRODUCTS,       // V
    ERROR_IMPORTING_INVENTORY_REPORT,       // V
    ERROR_UPDATING_WAREHOUSE_PRODUCT,       // V
    IMPORT_ORDER_TABLE_UNSUCCESSFUL,        // TODO
    LOGIN_FAILED_ALREADY_LOGGED_IN,         // V
    ERROR_GETTING_MACHINE_PRODUCTS,         // V
    ERROR_UPDATING_MACHINE_PRODUCT,         // V
    IMPORT_USER_TABLE_SUCCESSFUL,           // TODO
    IMPORT_MACHINE_ID_SUCCESSFUL,           // V
    ERROR_IMPORTING_MACHINE_IDS,            // V
    LOGOUT_FAILED_NOT_LOGGED_IN,            // V
    IMPORT_ORDER_TABLE_COMPLETE,            // TODO
    ERROR_GETTING_CLIENT_REPORT,            // V
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