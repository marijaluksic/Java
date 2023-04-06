package hr.java.production.threads;

import database.Database;
import hr.java.production.model.Category;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertNewCategoryToDatabaseThread implements Runnable{
    private static Connection connection = null;
    private static Category category;

    public void fetchCategoryData(Category category) {
        this.category = category;
    }

    @Override
    public void run() {
        try {
            createConnection();
            Database.insertNewCategoryToDatabase(connection, category);
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
