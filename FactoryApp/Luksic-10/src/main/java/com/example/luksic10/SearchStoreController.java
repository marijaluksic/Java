package com.example.luksic10;

import database.Database;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.threads.GetAllStoresFromDatabaseThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchStoreController {

    @FXML
    private RadioButton itemCount4;

    @FXML
    private RadioButton itemCount5;

    @FXML
    private TextField storeNameField;

    @FXML
    private TableView<Store> storesTableView;

    @FXML
    private TableColumn<Store, String> storeNameTableColumn;

    @FXML
    private TableColumn<Store, String> storeTypeTableColumn;

    @FXML
    private TableColumn<Store, String> webAddressTableColumn;

    @FXML
    private TableColumn<Store, String> productsListTableColumn;

    private static List<Category> categoriesList;
    private static List<Item> itemList;
    private static List<Store> storeList;

/*    private static TechnicalStore<Technical> tehnickiDucan = new TechnicalStore<Technical>(1L,
            "Technical Store", "www.technicalstore.com",
            Main.insertNewSet(Technical.class.getSimpleName(), Main.fetchItemsFromList(Main.fetchCategoriesFromList()).get()), Main.insertNewTechnicalList(Main.fetchItemsFromList(Main.fetchCategoriesFromList()).get()));
    private static FoodStore<Edible> prehrambeniDucan = new FoodStore<Edible>(2L, "Food Store",
            "www.foodstore.com", Main.insertNewSet(Edible.class.getSimpleName(), Main.fetchItemsFromList(Main.fetchCategoriesFromList()).get()),
            Main.insertNewFoodList(Main.fetchItemsFromList(Main.fetchCategoriesFromList()).get()));*/

    @FXML
    public void initialize() {
//        storeList.add(0, tehnickiDucan);
//        storeList.add(1, prehrambeniDucan);

        storeNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        storeTypeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getClass().getSimpleName());
        });

        webAddressTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getWebAddress());
        });

        productsListTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getItems().stream().map(f -> f.getName()).collect(Collectors.joining(", ")));
        });

        GetAllStoresFromDatabaseThread thread = new GetAllStoresFromDatabaseThread();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(thread);
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        storeList = new ArrayList<>(Database.getStoreList());

        ObservableList<Store> storeObservableList = FXCollections.observableList(storeList);

        storesTableView.setItems(storeObservableList);
    }

    @FXML
    protected void onSearchButtonClick() {

        String enteredItemName = storeNameField.getText();
        String itemCountSelected = "";

        if(itemCount4.isSelected()) {
            itemCountSelected = "4";
        } else if(itemCount5.isSelected()) {
            itemCountSelected = "5";
        }


        List<Store> filteredList = new ArrayList<>(storeList);

        if(storeNameField.getText().isBlank() == false) {
            filteredList = filteredList.stream()
                    .filter(i -> i.getName().toLowerCase().contains(enteredItemName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(itemCountSelected.isEmpty() == false) {
            if(itemCountSelected.equalsIgnoreCase("4")) {
                filteredList = filteredList.stream()
                        .filter(i -> i.getItems().size()>4)
                        .collect(Collectors.toList());
            }
            else if(itemCountSelected.equalsIgnoreCase("5")){
                filteredList = filteredList.stream()
                        .filter(i -> i.getItems().size()<5)
                        .collect(Collectors.toList());
            }
        }

        storesTableView.setItems(FXCollections.observableList(filteredList));

    }
}
