package com.example.luksic10;

import database.Database;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.sort.SellingPriceCompare;
import hr.java.production.threads.GetAllCategoriesFromDatabaseThread;
import hr.java.production.threads.GetAllItemsFromDatabaseThread;
import hr.java.production.threads.SortingItemsThread;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.luksic10.FirstScreenController.*;

public class SearchItemsController {

    @FXML
    private TextField itemNameField;

    @FXML
    private ChoiceBox<String> categorySelector;

    @FXML
    private TableView<Item> itemsTableView;

    @FXML
    private TableColumn<Item, String> itemNameTableColumn;

    @FXML
    private TableColumn<Item, String> categoryNameTableColumn;

    @FXML
    private TableColumn<Item, String> widthTableColumn;

    @FXML
    private TableColumn<Item, String> heightTableColumn;

    @FXML
    private TableColumn<Item, String> lengthTableColumn;

    @FXML
    private TableColumn<Item, String> productionCostsTableColumn;

    @FXML
    private TableColumn<Item, String> sellingPriceTableColumn;

    private static List<Category> categoriesList;
    private static List<Item> itemList;

    @FXML
    public void initialize() {
        itemNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        categoryNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getCategory().getName());
        });

        widthTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getWidth().toString());
        });

        heightTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getHeight().toString());
        });

        lengthTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLength().toString());
        });

        productionCostsTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getProductionCost().toString());
        });

        sellingPriceTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getSellingPrice().toString());
        });

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

        GetAllCategoriesFromDatabaseThread thread2 = new GetAllCategoriesFromDatabaseThread();
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        executorService2.execute(thread2);
        executorService2.shutdown();
        try {
            executorService2.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        categoriesList = new ArrayList<>(Database.getCategoryList());

        categorySelector.getItems().addAll(categoriesList.stream().map(c -> c.getName()).collect(Collectors.toList()));

        Platform.runLater(new SortingItemsThread(itemList, itemsTableView));

        if(brojac == 0){
        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            GetAllItemsFromDatabaseThread threadClock = new GetAllItemsFromDatabaseThread();
            ExecutorService executorServiceClock = Executors.newCachedThreadPool();
            executorServiceClock.execute(threadClock);
            executorServiceClock.shutdown();
            try {
                executorServiceClock.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            List<Item> listaArtikala = new ArrayList<>(Database.getItemList());
            listaArtikala = listaArtikala.stream().sorted(new SellingPriceCompare()).collect(Collectors.toList());
            FirstScreen.getStage().setTitle("Item with the highest selling price: " + listaArtikala.get(0).getName());
            //System.out.println("tik");
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        brojac = 1;
        }
        else {
            if(!clock.getStatus().equals(Animation.Status.RUNNING)) {
                clock.play();
            }
        }
    }

    @FXML
    protected void onSearchButtonClick() {

        String enteredItemName = itemNameField.getText();
        String enteredCategory = categorySelector.getValue();

        List<Item> filteredList = null;
            filteredList = new ArrayList<>(Database.getItemList());

        if(itemNameField.getText().isBlank() == false) {
            filteredList = filteredList.stream()
                    .filter(i -> i.getName().toLowerCase().contains(enteredItemName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(Optional.ofNullable(enteredCategory).isEmpty() == false) {
            filteredList = filteredList.stream()
                    .filter(i -> i.getCategory().getName().toLowerCase().equals(enteredCategory.toLowerCase()))
                    .collect(Collectors.toList());
        }

        itemsTableView.setItems(FXCollections.observableList(filteredList));

    }

}
