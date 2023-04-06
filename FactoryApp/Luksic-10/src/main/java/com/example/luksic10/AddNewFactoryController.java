package com.example.luksic10;

import database.Database;
import hr.java.production.model.Address;
import hr.java.production.model.Category;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import hr.java.production.threads.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AddNewFactoryController {
    @FXML
    private TextField factoryNameTextField;

    @FXML
    private TextField factoryStreetTextField;

    @FXML
    private TextField factoryHouseNumberTextField;

    @FXML
    private TextField factoryCityTextField;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private ListView<String> itemsListView;

    private static List<Category> categoriesList;
    private static List<Item> itemList;
    private static List<Address> addressList;
    private static List<Factory> factoryList;
    private static List<String> list = new ArrayList<>();

    @FXML
    public void initialize() {
        GetAllItemsFromDatabaseThread thread = new GetAllItemsFromDatabaseThread();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(thread);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        itemList = new ArrayList<>(Database.getItemList());

        ObservableList<String> itemsObservableList = FXCollections.observableList(itemList.stream().map(item ->
                item.getName()).collect(Collectors.toList()));
        itemsListView.getItems().addAll(itemsObservableList);

        list = new ArrayList<>();
        itemsListView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        list.add(item);
                    } else {
                        list.remove(item);
                    }
                });
                return observable;
            }
        }));
    }

    @FXML
    protected void onSaveButtonClick() {StringBuilder errorMessages = new StringBuilder();

        String factoryName = factoryNameTextField.getText();

        if(factoryName.isEmpty()) {
            errorMessages.append("Factory name must not be empty!\n");
        }
        String factoryStreet = factoryStreetTextField.getText();

        if(factoryStreet.isEmpty()) {
            errorMessages.append("Factory street must not be empty!\n");
        }

        String houseNumber = factoryHouseNumberTextField.getText();

        if(houseNumber.isEmpty()) {
            errorMessages.append("Factory house number must not be empty!\n");
        }

        String enteredCity = factoryCityTextField.getText();

        if(enteredCity.isEmpty()) {
            errorMessages.append("City must not be empty!\n");
        }

        String postalCode = postalCodeTextField.getText();

        if(postalCode.isEmpty()) {
            errorMessages.append("Postal code must not be empty!\n");
        }

        if(list.isEmpty()) {
            errorMessages.append("Items must be selected!\n");
        }

        if(errorMessages.isEmpty()) {

            Set<Item> selectedItemsSet = new HashSet<>();
            for(String selectedItemName : list){
                selectedItemsSet.add(itemList.stream().
                        filter(item -> item.getName().equals(selectedItemName)).findFirst().get());
            }
            Address factoryAddress = new Address(factoryStreet, houseNumber, enteredCity, postalCode);


            InsertNewAddressToDatabaseThread thread2 = new InsertNewAddressToDatabaseThread();
            ExecutorService executorService2 = Executors.newCachedThreadPool();
            thread2.fetchAddressData(factoryAddress);
            executorService2.execute(thread2);
            executorService2.shutdown();
            try {
                executorService2.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            GetAddressFromDatabaseThread thread3 = new GetAddressFromDatabaseThread();
            ExecutorService executorService3 = Executors.newCachedThreadPool();
            thread3.fetchAddressData(factoryAddress);
            executorService3.execute(thread3);
            executorService3.shutdown();
            try {
                executorService3.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            Address factoryAddressWithId = Database.getAdresa();
            factoryAddress.setId(factoryAddressWithId.getId());


            Factory newFactory = new Factory(factoryName, factoryAddress, selectedItemsSet);

            InsertNewFactoryToDatabaseThread thread4 = new InsertNewFactoryToDatabaseThread();
            ExecutorService executorService4 = Executors.newCachedThreadPool();
            thread4.fetchFactoryData(newFactory);
            executorService4.execute(thread4);
            executorService4.shutdown();
            try {
                executorService4.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            GetFactoryFromDatabaseThread thread5 = new GetFactoryFromDatabaseThread();
            ExecutorService executorService5 = Executors.newCachedThreadPool();
            thread5.fetchFactoryData(newFactory);
            executorService5.execute(thread5);
            executorService5.shutdown();
            try {
                executorService5.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            Factory newestFactory = Database.getTvornica();
            newFactory.setId(newestFactory.getId());

            for(Item item : selectedItemsSet) {
                InsertItemsToNewFactoryToDatabaseThread thread6 = new InsertItemsToNewFactoryToDatabaseThread();
                ExecutorService executorService6 = Executors.newCachedThreadPool();
                thread6.fetchItemData(item, newestFactory);
                executorService6.execute(thread6);
                executorService6.shutdown();
                try {
                    executorService6.awaitTermination(1, TimeUnit.SECONDS);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Factory added successfuly");
            alert.setHeaderText("Factory added successfuly!");
            alert.setContentText("The factory '" + factoryName + "' was saved successfuly!");

            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
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
