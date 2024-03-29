package ServerObjectInterfaceApp;


/**
* ServerObjectInterfaceApp/_ServerObjectInterfaceStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ServerObjectInterface.idl
* Thursday, February 29, 2024 4:16:03 o'clock PM EST
*/

public class _ServerObjectInterfaceStub extends org.omg.CORBA.portable.ObjectImpl implements ServerObjectInterfaceApp.ServerObjectInterface
{


  /**
        * Only Admin
        */
  public String addAppointment (String appointmentID, String appointmentType, int bookingCapacity)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("addAppointment", true);
                $out.write_string (appointmentID);
                $out.write_string (appointmentType);
                $out.write_long (bookingCapacity);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return addAppointment (appointmentID, appointmentType, bookingCapacity        );
            } finally {
                _releaseReply ($in);
            }
  } // addAppointment

  public String removeAppointment (String appointmentID, String appointmentType)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("removeAppointment", true);
                $out.write_string (appointmentID);
                $out.write_string (appointmentType);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return removeAppointment (appointmentID, appointmentType        );
            } finally {
                _releaseReply ($in);
            }
  } // removeAppointment

  public String listAppointmentAvailability (String appointmentType)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("listAppointmentAvailability", true);
                $out.write_string (appointmentType);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return listAppointmentAvailability (appointmentType        );
            } finally {
                _releaseReply ($in);
            }
  } // listAppointmentAvailability


  /**
        * Only Patient
        */
  public String bookAppointment (String patientID, String appointmentID, String appointmentType)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("bookAppointment", true);
                $out.write_string (patientID);
                $out.write_string (appointmentID);
                $out.write_string (appointmentType);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return bookAppointment (patientID, appointmentID, appointmentType        );
            } finally {
                _releaseReply ($in);
            }
  } // bookAppointment

  public String getBookingSchedule (String patientID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getBookingSchedule", true);
                $out.write_string (patientID);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getBookingSchedule (patientID        );
            } finally {
                _releaseReply ($in);
            }
  } // getBookingSchedule

  public String cancelAppointment (String patientID, String appointmentID, String appointmentType)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("cancelAppointment", true);
                $out.write_string (patientID);
                $out.write_string (appointmentID);
                $out.write_string (appointmentType);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return cancelAppointment (patientID, appointmentID, appointmentType        );
            } finally {
                _releaseReply ($in);
            }
  } // cancelAppointment

  public String swapAppointment (String patientID, String newAppointmentID, String newAppointmentType, String oldAppointmentID, String oldAppointmentType)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("swapAppointment", true);
                $out.write_string (patientID);
                $out.write_string (newAppointmentID);
                $out.write_string (newAppointmentType);
                $out.write_string (oldAppointmentID);
                $out.write_string (oldAppointmentType);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return swapAppointment (patientID, newAppointmentID, newAppointmentType, oldAppointmentID, oldAppointmentType        );
            } finally {
                _releaseReply ($in);
            }
  } // swapAppointment

  public void shutdown ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("shutdown", false);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                shutdown (        );
            } finally {
                _releaseReply ($in);
            }
  } // shutdown

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ServerObjectInterfaceApp/ServerObjectInterface:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _ServerObjectInterfaceStub
