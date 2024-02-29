package ServerObjectInterfaceApp;


/**
* ServerObjectInterfaceApp/ServerObjectInterfaceOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ServerObjectInterface.idl
* Thursday, February 29, 2024 1:42:29 o'clock PM EST
*/

public interface ServerObjectInterfaceOperations 
{

  /**
        * Only Admin
        */
  String addAppointment (String appointmentID, String appointmentType, int bookingCapacity);
  String removeAppointment (String appointmentID, String appointmentType);
  String listAppointmentAvailability (String appointmentType);

  /**
        * Only Patient
        */
  String bookAppointment (String patientID, String appointmentID, String appointmentType);
  String getBookingSchedule (String patientID);
  String cancelAppointment (String patientID, String appointmentID, String appointmentType);
  String swapAppointment (String patientID, String newAppointmentID, String newAppointmentType, String oldAppointmentID, String oldAppointmentType);
  void shutdown ();
} // interface ServerObjectInterfaceOperations