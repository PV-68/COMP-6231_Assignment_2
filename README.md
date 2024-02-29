# Distributed Healthcare Management System (DHMS)
## Comp6231 Assignment 2 (CORBA) - Concordia University Winter 2024

#### How to Run the Project:
1. Navigate to your Java JDK bin directory using the command prompt (`cmd`).
2. Run the following command: `start orbd -ORBInitialPort 1050`

3. In the run configurations of both `client.java` and `server.java`, add `-ORBInitialPort 1050` and `-ORBInitialHost localhost`. This setup will enable you to run them successfully.

   If you encounter an error related to the CORBA NameServer, you should additionally run: `tnameserv -ORBInitialPort 1050` in the terminal.

   Note: In some cases, Eclipse may not find CORBA-related packages. In such instances, ensure you add the latest CORBA `jar` file to the external libraries.
