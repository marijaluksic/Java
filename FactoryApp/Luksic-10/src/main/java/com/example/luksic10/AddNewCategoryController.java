package com.example.luksic10;

import hr.java.production.model.Category;
import hr.java.production.threads.InsertNewCategoryToDatabaseThread;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddNewCategoryController {
    @FXML
    private TextField categoryNameTextField;

    @FXML
    private TextField categoryDescriptionTextField;

    @FXML
    protected void onSaveButtonClick() {
        StringBuilder errorMessages = new StringBuilder();

        String categoryName = categoryNameTextField.getText();

        if(categoryName.isEmpty()) {
            errorMessages.append("Category name must not be empty!\n");
        }

        String categoryDescription = categoryDescriptionTextField.getText();

        if(categoryDescription.isEmpty()) {
            errorMessages.append("Category description must not be empty!\n");
        }

        if(errorMessages.isEmpty()) {
            Category newCategory = new Category(categoryName, categoryDescription);

            InsertNewCategoryToDatabaseThread thread = new InsertNewCategoryToDatabaseThread();
            ExecutorService executorService = Executors.newCachedThreadPool();
            thread.fetchCategoryData(newCategory);
            executorService.execute(thread);
            executorService.shutdown();
            try {
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Category added successfuly");
            alert.setHeaderText("Category added successfuly!");
            alert.setContentText("The category '" + categoryName + "' was saved successfuly!");

            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
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
            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation errors");
            alert.setHeaderText("There was one or more errors");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }

    }
}
