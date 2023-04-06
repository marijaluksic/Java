package com.example.luksic10;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstScreen extends Application {
    private static Stage mainStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("firstScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        primaryStage.setTitle("Production application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
//        try{
//            System.out.println("Uspjesno spajanje na bazu!");
//            List<Item> itemList = Database.getAllItemsFromDatabase();
//            for(Item item : itemList) {
//                item.toString();
//           }
//            List<Category> categoryList = Database.getAllCategoriesFromDatabase();
//            for(Category category : categoryList) {
//                category.toString();
//            }
//            List<Store> storeList = Database.getAllStoresFromDatabase();
//            for(Store store : storeList) {
//                store.toString();
//            }
//            List<Factory> factoryList = Database.getAllFactoriesFromDatabase();
//            for(Factory factory : factoryList) {
//                factory.toString();
//            }
//        } catch (SQLException | IOException ex) {
//            System.out.println("Pogreska pri spajanju na bazu!");
//            ex.printStackTrace();
//        }
    }
    public static Stage getStage() {
        return mainStage;
    }
}
