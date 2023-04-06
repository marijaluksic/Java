package com.example.luksic10;

import database.Database;
import hr.java.production.model.Category;
import hr.java.production.model.Discount;
import hr.java.production.model.Item;
import hr.java.production.threads.GetAllCategoriesFromDatabaseThread;
import hr.java.production.threads.InsertNewItemToDatabaseThread;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.luksic10.FirstScreenController.brojac;
import static com.example.luksic10.FirstScreenController.clock;

public class AddNewItemController {
    @FXML
    private TextField itemNameTextField;

    @FXML
    private ChoiceBox<String> categorySelector;

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField lengthTextField;

    @FXML
    private TextField productionCostsTextField;

    @FXML
    private TextField sellingPriceTextField;


    private static List<Category> categoriesList;
    private static List<Item> itemList;

    @FXML
    public void initialize() {

        GetAllCategoriesFromDatabaseThread thread = new GetAllCategoriesFromDatabaseThread();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(thread);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        categoriesList = new ArrayList<>(Database.getCategoryList());

        categorySelector.getItems().addAll(categoriesList.stream().map(c -> c.getName()).collect(Collectors.toList()));
        if((brojac != 0) && (!clock.getStatus().equals(Animation.Status.RUNNING))) {
            clock.play();
        }
    }

    @FXML
    protected void onSaveButtonClick() {
        StringBuilder errorMessages = new StringBuilder();

        String itemName = itemNameTextField.getText();

        if(itemName.isEmpty()) {
            errorMessages.append("Item name must not be empty!\n");
        }
        BigDecimal itemWidth = BigDecimal.ZERO;
        String itemWidthString = widthTextField.getText();

        if(itemWidthString.isEmpty()) {
            errorMessages.append("Item width must not be empty!\n");
        }
        else {
            try {
                itemWidth = new BigDecimal(itemWidthString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("Item width must be in decimal format!\n");
            }
        }
        BigDecimal itemHeight = BigDecimal.ZERO;
        String itemHeightString = heightTextField.getText();

        if(itemHeightString.isEmpty()) {
            errorMessages.append("Item height must not be empty!\n");
        }
        else {
            try {
                itemHeight = new BigDecimal(itemHeightString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("Item height must be in decimal format!\n");
            }
        }

        BigDecimal itemLength = BigDecimal.ZERO;
        String itemLengthString = lengthTextField.getText();

        if(itemLengthString.isEmpty()) {
            errorMessages.append("Item length must not be empty!\n");
        }
        else {
            try {
                itemLength = new BigDecimal(itemLengthString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("Item length must be in decimal format!\n");
            }
        }

        BigDecimal itemProductionCost = BigDecimal.ZERO;
        String itemProductionCostString = productionCostsTextField.getText();

        if(itemProductionCostString.isEmpty()) {
            errorMessages.append("Item width must not be empty!\n");
        }
        else {
            try {
                itemProductionCost = new BigDecimal(itemProductionCostString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("Item production cost must be in decimal format!\n");
            }
        }

        BigDecimal itemSellingPrice = BigDecimal.ZERO;
        String itemSellingPriceString = sellingPriceTextField.getText();

        if(itemSellingPriceString.isEmpty()) {
            errorMessages.append("Item width must not be empty!\n");
        }
        else {
            try {
                itemSellingPrice = new BigDecimal(itemSellingPriceString);
            }
            catch (NumberFormatException ex) {
                errorMessages.append("Item selling price must be in decimal format!\n");
            }
        }

        String enteredCategory = categorySelector.getValue();

        if(Optional.ofNullable(enteredCategory).isEmpty() == true) {
            errorMessages.append("Item category must be selected!\n");
        }

        if(errorMessages.isEmpty()) {
            Item newItem = new Item(itemName, categoriesList.stream().filter(c -> c.getName().equals(enteredCategory)).findAny().get(),
                    itemWidth, itemHeight, itemLength, itemProductionCost, itemSellingPrice,
                    new Discount(new BigDecimal(0)));

            InsertNewItemToDatabaseThread thread2 = new InsertNewItemToDatabaseThread();
            ExecutorService executorService2 = Executors.newCachedThreadPool();
            thread2.fetchItemData(newItem);
            executorService2.execute(thread2);
            executorService2.shutdown();
            try {
                executorService2.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item added successfuly");
            alert.setHeaderText("Item added successfuly!");
            alert.setContentText("The item '" + itemName + "' was saved successfuly!");

            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
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
