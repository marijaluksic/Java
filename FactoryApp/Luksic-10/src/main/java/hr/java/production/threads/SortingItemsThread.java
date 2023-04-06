package hr.java.production.threads;

import hr.java.production.model.Item;
import hr.java.production.sort.SellingPriceCompare;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.stream.Collectors;

public class SortingItemsThread implements Runnable{

    private List<Item> itemList;
    private TableView<Item> itemsTableView;

    public SortingItemsThread(List<Item> itemList, TableView<Item> itemsTableView) {
        this.itemList = itemList;
        this.itemsTableView = itemsTableView;
    }

    @Override
    public void run() {
            itemList = itemList.stream()
                    .sorted(new SellingPriceCompare())
                    .collect(Collectors.toList());

            ObservableList<Item> itemsObservableList = FXCollections.observableList(itemList);

            itemsTableView.setItems(itemsObservableList);
    }

}
