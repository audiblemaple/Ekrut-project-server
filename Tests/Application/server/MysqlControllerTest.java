package Application.server;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import common.Reports.InventoryReport;
import common.connectivity.User;
import common.orders.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class MysqlControllerTest {
    MysqlController mysqlController;
    String tooLongString;
    String northArea = "north";
    String machineID = "NOR1";
    String month = "01";
    String year = "2023";

    // set up method
    @BeforeEach
    void setUp() {
        mysqlController = MysqlController.getSQLInstance();
        mysqlController.setDataBaseIP("localhost");
        mysqlController.setDataBaseUsername("root");
        mysqlController.setDataBasePassword("Aa123456");
        mysqlController.setDataBaseName("ekrutdatabase");
        mysqlController.connectDataBase();

        northArea = "north";
        machineID = "NOR1";
        month = "01";
        year = "2023";

        tooLongString = "this string is too long to be updated in the database";
    }


    // tested functionality: generating a report that is not in the database
    // input: ArrayList that contains northArea, machineID, month, year
    // expected result: true, report added successfully
    @Test
    void testGenerateValidMonthlyInventoryReport(){
        // add new report
        ArrayList<String> data = new ArrayList<>();
        data.add(northArea);
        data.add(machineID);
        data.add(month);
        data.add(year);
        assertTrue(mysqlController.generateMonthlyInventoryReport(data));

        // check report is added
        data.clear();
        data.add(month);
        data.add(year);
        data.add(machineID);
        assertNotNull(mysqlController.getMonthlyInventoryReport(data));
    }


    // tested functionality: trying to generate a report that is already in the database
    // input: ArrayList that contains northArea, machineID, month, year (same as above)
    // expected result: true and catch SQLIntegrityConstraintViolationException, report already exists, and we don't need to do anything
    @Test
    void testGenerateValidMonthlyInventoryReportWhenReportAlreadyExistsCatchesSQLIntegrityConstraintViolationException(){
        ArrayList<String> data = new ArrayList<>();
        data.add(northArea);
        data.add(machineID);
        data.add(month);
        data.add(year);
        assertTrue(mysqlController.generateMonthlyInventoryReport(data));
    }

    // tested functionality: trying to write a value that is too long for the database cell
    // input: ArrayList that contains tooLongString, machineID, month, year
    // expected result: false and catch MysqlDataTruncation, cannot write data that is too long for its cell in the database
    @Test
    void testGenerateValidMonthlyInventoryReportCatchesMysqlDataTruncation(){
        ArrayList<String> data = new ArrayList<>();
        data.add(tooLongString);
        data.add(machineID);
        data.add(month);
        data.add(year);
        assertFalse(mysqlController.generateMonthlyInventoryReport(data));
    }


    // tested functionality: querying an existing report from the database
    // input: data of an existing report, a list of strings, month, year, machine id
    // expected result: an InventoryReport object with all the details of the inventory report
    @Test
    void testGetMonthlyInventoryReportWithExistingReport() {
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

        // assert report data:
        assertEquals(expectedReport.getReportID(), actualReport.getReportID());
        assertEquals(expectedReport.getArea(), actualReport.getArea());
        assertEquals(expectedReport.getMachineID(), actualReport.getMachineID());

        // assert product data in the report:
        assertEquals(expectedReport.getProducts().get(0).getProductId(), actualReport.getProducts().get(0).getProductId());
        assertEquals(expectedReport.getProducts().get(0).getName(), actualReport.getProducts().get(0).getName());
        assertEquals(expectedReport.getProducts().get(0).getAmount(), actualReport.getProducts().get(0).getAmount());
        assertEquals(expectedReport.getProducts().get(0).getPrice(), actualReport.getProducts().get(0).getPrice());
    }


    // tested functionality: querying an existing report that has nothing in its products list
    // input: valid input, a list of strings, month, year, machine id
    // expected result: null
    @Test
    void testGetMonthlyInventoryReportExistingReportWithNullProductsReturnNull() {
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


    // tested functionality: query a user that doesn't exist
    // input: incorrect credentials (user doesn't exist)
    // expected result: null
    @Test
    public void testLogUserInIncorrectCredentialsReturnsNull() {
        ArrayList<String> credentials = new ArrayList<>();
        credentials.add("nonExistentUser");
        credentials.add("password");

        User user = mysqlController.logUserIn(credentials);

        assertNull(user);
    }
}