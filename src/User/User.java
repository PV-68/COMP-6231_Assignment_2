package User;

import DataModel.AppointmentModel;
import Logger.Logger;
import ServerObjectInterfaceApp.ServerObjectInterface;
import ServerObjectInterfaceApp.ServerObjectInterfaceHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.util.Scanner;

public class User {
    public static final int USER_TYPE_PATIENT = 1;
    public static final int USER_TYPE_ADMIN = 2;
    public static final int PATIENT_BOOK_APPOINTMENT = 1;
    public static final int PATIENT_GET_BOOKING_SCHEDULE = 2;
    public static final int PATIENT_CANCEL_APPOINTMENT = 3;
    public static final int PATIENT_SWAP_APPOINTMENT = 4;
    public static final int PATIENT_LOGOUT = 5;
    public static final int ADMIN_ADD_APPOINTMENT = 1;
    public static final int ADMIN_REMOVE_APPOINTMENT = 2;
    public static final int ADMIN_LIST_APPOINTMENT_AVAILABILITY = 3;
    public static final int ADMIN_LOGOUT = 4;
    public static final int SHUTDOWN = 0;

    static Scanner input;

    public static void main(String[] args) throws Exception {
        try {
            ORB orb = ORB.init(args, null);
            // -ORBInitialPort 1050 -ORBInitialHost localhost
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            init(ncRef);
        } catch (Exception e) {
            System.out.println("User ORB init exception: " + e);
            e.printStackTrace();
        }
    }

    public static void init(NamingContextExt ncRef) throws Exception {
        input = new Scanner(System.in);
        String userID;
        System.out.println("---------------------------------------------------------------");
        System.out.println("Please Enter your UserID(For Concurrency test enter 'ConTest'):");
        userID = input.next().trim().toUpperCase();
        if (userID.equalsIgnoreCase("ConTest")) {
            startConcurrencyTest(ncRef);
        } else {
            Logger.userLog(userID, " login attempt");
            switch (checkUserType(userID)) {
                case USER_TYPE_PATIENT:
                    try {
                        System.out.println("Patient Login successful (" + userID + ")");
                        Logger.userLog(userID, " Patient Login successful");
                        patient(userID, ncRef);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case USER_TYPE_ADMIN:
                    try {
                        System.out.println("Admin Login successful (" + userID + ")");
                        Logger.userLog(userID, " Admin Login successful");
                        admin(userID, ncRef);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("!!UserID is not in correct format");
                    Logger.userLog(userID, " UserID is not in correct format");
                    Logger.deleteALogFile(userID);
                    init(ncRef);
            }
        }
    }

    private static void startConcurrencyTest(NamingContextExt ncRef) throws Exception {
        System.out.println("Concurrency Test Starting for BookAppointment");
        System.out.println("Connecting Montreal Server...");
        String appointmentType = AppointmentModel.PHYSICIAN;
        String appointmentID = "MTLE101020";
        ServerObjectInterface servant = ServerObjectInterfaceHelper.narrow(ncRef.resolve_str("MTL"));
        System.out.println("adding " + appointmentID + " " + appointmentType + " with capacity 2 to Montreal Server...");
        String response = servant.addAppointment(appointmentID, appointmentType, 2);
        System.out.println(response);
        Runnable task1 = () -> {
            String patientID = "MTLC2345";
            String res = servant.bookAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Booking response for " + patientID + " " + res);
            res = servant.cancelAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Canceling response for " + patientID + " " + res);
        };
        Runnable task2 = () -> {
            String patientID = "MTLC3456";
            String res = servant.bookAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Booking response for " + patientID + " " + res);
            res = servant.cancelAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Canceling response for " + patientID + " " + res);
        };
        Runnable task3 = () -> {
            String patientID = "MTLC4567";
            String res = servant.bookAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Booking response for " + patientID + " " + res);
            res = servant.cancelAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Canceling response for " + patientID + " " + res);
        };
        Runnable task4 = () -> {
            String patientID = "MTLC6789";
            String res = servant.bookAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Booking response for " + patientID + " " + res);
            res = servant.cancelAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Canceling response for " + patientID + " " + res);
        };
        Runnable task5 = () -> {
            String patientID = "MTLC7890";
            String res = servant.bookAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Booking response for " + patientID + " " + res);
            res = servant.cancelAppointment(patientID, appointmentID, appointmentType);
            System.out.println("Canceling response for " + patientID + " " + res);
        };

        Runnable task6 = () -> {
            String res = servant.removeAppointment(appointmentID, appointmentType);
            System.out.println("removeAppointment response for " + appointmentID + " " + res);
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);
        Thread thread4 = new Thread(task4);
        Thread thread5 = new Thread(task5);
        Thread thread6 = new Thread(task6);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        System.out.println("Concurrency Test Finished for BookAppointment");
        thread6.start();
        thread6.join();
        init(ncRef);

    }

    private static String getServerID(String userID) {
        String branchAcronym = userID.substring(0, 3);
        if (branchAcronym.equalsIgnoreCase("MTL")) {
            return branchAcronym;
        } else if (branchAcronym.equalsIgnoreCase("SHE")) {
            return branchAcronym;
        } else if (branchAcronym.equalsIgnoreCase("QUE")) {
            return branchAcronym;
        }
        return "1";
    }

    private static int checkUserType(String userID) {
        if (userID.length() == 8) {
            if (userID.substring(0, 3).equalsIgnoreCase("MTL") ||
                    userID.substring(0, 3).equalsIgnoreCase("QUE") ||
                    userID.substring(0, 3).equalsIgnoreCase("SHE")) {
                if (userID.substring(3, 4).equalsIgnoreCase("P")) {
                    return USER_TYPE_PATIENT;
                } else if (userID.substring(3, 4).equalsIgnoreCase("A")) {
                    return USER_TYPE_ADMIN;
                }
            }
        }
        return 0;
    }

    private static void patient(String patientID, NamingContextExt ncRef) throws Exception {
        String serverID = getServerID(patientID);
        if (serverID.equals("1")) {
            init(ncRef);
        }
        ServerObjectInterface servant = ServerObjectInterfaceHelper.narrow(ncRef.resolve_str(serverID));
        boolean repeat = true;
        printMenu(USER_TYPE_PATIENT);
        int menuSelection = input.nextInt();
        String appointmentType;
        String appointmentID;
        String serverResponse;
        switch (menuSelection) {
            case PATIENT_BOOK_APPOINTMENT:
                appointmentType = promptForAppointmentType();
                appointmentID = promptForAppointmentID();
                Logger.userLog(patientID, " attempting to bookAppointment");
                serverResponse = servant.bookAppointment(patientID, appointmentID, appointmentType);
                System.out.println(serverResponse);
                Logger.userLog(patientID, " bookAppointment", " appointmentID: " + appointmentID + " appointmentType: " + appointmentType + " ", serverResponse);
                break;
            case PATIENT_GET_BOOKING_SCHEDULE:
                Logger.userLog(patientID, " attempting to getBookingSchedule");
                serverResponse = servant.getBookingSchedule(patientID);
                System.out.println(serverResponse);
                Logger.userLog(patientID, " bookAppointment", " null ", serverResponse);
                break;
            case PATIENT_CANCEL_APPOINTMENT:
                appointmentType = promptForAppointmentType();
                appointmentID = promptForAppointmentID();
                Logger.userLog(patientID, " attempting to cancelAppointment");
                serverResponse = servant.cancelAppointment(patientID, appointmentID, appointmentType);
                System.out.println(serverResponse);
                Logger.userLog(patientID, " bookAppointment", " appointmentID: " + appointmentID + " appointmentType: " + appointmentType + " ", serverResponse);
                break;
            case PATIENT_SWAP_APPOINTMENT:
                System.out.println("Please Enter the OLD appointment to be replaced");
                appointmentType = promptForAppointmentType();
                appointmentID = promptForAppointmentID();
                System.out.println("Please Enter the NEW appointment to be replaced");
                String newAppointmentType = promptForAppointmentType();
                String newAppointmentID = promptForAppointmentID();
                Logger.userLog(patientID, " attempting to swapAppointment");
                serverResponse = servant.swapAppointment(patientID, newAppointmentID, newAppointmentType, appointmentID, appointmentType);
                System.out.println(serverResponse);
                Logger.userLog(patientID, " swapAppointment", " oldAppointmentID: " + appointmentID + " oldAppointmentType: " + appointmentType + " newAppointmentID: " + newAppointmentID + " newAppointmentType: " + newAppointmentType + " ", serverResponse);
                break;
            case SHUTDOWN:
                Logger.userLog(patientID, " attempting ORB shutdown");
                servant.shutdown();
                Logger.userLog(patientID, " shutdown");
                return;
            case PATIENT_LOGOUT:
                repeat = false;
                Logger.userLog(patientID, " attempting to Logout");
                init(ncRef);
                break;
        }
        if (repeat) {
            patient(patientID, ncRef);
        }
    }

    private static void admin(String appointmentAdminID, NamingContextExt ncRef) throws Exception {
        String serverID = getServerID(appointmentAdminID);
        if (serverID.equals("1")) {
            init(ncRef);
        }
        ServerObjectInterface servant = ServerObjectInterfaceHelper.narrow(ncRef.resolve_str(serverID));
        boolean repeat = true;
        printMenu(USER_TYPE_ADMIN);
        String patientID;
        String appointmentType;
        String appointmentID;
        String serverResponse;
        int capacity;
        int menuSelection = input.nextInt();
        switch (menuSelection) {
            case ADMIN_ADD_APPOINTMENT:
                appointmentType = promptForAppointmentType();
                appointmentID = promptForAppointmentID();
                capacity = promptForCapacity();
                Logger.userLog(appointmentAdminID, " attempting to addAppointment");
                serverResponse = servant.addAppointment(appointmentID, appointmentType, capacity);
                System.out.println(serverResponse);
                Logger.userLog(appointmentAdminID, " addAppointment", " appointmentID: " + appointmentID + " appointmentType: " + appointmentType + " appointmentCapacity: " + capacity + " ", serverResponse);
                break;
            case ADMIN_REMOVE_APPOINTMENT:
                appointmentType = promptForAppointmentType();
                appointmentID = promptForAppointmentID();
                Logger.userLog(appointmentAdminID, " attempting to removeAppointment");
                serverResponse = servant.removeAppointment(appointmentID, appointmentType);
                System.out.println(serverResponse);
                Logger.userLog(appointmentAdminID, " removeAppointment", " appointmentID: " + appointmentID + " appointmentType: " + appointmentType + " ", serverResponse);
                break;
            case ADMIN_LIST_APPOINTMENT_AVAILABILITY:
                appointmentType = promptForAppointmentType();
                Logger.userLog(appointmentAdminID, " attempting to listAppointmentAvailability");
                serverResponse = servant.listAppointmentAvailability(appointmentType);
                System.out.println(serverResponse);
                Logger.userLog(appointmentAdminID, " listAppointmentAvailability", " appointmentType: " + appointmentType + " ", serverResponse);
                break;
            case SHUTDOWN:
                Logger.userLog(appointmentAdminID, " attempting ORB shutdown");
                servant.shutdown();
                Logger.userLog(appointmentAdminID, " shutdown");
                return;
            case ADMIN_LOGOUT:
                repeat = false;
                Logger.userLog(appointmentAdminID, "attempting to Logout");
                init(ncRef);
                break;
        }
        if (repeat) {
            admin(appointmentAdminID, ncRef);
        }
    }

    private static void printMenu(int userType) {
        System.out.println("---------------------------------------------------------------");
        System.out.println("Please choose an option below:");
        if (userType == USER_TYPE_PATIENT) {
            System.out.println("1.Book Appointment");
            System.out.println("2.Get Booking Schedule");
            System.out.println("3.Cancel Appointment");
            System.out.println("4.Swap Appointment");
            System.out.println("5.Logout");
            System.out.println("0.ShutDown");
        } else if (userType == USER_TYPE_ADMIN) {
            System.out.println("1.Add Appointment");
            System.out.println("2.Remove Appointment");
            System.out.println("3.List Appointment Availability");
            System.out.println("4.Logout");
            System.out.println("0.ShutDown");
        }
    }

    private static String promptForAppointmentType() {
        System.out.println("---------------------------------------------------------------");
        System.out.println("Please choose an appointmentType below:");
        System.out.println("1.Physician");
        System.out.println("2.Surgeon");
        System.out.println("3.Dental");
        switch (input.nextInt()) {
            case 1:
                return AppointmentModel.PHYSICIAN;
            case 2:
                return AppointmentModel.SURGEON;
            case 3:
                return AppointmentModel.DENTAL;
        }
        return promptForAppointmentType();
    }

    private static String promptForAppointmentID() {
        System.out.println("---------------------------------------------------------------");
        System.out.println("Please enter the AppointmentID (e.g MTLM190124)");
        String appointmentID = input.next().trim().toUpperCase();
        if (appointmentID.length() == 10) {
            if (appointmentID.substring(0, 3).equalsIgnoreCase("MTL") ||
                    appointmentID.substring(0, 3).equalsIgnoreCase("SHE") ||
                    appointmentID.substring(0, 3).equalsIgnoreCase("QUE")) {
                if (appointmentID.substring(3, 4).equalsIgnoreCase("M") ||
                        appointmentID.substring(3, 4).equalsIgnoreCase("A") ||
                        appointmentID.substring(3, 4).equalsIgnoreCase("E")) {
                    return appointmentID;
                }
            }
        }
        return promptForAppointmentID();
    }

    private static int promptForCapacity() {
        System.out.println("---------------------------------------------------------------");
        System.out.println("Please enter the booking capacity:");
        return input.nextInt();
    }
}
