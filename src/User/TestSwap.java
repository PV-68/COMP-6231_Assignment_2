package User;

import ServerObjectInterfaceApp.ServerObjectInterface;
import ServerObjectInterfaceApp.ServerObjectInterfaceHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class TestSwap {
    public static final String PHYSICIAN = "Physician";
    public static final String SURGEON = "Surgeon";
    public static final String DENTAL = "Dental";

    public static void main(String[] args) {

        try {
            ORB orb = ORB.init(args, null);
            // -ORBInitialPort 1050 -ORBInitialHost localhost
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            startTest(ncRef);
        } catch (Exception e) {
            System.out.println("Client ORB init exception: " + e);
            e.printStackTrace();
        }
    }

    private synchronized static void startTest(NamingContextExt ncRef) throws Exception {
        ServerObjectInterface MTLobj = ServerObjectInterfaceHelper.narrow(ncRef.resolve_str("MTL"));
        ServerObjectInterface QUEobj = ServerObjectInterfaceHelper.narrow(ncRef.resolve_str("QUE"));
        ServerObjectInterface SHEobj = ServerObjectInterfaceHelper.narrow(ncRef.resolve_str("SHE"));

        System.out.println("TestCases Started for Swap");
        System.out.println("---------------------------------------------------------");
        /*
          pre-added Test Cases file
          --------------------------
          QUEP1234 has booked:
          SHEE150624 - PHYSICIAN
          SHEE160624 - SURGEON
          MTLA160624 - PHYSICIAN
          QUEA150624 - PHYSICIAN
          <p>
          SHEP1234 has booked:
          MTLA170624 - DENTAL
          <p>
          appointments available:
          QUEA150624 - PHYSICIAN (0)
          MTLA160624 - PHYSICIAN (1)
          SHEE150624 - PHYSICIAN (1)
          <p>
          MTLA170624 - DENTAL (0)
          MTLA150624 - DENTAL (1)
          <p>
          QUEA160624 - SURGEON (1)
          SHEE160624 - SURGEON (0)
          */

        System.out.println("Case0 assuming the new appointment has no capacity:");
        System.out.println("swapAppointment(\"QUEP1234\", \"MTLA170624\", DENTAL, \"SHEE150624\", PHYSICIAN)");
        System.out.println("<Fail>");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA170624", DENTAL, "SHEE150624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");
        System.out.println("In all the below cases we are assuming that the new appointment has the capacity");
        System.out.println("Also the weekly limit is assumed to be for the newAppointment");
        System.out.println("Also in every Success situation we rolled back the result to before the swap to have consistent data to work with");
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case1 OldAppointment Does not exist-newAppointment exists:");
        System.out.println("swapAppointment(\"QUEP1234\",\"MTLA150624\",DENTAL, \"QUEA121212\",PHYSICIAN)");
        System.out.println("<Fail>");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA150624", DENTAL, "QUEA121212", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case2 OldAppointment exists-newAppointment does not exist:");
        System.out.println("<Fail>");
        System.out.println("swapAppointment(\"QUEP1234\",\"QUEA121212\", PHYSICIAN,\"SHEE160624\",SURGEON)");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA121212", PHYSICIAN, "SHEE160624", SURGEON));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case3 OldAppointment and newAppointment in user city:");
        System.out.println("(OldAppointment and newAppointment in same week)");
        System.out.println("<Success>");
        System.out.println("swapAppointment(\"QUEP1234\",\"QUEA160624\", SURGEON, \"QUEA150624\", PHYSICIAN)");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA160624", SURGEON, "QUEA150624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA150624", PHYSICIAN, "QUEA160624", SURGEON));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");
//
        System.out.println("Case4 OldAppointment and newAppointment in user city:");
        System.out.println("(OldAppointment and newAppointment NOT in same week)");
        System.out.println("<Success>");
        System.out.println("swapAppointment(\"QUEP1234\",\"QUEA160724\", PHYSICIAN,\"QUEA150624\", PHYSICIAN)");
        System.out.println(QUEobj.addAppointment("QUEA160724", PHYSICIAN, 1));
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA160724", PHYSICIAN, "QUEA150624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA150624", PHYSICIAN, "QUEA160724", PHYSICIAN));
        System.out.println(QUEobj.removeAppointment("QUEA160724", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case5 OldAppointment NOT in user city and newAppointment in user city:");
        System.out.println("(OldAppointment and newAppointment in same week)");
        System.out.println("<Success>");
        System.out.println("swapAppointment(\"QUEP1234\",\"QUEA160624\",SURGEON,\"MTLA160624\",PHYSICIAN)");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA160624", SURGEON, "MTLA160624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA160624", PHYSICIAN, "QUEA160624", SURGEON));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case6 OldAppointment NOT in user city and newAppointment in user city:");
        System.out.println("(OldAppointment and newAppointment NOT in same week)");
        System.out.println("<Success>");
        System.out.println("swapAppointment(\"QUEP1234\",\"QUEA160724\",PHYSICIAN,\"MTLA160624\",PHYSICIAN)");
        System.out.println(QUEobj.addAppointment("QUEA160724", PHYSICIAN, 1));
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA160724", PHYSICIAN, "MTLA160624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA160624", PHYSICIAN, "QUEA160724", PHYSICIAN));
        System.out.println(QUEobj.removeAppointment("QUEA160724", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case7 OldAppointment in user city and newAppointment in OTHER city:");
        System.out.println("(in same week && limit == 3)");
        System.out.println("<Fail>");
        System.out.println("swapAppointment(\"QUEP1234\",\"MTLA150624\", DENTAL, \"QUEA150624\", PHYSICIAN)");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA150624", DENTAL, "QUEA150624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case8 OldAppointment in user city and newAppointment in OTHER city:");
        System.out.println("(in same week && limit < 3)");
        System.out.println("<Success>");
        System.out.println("swapAppointment(\"QUEP1234\",\"MTLA150624\", DENTAL, \"QUEA150624\", PHYSICIAN)");
        System.out.println(QUEobj.cancelAppointment("QUEP1234", "SHEE150624", PHYSICIAN));
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA150624", DENTAL, "QUEA150624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA150624", PHYSICIAN, "MTLA150624", DENTAL));
        System.out.println(QUEobj.bookAppointment("QUEP1234", "SHEE150624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case9 OldAppointment in user city and newAppointment in OTHER city:");
        System.out.println("(NOT in same week && limit == 3)");
        System.out.println("<Fail>");
        System.out.println("swapAppointment(\"QUEP1234\",\"MTLA150624\",DENTAL,\"QUEA160724\",PHYSICIAN)");
        System.out.println(QUEobj.addAppointment("QUEA160724", PHYSICIAN, 1));
        System.out.println(QUEobj.bookAppointment("QUEP1234", "QUEA160724", PHYSICIAN));
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA150624", DENTAL, "QUEA160724", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.cancelAppointment("QUEP1234", "QUEA160724", PHYSICIAN));
        System.out.println(QUEobj.removeAppointment("QUEA160724", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case10 OldAppointment in user city and newAppointment in OTHER city:");
        System.out.println("(NOT in same week && limit < 3)");
        System.out.println("<Success>");
        System.out.println("swapAppointment(\"QUEP1234\",\"QUEA160724\", SURGEON,\"QUEA150624\", PHYSICIAN)");
        System.out.println(MTLobj.addAppointment("QUEA160724", SURGEON, 1));
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA160724", SURGEON, "QUEA150624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA150624", PHYSICIAN, "QUEA160724", SURGEON));
        System.out.println(MTLobj.removeAppointment("QUEA160724", SURGEON));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case11 OldAppointment and newAppointment in OTHER city:");
        System.out.println("(in same week && limit == 3)");
        System.out.println("<Success>");
        System.out.println("swapAppointment(\"QUEP1234\",\"MTLA150624\", DENTAL,\"SHEE160624\",SURGEON)");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA150624", DENTAL, "SHEE160624", SURGEON));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "SHEE160624", SURGEON, "MTLA150624", DENTAL));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case12 OldAppointment and newAppointment in OTHER city:");
        System.out.println("(in same week && limit < 3)");
        System.out.println("<Success>");
        System.out.println("swapAppointment(\"QUEP1234\",\"MTLA150624\", DENTAL,\"SHEE160624\",SURGEON)");
        System.out.println(QUEobj.cancelAppointment("QUEP1234", "MTLA160624", PHYSICIAN));
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA150624", DENTAL, "SHEE160624", SURGEON));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "SHEE160624", SURGEON, "MTLA150624", DENTAL));
        System.out.println(QUEobj.bookAppointment("QUEP1234", "MTLA160624", PHYSICIAN));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case13 OldAppointment and newAppointment in OTHER city:");
        System.out.println("(NOT in same week && limit == 3)");
        System.out.println("<Fail>");
        System.out.println("swapAppointment(\"QUEP1234\",\"MTLA150624\",DENTAL,\"QUEA160724\",SURGEON)");
        System.out.println(MTLobj.addAppointment("QUEA160724", SURGEON, 1));
        System.out.println(QUEobj.bookAppointment("QUEP1234", "QUEA160724", SURGEON));
        System.out.println(QUEobj.swapAppointment("QUEP1234", "MTLA150624", DENTAL, "QUEA160724", SURGEON));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.cancelAppointment("QUEP1234", "QUEA160724", SURGEON));
        System.out.println(MTLobj.removeAppointment("QUEA160724", SURGEON));
        System.out.println("---------------------------------------------------------------");

        System.out.println("Case14 OldAppointment and newAppointment in OTHER city:");
        System.out.println("(NOT in same week && limit < 3)");
        System.out.println("<Success>");
        System.out.println("swapAppointment(\"QUEP1234\",\"QUEA160724\",SURGEON,\"SHEE160624\",SURGEON)");
        System.out.println(MTLobj.addAppointment("QUEA160724", SURGEON, 1));
        System.out.println(QUEobj.swapAppointment("QUEP1234", "QUEA160724", SURGEON, "SHEE160624", SURGEON));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println(">>>Rolling back the changes...");
        System.out.println(QUEobj.swapAppointment("QUEP1234", "SHEE160624", SURGEON, "QUEA160724", SURGEON));
        System.out.println(MTLobj.removeAppointment("QUEA160724", SURGEON));
        System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
        System.out.println("---------------------------------------------------------------");


    }
}
