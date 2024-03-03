package User;

import ServerObjectInterfaceApp.ServerObjectInterface;
import ServerObjectInterfaceApp.ServerObjectInterfaceHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class Test {
	public static final String PHYSICIAN = "Physician";
	public static final String SURGEON = "Surgeon";
	public static final String DENTAL = "Dental";

	public static void main(String[] args) {

		try {
			ORB orb = ORB.init(args, null);
			// -ORBInitialPort 1050 -ORBInitialHost localhost
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			addData(ncRef);
		} catch (Exception e) {
			System.out.println("Client ORB init exception: " + e);
			e.printStackTrace();
		}
	}

	private synchronized static void addData(NamingContextExt ncRef) throws Exception {
		ServerObjectInterface MTLobj = ServerObjectInterfaceHelper.narrow(ncRef.resolve_str("MTL"));
		ServerObjectInterface QUEobj = ServerObjectInterfaceHelper.narrow(ncRef.resolve_str("QUE"));
		ServerObjectInterface SHEobj = ServerObjectInterfaceHelper.narrow(ncRef.resolve_str("SHE"));

		System.out.println("TestCases Started");
		System.out.println("---------------------------------------------------------------");


		System.out.println("Logged in as SHEA3456 ADMIN:");
		System.out.println(SHEobj.addAppointment("SHEE150624", PHYSICIAN, 2));
		System.out.println(SHEobj.addAppointment("SHEE160624", SURGEON, 1));

		System.out.println("Logged in as MTLA2345 ADMIN:");
		System.out.println(MTLobj.addAppointment("MTLA160624", PHYSICIAN, 2));
		System.out.println(MTLobj.addAppointment("MTLA150624", DENTAL, 1));
		System.out.println(MTLobj.addAppointment("MTLA170624", DENTAL, 1));

		System.out.println("Logged in as QUEA2345 ADMIN:");
		System.out.println(QUEobj.addAppointment("QUEA150624", PHYSICIAN, 1));
		System.out.println(QUEobj.addAppointment("QUEA160624", SURGEON, 1));

		System.out.println("Logged in as QUEP1234 PATIENT:");
		System.out.println(QUEobj.bookAppointment("QUEP1234", "SHEE150624", PHYSICIAN));
		System.out.println(QUEobj.bookAppointment("QUEP1234", "SHEE160624", SURGEON));
		System.out.println(QUEobj.bookAppointment("QUEP1234", "MTLA160624", PHYSICIAN));
		System.out.println(QUEobj.bookAppointment("QUEP1234", "QUEA150624", PHYSICIAN));

		System.out.println("Logged in as SHEP1234 PATIENT:");
		System.out.println(SHEobj.bookAppointment("SHEC1234", "MTLA170624", DENTAL));

		System.out.println("Logged in as QUEA6785 ADMIN:");
		System.out.println(QUEobj.listAppointmentAvailability(PHYSICIAN));
		System.out.println(QUEobj.listAppointmentAvailability(DENTAL));
		System.out.println(QUEobj.listAppointmentAvailability(SURGEON));


		System.out.println();
		System.out.println("Testing Started");
		System.out.println("---------------------------------------------------------------");

		System.out.println("Test1");
		System.out.println("Quebec Admin listAppointmentAvailability:");
		System.out.println(QUEobj.listAppointmentAvailability(PHYSICIAN));
		System.out.println(QUEobj.listAppointmentAvailability(SURGEON));
		System.out.println(QUEobj.listAppointmentAvailability(DENTAL));
		System.out.println("---------------------------------------------------------------");

		System.out.println("Test2");
		System.out.println("QUEP1234 bookAppointment:");
		System.out.println(QUEobj.bookAppointment("QUEP1234", "SHEE110624", PHYSICIAN));
		System.out.println("QUEP1234 bookAppointment:");
		System.out.println(QUEobj.bookAppointment("QUEP1234", "MTLE230624", SURGEON));
		System.out.println("---------------------------------------------------------------");

		System.out.println("Test3");
		System.out.println("QUEP1234 cancelAppointment:");
		System.out.println(QUEobj.cancelAppointment("QUEP1234", "MTLA090624", PHYSICIAN));
		System.out.println("---------------------------------------------------------------");

		System.out.println("Test4");
		System.out.println("SHEP2345 bookAppointment:");
		System.out.println(SHEobj.bookAppointment("SHEP2345", "SHEE080624", PHYSICIAN));
		System.out.println("---------------------------------------------------------------");

		System.out.println("Test5");
		System.out.println("Montreal Admin removeAppointment:");
		System.out.println(MTLobj.removeAppointment("MTLA080624", DENTAL));
		System.out.println("---------------------------------------------------------------");

		System.out.println("Test6");
		System.out.println("SHEP2345 Booking Schedule:");
		System.out.println(SHEobj.getBookingSchedule("SHEP2345"));
		System.out.println("QUEP1234 Booking Schedule:");
		System.out.println(QUEobj.getBookingSchedule("QUEP1234"));
		System.out.println("---------------------------------------------------------------");

		System.out.println("Test7");
		System.out.println("Sherebrook Admin removeAppointment:");
		System.out.println(SHEobj.listAppointmentAvailability(PHYSICIAN));
		System.out.println(SHEobj.listAppointmentAvailability(SURGEON));
		System.out.println(SHEobj.listAppointmentAvailability(DENTAL));
		System.out.println("---------------------------------------------------------------");
	}
}
