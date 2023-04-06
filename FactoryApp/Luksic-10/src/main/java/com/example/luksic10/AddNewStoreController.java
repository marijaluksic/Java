package com.example.luksic10;

import database.Database;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.threads.GetAllItemsFromDatabaseThread;
import hr.java.production.threads.GetStoreFromDatabaseThread;
import hr.java.production.threads.InsertItemsToNewStoreToDatabaseThread;
import hr.java.production.threads.InsertNewStoreToDatabaseThread;
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

public class AddNewStoreController {
    @FXML
    private TextField storeNameTextField;

    @FXML
    private TextField webAddressTextField;

    @FXML
    private ListView<String> itemsListView;

    private static List<Category> categoriesList;
    private static List<Item> itemList;
    private static List<Store> storeList;
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
        itemList = Database.getItemList();
        ObservableList<String> itemsObservableList = FXCollections.observableList(itemList.stream().map(item -> item.getName()).collect(Collectors.toList()));
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
    protected void onSaveButtonClick() {
        StringBuilder errorMessages = new StringBuilder();

        String storeName = storeNameTextField.getText();

        if(storeName.isEmpty()) {
            errorMessages.append("Store name must not be empty!\n");
        }
        String storeWebAddress = webAddressTextField.getText();

        if(storeWebAddress.isEmpty()) {
            errorMessages.append("Store web address must not be empty!\n");
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
            Store newStore = new Store(storeName, storeWebAddress, selectedItemsSet);

            InsertNewStoreToDatabaseThread thread2 = new InsertNewStoreToDatabaseThread();
            ExecutorService executorService2 = Executors.newCachedThreadPool();
            thread2.fetchStoreData(newStore);
            executorService2.execute(thread2);
            executorService2.shutdown();
            try {
                executorService2.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            GetStoreFromDatabaseThread thread3 = new GetStoreFromDatabaseThread();
            ExecutorService executorService3 = Executors.newCachedThreadPool();
            thread3.fetchStoreData(newStore);
            executorService3.execute(thread3);
            executorService3.shutdown();
            try {
                executorService3.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            Store newestStore = Database.getDucan();
            newStore.setId(newestStore.getId());

            for(Item item : selectedItemsSet) {
                InsertItemsToNewStoreToDatabaseThread thread4 = new InsertItemsToNewStoreToDatabaseThread();
                ExecutorService executorService4 = Executors.newCachedThreadPool();
                thread4.fetchItemData(item, newStore);
                executorService4.execute(thread4);
                executorService4.shutdown();
                try {
                    executorService4.awaitTermination(1, TimeUnit.SECONDS);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Store added successfuly");
            alert.setHeaderText("Store added successfuly!");
            alert.setContentText("The store '" + storeName + "' was saved successfuly!");

            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
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
