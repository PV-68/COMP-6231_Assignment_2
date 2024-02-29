package Server;

import DataModel.AppointmentModel;
import Logger.Logger;
import ServerInterface.AppointmentManagement;
import ServerObjectInterfaceApp.ServerObjectInterface;
import ServerObjectInterfaceApp.ServerObjectInterfaceHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerInstance {

    private String serverID;
    private String serverName;
    private int serverUdpPort;

    public ServerInstance(String serverID, String[] args) throws Exception {
        this.serverID = serverID;
        switch (serverID) {
            case "MTL":
                serverName = AppointmentManagement.APPOINTMENT_SERVER_MONTREAL;
                serverUdpPort = AppointmentManagement.Montreal_Server_Port;
                break;
            case "QUE":
                serverName = AppointmentManagement.APPOINTMENT_SERVER_QUEBEC;
                serverUdpPort = AppointmentManagement.Quebec_Server_Port;
                break;
            case "SHE":
                serverName = AppointmentManagement.APPOINTMENT_SERVER_SHERBROOKE;
                serverUdpPort = AppointmentManagement.Sherbrooke_Server_Port;
                break;
        }
        try {
            // create and initialize the ORB //// get reference to rootpoa &amp; activate
            // the POAManager
            ORB orb = ORB.init(args, null);
            // -ORBInitialPort 1050 -ORBInitialHost localhost
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            AppointmentManagement servant = new AppointmentManagement(serverID, serverName);
            servant.setORB(orb);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(servant);
            ServerObjectInterface href = ServerObjectInterfaceHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent[] path = ncRef.to_name(serverID);
            ncRef.rebind(path, href);

            System.out.println(serverName + " Server is Up & Running");
            Logger.serverLog(serverID, " Server is Up & Running");

            addTestData(servant);
            Runnable task = () -> {
                listenForRequest(servant, serverUdpPort, serverName, serverID);
            };
            Thread thread = new Thread(task);
            thread.start();

            // wait for invocations from users
            while (true) {
                orb.run();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            Logger.serverLog(serverID, "Exception: " + e);
        }

        System.out.println(serverName + " Server Shutting down");
        Logger.serverLog(serverID, " Server Shutting down");

    }

    private void addTestData(AppointmentManagement remoteObject) {
        switch (serverID) {
            case "MTL":
                remoteObject.addNewAppointment("MTLA090620", AppointmentModel.PHYSICIAN, 2);
                remoteObject.addNewAppointment("MTLA080620", AppointmentModel.DENTAL, 2);
                remoteObject.addNewAppointment("MTLE230620", AppointmentModel.SURGEON, 1);
                remoteObject.addNewAppointment("MTLA150620", AppointmentModel.DENTAL, 12);
                break;
            case "QUE":
                remoteObject.addNewPatientToUsers("QUEP1234");
                remoteObject.addNewPatientToUsers("QUEP4114");
                break;
            case "SHE":
                remoteObject.addNewAppointment("SHEE110620", AppointmentModel.PHYSICIAN, 1);
                remoteObject.addNewAppointment("SHEE080620", AppointmentModel.PHYSICIAN, 1);
                break;
        }
    }

    private static void listenForRequest(AppointmentManagement obj, int serverUdpPort, String serverName, String serverID) {
        DatagramSocket aSocket = null;
        String sendingResult = "";
        try {
            aSocket = new DatagramSocket(serverUdpPort);
            byte[] buffer = new byte[1000];
            System.out.println(serverName + " UDP Server Started at port " + aSocket.getLocalPort() + " ............");
            Logger.serverLog(serverID, " UDP Server Started at port " + aSocket.getLocalPort());
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                String sentence = new String(request.getData(), 0,
                        request.getLength());
                String[] parts = sentence.split(";");
                String method = parts[0];
                String PatientID = parts[1];
                String appointmentType = parts[2];
                String appointmentID = parts[3];
                if (method.equalsIgnoreCase("removeAppointment")) {
                    Logger.serverLog(serverID, PatientID, " UDP request received " + method + " ", " appointmentID: " + appointmentID + " appointmentType: " + appointmentType + " ", " ...");
                    String result = obj.removeAppointmentUDP(appointmentID, appointmentType, PatientID);
                    sendingResult = result + ";";
                } else if (method.equalsIgnoreCase("listAppointmentAvailability")) {
                    Logger.serverLog(serverID, PatientID, " UDP request received " + method + " ", " appointmentType: " + appointmentType + " ", " ...");
                    String result = obj.listAppointmentAvailabilityUDP(appointmentType);
                    sendingResult = result + ";";
                } else if (method.equalsIgnoreCase("bookAppointment")) {
                    Logger.serverLog(serverID, PatientID, " UDP request received " + method + " ", " appointmentID: " + appointmentID + " appointmentType: " + appointmentType + " ", " ...");
                    String result = obj.bookAppointment(PatientID, appointmentID, appointmentType);
                    sendingResult = result + ";";
                } else if (method.equalsIgnoreCase("cancelAppointment")) {
                    Logger.serverLog(serverID, PatientID, " UDP request received " + method + " ", " appointmentID: " + appointmentID + " appointmentType: " + appointmentType + " ", " ...");
                    String result = obj.cancelAppointment(PatientID, appointmentID, appointmentType);
                    sendingResult = result + ";";
                }
                byte[] sendData = sendingResult.getBytes();
                DatagramPacket reply = new DatagramPacket(sendData, sendingResult.length(), request.getAddress(),
                        request.getPort());
                aSocket.send(reply);
                Logger.serverLog(serverID, PatientID, " UDP reply sent " + method + " ", " appointmentID: " + appointmentID + " appointmentType: " + appointmentType + " ", sendingResult);
            }
        } catch (SocketException e) {
            System.err.println("SocketException: " + e);
            e.printStackTrace(System.out);
        } catch (IOException e) {
            System.err.println("IOException: " + e);
            e.printStackTrace(System.out);
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }
}
