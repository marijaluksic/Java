package hr.java.production.threads;

import database.Database;
import hr.java.production.model.Store;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class GetStoreFromDatabaseThread implements Runnable{
    private static Connection connection = null;
    private static Store store;

    public void fetchStoreData(Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        try {
            createConnection();
            Database.getStoreFromDatabase(connection, store);
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
