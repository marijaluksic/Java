package hr.java.production.threads;

import database.Database;
import hr.java.production.model.Item;
import hr.java.production.model.Store;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertItemsToNewStoreToDatabaseThread implements Runnable{
    private static Connection connection = null;
    private static Item item;
    private static Store store;

    public void fetchItemData(Item item, Store store) {
        this.item = item;
        this.store = store;
    }

    @Override
    public void run() {
        try {
            createConnection();
            Database.insertItemsToNewStoreToDatabase(connection, store, item);
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
