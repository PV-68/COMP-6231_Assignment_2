package Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static final int LOG_TYPE_SERVER = 1;
    public static final int LOG_TYPE_USER = 0;

    public static void userLog(String userID, String action, String requestParams, String response) throws IOException {
        FileWriter fileWriter = new FileWriter(getFileName(userID, LOG_TYPE_USER), true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("DATE: " + getFormattedDate() + " User Action: " + action + " | RequestParameters: " + requestParams + " | Server Response: " + response);

        printWriter.close();
    }

    public static void userLog(String userID, String msg) throws IOException {
        FileWriter fileWriter = new FileWriter(getFileName(userID, LOG_TYPE_USER), true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("DATE: " + getFormattedDate() + " " + msg);

        printWriter.close();
    }

    public static void serverLog(String serverID, String userID, String requestType, String requestParams, String serverResponse) throws IOException {

        if (userID.equals("null")) {
            userID = "Appointment Admin";
        }
        FileWriter fileWriter = new FileWriter(getFileName(serverID, LOG_TYPE_SERVER), true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("DATE: " + getFormattedDate() + " UserID: " + userID + " | RequestType: " + requestType + " | RequestParameters: " + requestParams + " | ServerResponse: " + serverResponse);

        printWriter.close();
    }

    public static void serverLog(String serverID, String msg) throws IOException {

        FileWriter fileWriter = new FileWriter(getFileName(serverID, LOG_TYPE_SERVER), true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("DATE: " + getFormattedDate() + " " + msg);

        printWriter.close();
    }

    public static void deleteALogFile(String ID) {

        String fileName = getFileName(ID, LOG_TYPE_USER);
        File file = new File(fileName);
        file.delete();
    }

    private static String getFileName(String ID, int logType) {
        final String dir = System.getProperty("user.dir");
        String fileName = dir;
        if (logType == LOG_TYPE_SERVER) {
            if (ID.equalsIgnoreCase("MTL")) {
                fileName = dir + "\\src\\Logs\\Server\\Montreal.txt";
            } else if (ID.equalsIgnoreCase("SHE")) {
                fileName = dir + "\\src\\Logs\\Server\\Sherbrooke.txt";
            } else if (ID.equalsIgnoreCase("QUE")) {
                fileName = dir + "\\src\\Logs\\Server\\Quebec.txt";
            }
        } else {
            fileName = dir + "\\src\\Logs\\User\\" + ID + ".txt";
        }
        return fileName;
    }

    private static String getFormattedDate() {
        Date date = new Date();

        String strDateFormat = "yyyy-MM-dd hh:mm:ss a";

        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

        return dateFormat.format(date);
    }

}
