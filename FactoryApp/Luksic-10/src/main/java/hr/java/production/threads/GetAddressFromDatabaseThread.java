package hr.java.production.threads;

import database.Database;
import hr.java.production.model.Address;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class GetAddressFromDatabaseThread implements Runnable{
    private static Connection connection = null;
    private static Address address;

    public void fetchAddressData(Address address) {
        this.address = address;
    }

    @Override
    public void run() {
        try {
            createConnection();
            Database.getAddressFromDatabase(connection, address);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            endConnection();
        }

    }

    public synchronized void createConnection() throws IOException, SQLException, InterruptedException {
        if(Database.activeConnectionWithDatabase){
            wait();
            System.out.println("Connection in use!");
        }
        connection = Database.connectToDatabase();
    }
    public synchronized void endConnection(){
        Database.disconnectFromDataBase(connection);
        notifyAll();
    }
}
