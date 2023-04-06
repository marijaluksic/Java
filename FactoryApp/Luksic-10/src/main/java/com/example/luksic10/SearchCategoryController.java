package com.example.luksic10;

import database.Database;
import hr.java.production.model.Category;
import hr.java.production.threads.GetAllCategoriesFromDatabaseThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchCategoryController {

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, String> categoryNameTableColumn;

    @FXML
    private TableColumn<Category, String> categoryDescriptionTableColumn;

    private static List<Category> categoriesList;

    @FXML
    public void initialize() {
        categoryNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        categoryDescriptionTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDescription());
        });

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

        ObservableList<Category> categoriesObservableList = FXCollections.observableList(categoriesList);

        categoryTableView.setItems(categoriesObservableList);

    }

    @FXML
    protected void onSearchButtonClick() {

        String enteredCategoryName = categoryNameTextField.getText();

        List<Category> filteredList = null;

        filteredList = new ArrayList<>(Database.getCategoryList());

        if(categoryNameTextField.getText().isBlank() == false) {
            filteredList = filteredList.stream()
                    .filter(i -> i.getName().toLowerCase().contains(enteredCategoryName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        categoryTableView.setItems(FXCollections.observableList(filteredList));

    }
}
