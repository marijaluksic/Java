package com.example.luksic10;

import database.Database;
import hr.java.production.model.Address;
import hr.java.production.model.Category;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import hr.java.production.threads.GetAllAddressesFromDatabaseThread;
import hr.java.production.threads.GetAllFactoriesFromDatabaseThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchFactoryController {
    @FXML
    private TextField factoryNameField;

    @FXML
    private ChoiceBox<String> citySelector;

    @FXML
    private TableView<Factory> factoriesTableView;

    @FXML
    private TableColumn<Factory, String> factoryNameTableColumn;

    @FXML
    private TableColumn<Factory, String> cityTableColumn;

    @FXML
    private TableColumn<Factory, String> postalCodeTableColumn;

    @FXML
    private TableColumn<Factory, String> streetTableColumn;

    @FXML
    private TableColumn<Factory, String> houseNumberTableColumn;

    @FXML
    private TableColumn<Factory, String> productsListTableColumn;
    private static List<Category> categoriesList;

    private static List<Item> itemList;

    private static List<Address> addressList;

    private static List<Factory> factoryList;


    @FXML
    public void initialize() {
        factoryNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        cityTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAddress().getCityy());
        });

        postalCodeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAddress().getPostalCode());
        });

        streetTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAddress().getStreet());
        });

        houseNumberTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getAddress().getHouseNumber());
        });

        productsListTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getItems().stream().map(f -> f.getName())
                    .collect(Collectors.joining(", ")));
        });

        GetAllFactoriesFromDatabaseThread thread = new GetAllFactoriesFromDatabaseThread();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(thread);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        factoryList = new ArrayList<>(Database.getFactoryList());

        GetAllAddressesFromDatabaseThread thread2 = new GetAllAddressesFromDatabaseThread();
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        executorService2.execute(thread2);
        executorService2.shutdown();
        try {
            executorService2.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        addressList = new ArrayList<>(Database.getAddressList());

        ObservableList<Factory> factoryObservableList = FXCollections.observableList(factoryList);

        factoriesTableView.setItems(factoryObservableList);
        citySelector.getItems().addAll(addressList.stream().map(c -> c.getCityy()).collect(Collectors.toSet()));
    }

    @FXML
    protected void onSearchButtonClick() {

        String enteredItemName = factoryNameField.getText();
        String enteredCity = citySelector.getValue();

        List<Factory> filteredList = null;

        filteredList = new ArrayList<>(Database.getFactoryList());

        if(factoryNameField.getText().isBlank() == false) {
            filteredList = filteredList.stream()
                    .filter(i -> i.getName().toLowerCase().contains(enteredItemName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(Optional.ofNullable(enteredCity).isEmpty() == false) {
            filteredList = filteredList.stream()
                    .filter(i -> i.getAddress().getCityy().toLowerCase(Locale.ROOT).equals(enteredCity.toLowerCase()))
                    .collect(Collectors.toList());
        }

        factoriesTableView.setItems(FXCollections.observableList(filteredList));

    }
}
