package Application.server;

import java.util.Calendar;
import javafx.concurrent.Task;

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