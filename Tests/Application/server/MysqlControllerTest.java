package Application.server;

import common.Reports.InventoryReport;
import common.connectivity.User;
import common.orders.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class MysqlControllerTest {
    MysqlController mysqlController;


    // set up method
    @BeforeEach
    void setUp() {
        mysqlController = MysqlController.getSQLInstance();
        mysqlController.setDataBaseIP("localhost");
        mysqlController.setDataBaseUsername("root");
        mysqlController.setDataBasePassword("Aa123456");
        mysqlController.setDataBaseName("ekrutdatabase");
        mysqlController.connectDataBase();

    }

    // tested functionality: querying an existing report from the database
    // input: valid input, a list of strings, month, year, machine id
    // expected result: valid output, an InventoryReport object with all the details of the inventory report
    @Test
    void testGetMonthlyInventoryReportValidInputValidOutput() {
        // setting up report data
        ArrayList<String> monthYearMachine = new ArrayList<>();
        monthYearMachine.add("07");
        monthYearMachine.add("2022");
        monthYearMachine.add("UAE1");


        InventoryReport expectedReport = new InventoryReport();
        expectedReport.setReportID("2787339a");
        expectedReport.setArea("uae");
        expectedReport.setMachineID("UAE1");
        expectedReport.setMonth("07");
        expectedReport.setYear("2022");
        expectedReport.setTotalValue(5.4F);

        Product product = new Product();
        product.setProductId("P001");
        product.setDescription("Bamba nougatte");
        product.setAmount(1);
        product.setPrice(5.4F);
        product.setNumOfTimesBelowCritical(0);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        expectedReport.setProducts(products);

        InventoryReport actualReport = mysqlController.getMonthlyInventoryReport(monthYearMachine);

        assertEquals(expectedReport.getReportID(), actualReport.getReportID());
        assertEquals(expectedReport.getArea(), actualReport.getArea());
        assertEquals(expectedReport.getMachineID(), actualReport.getMachineID());
        assertEquals(expectedReport.getProducts().get(0).getProductId(), actualReport.getProducts().get(0).getProductId());
        assertEquals(expectedReport.getProducts().get(0).getName(), actualReport.getProducts().get(0).getName());
        assertEquals(expectedReport.getProducts().get(0).getAmount(), actualReport.getProducts().get(0).getAmount());
        assertEquals(expectedReport.getProducts().get(0).getPrice(), actualReport.getProducts().get(0).getPrice());
    }



    // tested functionality: querying an existing report that has an invalid or corrupted products list
    // input: valid input, a list of strings, month, year, machine id
    // expected result: returns null
    @Test
    void testGetMonthlyInventoryReportValidInputNullProductsReturnNull() {
        // setting up the object
        ArrayList<String> monthYearMachine = new ArrayList<>();
        monthYearMachine.add("01");
        monthYearMachine.add("2021");
        monthYearMachine.add("UAE3");

        InventoryReport actualReport = mysqlController.getMonthlyInventoryReport(monthYearMachine);
        assertNull(actualReport);
    }


    // tested functionality: testing if statement on function start that throws a NullPointerException
    // input: null input
    // expected result: throws a NullPointerException
    @Test
    void testGetMonthlyInventoryReportNullInputThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> mysqlController.getMonthlyInventoryReport(null));
    }


    // tested functionality: querying a report that doesn't exist in the database
    // input: input for which no matching record is found in the database
    // expected result: returns null
    @Test
    void testGetMonthlyInventoryReportNoMatchingRecordReturnsNull() {
        ArrayList<String> monthYearMachine = new ArrayList<>();
        monthYearMachine.add("01");
        monthYearMachine.add("1990");
        monthYearMachine.add("NOR2");

        InventoryReport actualReport = mysqlController.getMonthlyInventoryReport(monthYearMachine);

        assertNull(actualReport);
    }


    // tested functionality: querying an existing user
    // input: valid credentials, a list of strings, username and password
    // expected result: valid output, a User object with all the user details
    @Test
    public void testLogUserInValidCredentialsValidUser() {
        ArrayList<String> credentials = new ArrayList<>();
        credentials.add("lior");
        credentials.add("123");

        User expectedUser = new User();
        expectedUser.setUsername("lior");
        expectedUser.setFirstname("lior");
        expectedUser.setLastname("jigalo");
        expectedUser.setId("316109115");
        expectedUser.setPhonenumber("0528081434");
        expectedUser.setEmailaddress("audiblemaple@gmail.com");
        expectedUser.setDepartment("customer");
        expectedUser.setStatus("approved");

        User actualUser = mysqlController.logUserIn(credentials);

        assertEquals(expectedUser.getFirstname(), actualUser.getFirstname());
        assertEquals(expectedUser.getLastname(), actualUser.getLastname());
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getPhonenumber(), actualUser.getPhonenumber());
        assertEquals(expectedUser.getEmailaddress(), actualUser.getEmailaddress());
        assertEquals(expectedUser.getDepartment(), actualUser.getDepartment());
        assertEquals(expectedUser.getStatus(), actualUser.getStatus());
    }


    // tested functionality: test if statement that checks if credentials parameter is null
    // input: null credentials
    // expected result: throws a NullPointerException
    @Test
    public void testLogUserInNullCredentialsThrowsException() {
        assertThrows(NullPointerException.class, () -> mysqlController.logUserIn(null));
    }


    // tested functionality: query a user that doesn't exists
    // input: invalid credentials
    // expected result: returns null
    @Test
    public void testLogUserInInvalidCredentialsReturnsNull() {

        ArrayList<String> credentials = new ArrayList<>();
        credentials.add("nonExistentUser");
        credentials.add("password");

        User user = mysqlController.logUserIn(credentials);

        assertNull(user);
    }
}