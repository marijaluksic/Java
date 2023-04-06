package com.example.luksic10;

import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class FirstScreenController {
    public static Timeline clock = new Timeline();
    public static Integer brojac = 0;

    public void showFirstScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("firstScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setTitle("Production application");
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showCategorySearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("categorySearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setTitle("Production application");
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
        clock.stop();
    }

    public void showItemSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("itemSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setTitle("Production application");
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }
    public void showFactorySearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("factorySearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setTitle("Production application");
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
        clock.stop();
    }
    public void showStoreSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("storeSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setTitle("Production application");
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
        clock.stop();
    }
    public void addNewCategoryScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("addNewCategoryScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setTitle("Production application");
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
        clock.stop();
    }

    public void addNewItemScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("addNewItemScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setTitle("Production application");
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }
    public void addNewFactoryScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("addNewFactoryScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setTitle("Production application");
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
        clock.stop();
    }
    public void addNewStoreScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("addNewStoreScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setTitle("Production application");
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
        clock.stop();
    }
}
