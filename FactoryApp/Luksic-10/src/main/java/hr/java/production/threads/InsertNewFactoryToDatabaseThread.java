package hr.java.production.threads;

import database.Database;
import hr.java.production.model.Factory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertNewFactoryToDatabaseThread implements Runnable{
    private static Connection connection = null;
    private static Factory factory;

    public void fetchFactoryData(Factory factory) {
        this.factory = factory;
    }

    @Override
    public void run() {
        try {
            createConnection();
            Database.insertNewFactoryToDatabase(connection, factory);
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
