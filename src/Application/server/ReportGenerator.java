package Application.server;

import java.util.Calendar;
import javafx.concurrent.Task;


/**
 * ReportGenerator class is responsible for generating reports on the first day of every month.
 * It extends the Task class and overrides the call method.
 * The class uses the MysqlController class to call the generateReports method, which generates all the necessary reports.
 * It uses a while loop to continually check if the current date is the first day of the month.
 * If it is the first day of the month, the reports are generated and the thread sleeps until the first day of the next month.
 * If it is not the first day of the month, the thread sleeps for 24 hours before checking again.
 * @author lior jigalo
 */
public class ReportGenerator extends Task<Void> {
    MysqlController mysqlController = MysqlController.getSQLInstance();
    @Override
    protected Void call() throws Exception {
        while (true) {
            // Get the current date
            Calendar cal = Calendar.getInstance();
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);

            // Check if the current date is the first day of the month
            if (dayOfMonth == 1) {
                mysqlController.generateReports();

                // Sleep until the first day of the next month
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                long sleepTime = cal.getTimeInMillis() - System.currentTimeMillis();
                Thread.sleep(sleepTime);
            } else {
                // Sleep for 24 hours
                Thread.sleep(24 * 60 * 60 * 1000);
            }
        }
    }
}