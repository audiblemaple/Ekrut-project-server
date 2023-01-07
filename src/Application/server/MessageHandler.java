package Application.server;

import OCSF.ConnectionToClient;
import common.Deals;
import common.RefillOrder;
import common.Reports.ClientReport;
import common.Reports.InventoryReport;
import common.Reports.OrderReport;
import common.connectivity.Customer;
import common.connectivity.Message;
import common.connectivity.MessageFromServer;
import common.connectivity.User;
import common.orders.Order;
import common.orders.Product;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author Lior Jigalo
 * This class handles messages from the client.
 */
public class MessageHandler {
    private static MysqlController mysqlcontroller = MysqlController.getSQLInstance();

    /**
     * @param clientMessage message from the client.
     * @param client        client to respond to.
     * This method handles clients requests, processes them and returns the needed data from database.
     */
    public static void handleMessage(Object clientMessage, ConnectionToClient client){
        // if got null return unknown task
        if (clientMessage == null){
            sendMessageToClient(client, new Message(null, MessageFromServer.UNKNOWN_TASK));
            return;
        }
        Message message = (Message) clientMessage;
        System.out.println(message);
        ArrayList<Product> productList = null;
        ArrayList<String> machines = null;
        switch(message.getTask().name()){ // TODO: add disconnect message to set client connection status to disconnected
            case "REQUEST_LOGIN":
                ArrayList<String> userLogInCredentials = (ArrayList<String>) message.getData();
                if (mysqlcontroller.isLoggedIn(userLogInCredentials)){
                    sendMessageToClient(client, new Message(null, MessageFromServer.LOGIN_FAILED_ALREADY_LOGGED_IN));
                    break;
                }
                User userData = mysqlcontroller.logUserIn(userLogInCredentials);
                if (userData == null){
                    sendMessageToClient(client, new Message(null, MessageFromServer.LOG_IN_ERROR_USER_DOES_NOT_EXIST));
                    break;
                }
                sendMessageToClient(client ,new Message(userData, MessageFromServer.LOGIN_SUCCESSFUL));
                break;

            case "REQUEST_LOGOUT":
                if (mysqlcontroller.logUserOut((ArrayList<String>)message.getData())){
                    sendMessageToClient(client, new Message(null, MessageFromServer.LOGOUT_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message(null, MessageFromServer.LOGOUT_FAILED_NOT_LOGGED_IN));
                break;

            case "REQUEST_LOCATION_PRODUCTS":
            case "REQUEST_ALL_MACHINE_PRODUCTS":
            case "REQUEST_MACHINE_PRODUCTS":
                productList = mysqlcontroller.getMachineProducts((ArrayList<String>) message.getData());
                if(productList != null){
                    sendMessageToClient(client, new Message(productList, MessageFromServer.IMPORT_MACHINE_PRODUCTS_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message("error importing products", MessageFromServer.ERROR_IMPORTING_MACHINE_PRODUCTS));
                break;

            case "REQUEST_ADD_USER":
                String result = mysqlcontroller.dataExists((Customer) message.getData());
                if(!result.equals("")){
                    sendMessageToClient(client, new Message(result, MessageFromServer.ERROR_ADDING_USER_EXISTS));
                    break;
                }
                if(mysqlcontroller.addUser((Customer) message.getData()) && mysqlcontroller.addCustomer((Customer) message.getData())){
                    sendMessageToClient(client, new Message(null, MessageFromServer.USER_ADDED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message(null, MessageFromServer.ERROR_ADDING_USER));
                break;

            case "REQUEST_DELETE_USER":
                if(mysqlcontroller.deleteUser((String)message.getData())){
                    sendMessageToClient(client, new Message(null, MessageFromServer.DELETE_USER_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message(null, MessageFromServer.ERROR_DELETING_USER));
                break;

            case "REQUEST_MACHINE_IDS":
                machines = mysqlcontroller.getMachineIds((String)message.getData());
                if (machines == null){
                    sendMessageToClient(client, new Message(null, MessageFromServer.ERROR_IMPORTING_MACHINE_IDS));
                    break;
                }
                sendMessageToClient(client, new Message(machines, MessageFromServer.IMPORT_MACHINE_ID_SUCCESSFUL));
                break;

            case "REQUEST_WAREHOUSE_PRODUCTS":
                productList = mysqlcontroller.getWarehouseProducts();
                if(productList != null){
                    sendMessageToClient(client, new Message(productList, MessageFromServer.IMPORT_WAREHOUSE_PRODUCTS_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message(productList, MessageFromServer.ERROR_IMPORTING_WAREHOUSE_PRODUCTS));
                break;

            case "REQUEST_ALL_MACHINE_LOCATIONS":
                ArrayList<String> locations = mysqlcontroller.getAllMachineLocations();
                if(locations != null){
                    sendMessageToClient(client, new Message(locations, MessageFromServer.IMPORT_MACHINE_LOCATIONS_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message("Error importing machine locations", MessageFromServer.ERROR_IMPORTING_MACHINE_LOCATIONS));
                break;

            case "REQUEST_MACHINE_MONTHLY_INVENTORY_REPORT":
                ArrayList<String> monthYearMachine = (ArrayList<String>) message.getData();
                InventoryReport report =  mysqlcontroller.getMonthlyInventoryReport(monthYearMachine);
                if(report != null){
                    sendMessageToClient(client, new Message(report, MessageFromServer.IMPORT_INVENTORY_REPORT_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message("Error importing inventory report", MessageFromServer.ERROR_IMPORTING_INVENTORY_REPORT));
                break;

            case "REQUEST_ADD_NEW_ORDER":
                if (!mysqlcontroller.AddNewOrder((Order) message.getData())){
                    sendMessageToClient(client, new Message("Error adding your order", MessageFromServer.ERROR_ADDING_NEW_ORDER));
                    break;
                }
                if (!mysqlcontroller.updateAmountsFromOrder((Order) message.getData())){
                    sendMessageToClient(client, new Message("Error updating order amounts", MessageFromServer.ERROR_ADDING_NEW_ORDER));
                }

                sendMessageToClient(client, new Message("Order added successfully", MessageFromServer.ADD_NEW_ORDER_SUCCESSFUL));
                break;

            case "REQUEST_ORDER_BY_ORDER_ID_AND_CUSTOMER_ID":
                Order order = mysqlcontroller.getOrderByOrderIdAndCustomerID((ArrayList<String>) message.getData());
                if (order == null){
                    sendMessageToClient(client, new Message("Error importing your order", MessageFromServer.ERROR_IMPORTING_ORDER));
                    break;
                }
                sendMessageToClient(client, new Message(order, MessageFromServer.IMPORT_ORDER_BY_ORDER_ID_AND_CUSTOMER_ID_SUCCESSFUL));
                break;

            case "REQUEST_GENERATE_MONTHLY_INVENTORY_REPORT": // TODO: should not be accessible by users
                if (mysqlcontroller.generateMonthlyInventoryReport((ArrayList<String>) message.getData()))
                    sendMessageToClient(client, new Message("report generated successfully", MessageFromServer.SUCCESSFULLY_GENERATED_MONTHLY_INVENTORY_REPORT));
                sendMessageToClient(client, new Message("report generated successfully", MessageFromServer.ERROR_GENERATING_MONTHLY_INVENTORY_REPORT));
                break;

            case "REQUEST_ALL_MACHINES_ORDERS_MONTHLY_REPORT":
                ArrayList<OrderReport> orderReports = mysqlcontroller.getOrderReport((ArrayList<String>) message.getData());
                if (orderReports == null)
                    sendMessageToClient(client, new Message("Error to get order report", MessageFromServer.ERROR_IMPORTING_ALL_MACHINES_MONTHLY_REPORT));
                sendMessageToClient(client, new Message(orderReports, MessageFromServer.IMPORT_ALL_MACHINES_MONTHLY_REPORT_SUCCESSFUL));
                break;

            case "REQUEST_UPDATE_WAREHOUSE_PRODUCTS":
                if (mysqlcontroller.updateWarehouseProduct((Product) message.getData())){
                    sendMessageToClient(client, new Message("data updated successfully!", MessageFromServer.WAREHOUSE_PRODUCTS_UPDATED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message("failed to update warehouse product", MessageFromServer.ERROR_UPDATING_WAREHOUSE_PRODUCT));


            case"REQUEST_UPDATE_MACHINE_PRODUCTS":
                if (mysqlcontroller.updateMachineProduct((Product) message.getData())){
                    sendMessageToClient(client, new Message("data updated successfully!", MessageFromServer.MACHINE_PRODUCT_UPDATED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message("failed to update machine product", MessageFromServer.ERROR_UPDATING_MACHINE_PRODUCT));
                break;

            case "REQUEST_ADD_NEW_PRODUCT_TO_PRODUCT_TABLE":
                if (mysqlcontroller.checkProductExistsInGivenTable((Product) message.getData(), "")){
                    sendMessageToClient(client, new Message("product already exists", MessageFromServer.ERROR_ADDING_PRODUCT));
                    break;
                }
                if (mysqlcontroller.addNewProductToProductTable((Product) message.getData())){
                    sendMessageToClient(client, new Message("New product added successfully", MessageFromServer.PRODUCT_ADDED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message("failed to add new product to product table", MessageFromServer.ERROR_ADDING_PRODUCT));
                break;

            case "REQUEST_ADD_NEW_PRODUCT_TO_WAREHOUSE":
                if (mysqlcontroller.checkProductExistsInGivenTable((Product) message.getData(), "warehouse")){
                    sendMessageToClient(client, new Message("product already exists", MessageFromServer.ERROR_ADDING_PRODUCT));
                    break;
                }
                if (mysqlcontroller.addNewProductToWarehouse((Product) message.getData())){
                    sendMessageToClient(client, new Message("successfully added product to warehouse", MessageFromServer.PRODUCT_ADDED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message("failed to add product to warehouse", MessageFromServer.ERROR_ADDING_PRODUCT));
                break;

            case "REQUEST_ADD_NEW_PRODUCT_TO_MACHINE":
                if (mysqlcontroller.checkProductExistsInGivenTable((Product) message.getData(), "machine")){
                    sendMessageToClient(client, new Message("product already exists", MessageFromServer.ERROR_ADDING_PRODUCT));
                    break;
                }
                if (mysqlcontroller.addNewProductToMachine((Product) message.getData())){
                    sendMessageToClient(client, new Message("successfully added product to machine", MessageFromServer.PRODUCT_ADDED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message("failed to add product to machine", MessageFromServer.ERROR_ADDING_PRODUCT));
                break;

            case "REQUEST_CLIENT_REPORT":
                ClientReport clientreport = mysqlcontroller.getClientReport((ArrayList<String>) message.getData());
                if(clientreport == null){
                    sendMessageToClient(client, new Message("error getting client report", MessageFromServer.ERROR_GETTING_CLIENT_REPORT));
                    break;
                }
                sendMessageToClient(client, new Message(clientreport, MessageFromServer.SUCCESSFULLY_IMPORTED_CLIENT_REPORT));
                break;

            case "REQUEST_REFILL_ORDERS":
                mysqlcontroller.checkAmount();
                ArrayList<RefillOrder> refillOrderList = mysqlcontroller.getRefillOrders();
                if (refillOrderList == null){
                    sendMessageToClient(client, new Message("error getting refill orders", MessageFromServer.ERROR_IMPORTING_REFILL_ORDERS));
                    break;
                }
                sendMessageToClient(client, new Message(refillOrderList, MessageFromServer.SUCCESSFULLY_IMPORTED_REFILL_ORDERS));
                break;

            case "REQUEST_UPDATE_MACHINE_PRODUCT_AMOUNT":
                if ( !mysqlcontroller.updateMachineAmount((ArrayList<String>) message.getData())){
                    sendMessageToClient(client, new Message("Error updating amount!", MessageFromServer.ERROR_UPDATING_MACHINE_AMOUNT));
                    break;
                }
                if ( !mysqlcontroller.completeOrderRefil((ArrayList<String>) message.getData())){
                    sendMessageToClient(client, new Message("Error removing refill order", MessageFromServer.ERROR_REMOVING_REFILL_ORDER));
                    break;
                }
                sendMessageToClient(client, new Message("Amount in machine updated successfully!", MessageFromServer.SUCCESSFULLY_UPDATED_AMOUNT_IN_MACHINE));
                break;


            case "REQUEST_ASSIGN_EMPLOYEE_TO_REFILL_ORDER":
                if (mysqlcontroller.assignEmployeeToRefillOrder((RefillOrder) message.getData())){
                    sendMessageToClient(client, new Message("employee assigned successfully!", MessageFromServer.SUCCESSFULLY_ASSIGNED_EMPLOYEE_TO_REFILL_REQUEST));
                    break;
                }
                sendMessageToClient(client, new Message("Error assigning employee to request!", MessageFromServer.ERROR_ASSIGNING_EMPLOYEE_TO_REFILL_REQUEST));
                break;

            case "REQUEST_CUSTOMER_DATA":
                Customer customer = mysqlcontroller.getCustomerData((String) message.getData());
                if (customer == null){
                    sendMessageToClient(client, new Message("Error getting customer data", MessageFromServer.ERROR_GETTING_CUSTOMER_DATA));
                    break;
                }
                sendMessageToClient(client, new Message(customer, MessageFromServer.CUSTOMER_DATA_IMPORTED_SUCCESSFULLY));
                break;

            case "REQUEST_CREDIT_CARD_CHECK":
                if ( !mysqlcontroller.verifyCreditCard((ArrayList<String>) message.getData())){
                    sendMessageToClient(client, new Message("Wrong credit card number", MessageFromServer.ERROR_VERIFYING_CREDIT_CARD));
                    break;
                }
                sendMessageToClient(client, new Message("credit card verified successfully", MessageFromServer.CREDIT_CARD_VERIFIED_SUCCESSFULLY));

            case "REQUEST_SET_FIRST_TIME_BUY_AS_SUB":
                if ( !mysqlcontroller.updateFirstBuyAsSub((String) message.getData())){
                    sendMessageToClient(client, new Message("Error updating status", MessageFromServer.ERROR_UPDATING_FIRST_TIME_BUY_AS_SUB));
                    break;
                }
                sendMessageToClient(client, new Message("status updated successfully", MessageFromServer.UPDATE_FIRST_TIME_AS_SUB_SUCCESSFUL));
                break;

            case "REQUEST_CHECK_IF_SUB":
                if (mysqlcontroller.checkIfCustomerIsSub((String) message.getData())){
                    sendMessageToClient(client, new Message("customer is sub", MessageFromServer.CHECK_IF_CUSTOMER_IS_SUB_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message("customer is not sub", MessageFromServer.ERROR_CHECKING_IF_CUSTOMER_IS_SUB));
                break;

            case "REQUEST_CUSTOMERS_FROM_USER_TABLE":
                ArrayList<User> users = mysqlcontroller.getAllCustomerUsers();
                if (users == null){
                    sendMessageToClient(client, new Message("Error getting users", MessageFromServer.ERROR_IMPORTING_CUSTOMERS_FROM_USER_TABLE));
                    break;
                }
                sendMessageToClient(client, new Message(users, MessageFromServer.IMPORTING_CUSTOMERS_FROM_USER_TABLE_SUCCESSFUL));
                break;

            case "REQUEST_UPDATE_USERS_STATUSES":
                if (mysqlcontroller.updateUsersStatuses((ArrayList<User>) message.getData())){
                    sendMessageToClient(client, new Message("User statuses updated successfully", MessageFromServer.USERS_STATUSES_UPDATED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message("Error updating user statuses", MessageFromServer.ERROR_UPDATING_USERS_STATUSES));
                break;

            case "REQUEST_DISCOUNT_LIST":
                ArrayList<Deals> dealList = mysqlcontroller.getAllDiscounts();
                if (dealList == null){
                    sendMessageToClient(client, new Message("No deals to import", MessageFromServer.ERROR_GETTING_DEALS));
                    break;
                }
                sendMessageToClient(client, new Message(dealList, MessageFromServer.DEALS_IMPORTED_SUCCESSFULLY));
                break;

            case "REQUEST_UPDATE_DEALS":
                if (!mysqlcontroller.updateDeal((Deals) message.getData())){
                    sendMessageToClient(client, new Message("Error updating deal.", MessageFromServer.ERROR_UPDATING_DEAL));
                    break;
                }
                if (mysqlcontroller.applyDeals()) {
                    sendMessageToClient(client, new Message("deal updated successfully.", MessageFromServer.DEAL_UPDATED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message("Error updating deal.", MessageFromServer.ERROR_UPDATING_DEAL));
                break;

            case "REQUEST_UPDATE_ORDER_STATUS":
                if (mysqlcontroller.updateOrderStatus((ArrayList<String>) message.getData())){
                    sendMessageToClient(client, new Message("Order updated successfully", MessageFromServer.ORDER_STATUS_UPDATED_SUCCESSFULLY));
                    break;
                }
                sendMessageToClient(client, new Message("Error updating status", MessageFromServer.ERROR_UPDATING_ORDER_STATUS));
                break;

            case "REQUEST_ALL_CUSTOMER_DATA":
                ArrayList<Customer> customerList = mysqlcontroller.getAllCustomerData();
                if (customerList == null){
                    sendMessageToClient(client, new Message("Error getting users", MessageFromServer.ERROR_IMPORTING_CUSTOMER_DATA));
                    break;
                }
                sendMessageToClient(client, new Message(customerList, MessageFromServer.CUSTOMER_IMPORTED_SUCCESSFULLY));
                break;

            case "REQUEST_UPDATE_CUSTOMER_STATUS":
                if ( mysqlcontroller.updateCustomerSubscriber((ArrayList<String>) message.getData())){
                    sendMessageToClient(client, new Message("customer updated successfully", MessageFromServer.CUSTOMER_UPDATE_SUCCESSFUL));
                    break;
                }
                sendMessageToClient(client, new Message("Error updating customer", MessageFromServer.ERROR_UPDATING_CUSTOMER));
                break;


            case "REQUEST_ORDERS_BY_AREA":
                ArrayList<Order> orderList = mysqlcontroller.getOrderByArea((String)message.getData());
                if (orderList == null){
                    sendMessageToClient(client, new Message("Error importing orders", MessageFromServer.ERROR_IMPORTING_ORDERS_BY_AREA));
                    break;
                }
                sendMessageToClient(client, new Message(orderList, MessageFromServer.ORDERS_IMPORTED_SUCCESSFULLY));
                break;


            default:
                sendMessageToClient(client, new Message(null, MessageFromServer.UNKNOWN_TASK));
        }
    }

    /**
     * @param client    the client to send the message to.
     * @param message   the message to send to the client.
     * This method sends a message to the client passed as a parameter.
     */
    private static void sendMessageToClient(ConnectionToClient client, Message message){
        try {
            client.sendToClient(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}