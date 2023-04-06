package hr.java.production.files;

import hr.java.production.enumeracija.AvailableCities;
import hr.java.production.main.Main;
import hr.java.production.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FilesUtils {
    public static void saveCategories(List<Category> categoriesList) throws IOException {
        FileWriter writer = new FileWriter(Main.CATEGORIES_FILE_NAME);
        PrintWriter printer = new PrintWriter(writer);

        for(Category category : categoriesList) {
            printer.println(category.getId().toString());
            printer.println(category.getName());
            printer.println(category.getDescription());
        }

        writer.flush();
    }
    public static void saveItems(List<Item> itemsList) throws IOException {
        FileWriter writer = new FileWriter(Main.ITEMS_FILE_NAME);
        PrintWriter printer = new PrintWriter(writer);

        for(Item item : itemsList) {
            printer.println(item.getCategory().getId().toString());
            printer.println(item.getId().toString());
            printer.println(item.getName());
            printer.println(item.getWidth().toString());
            printer.println(item.getHeight().toString());
            printer.println(item.getLength().toString());
            printer.println(item.getProductionCost().toString());
            printer.println(item.getSellingPrice().toString());
            printer.println(item.getDiscount().discountAmount().toString());
        }

        writer.flush();
    }
    public static void saveStores(List<Store> storesList) throws IOException {
        FileWriter writer = new FileWriter(Main.STORES_FILE_NAME);
        PrintWriter printer = new PrintWriter(writer);

        for(Store store : storesList) {
            printer.println(store.getId().toString());
            printer.println(store.getName());
            printer.println(store.getWebAddress());
            List<Long> listItemId = store.getItems().stream().map(s -> s.getId()).collect(Collectors.toList());
            String itemsInStore = "";
            for(Long number : listItemId) {
                if(itemsInStore.isEmpty()){
                    itemsInStore += number.toString();
                }
                else{
                    itemsInStore += "," + number.toString();
                }
            }
            printer.println(itemsInStore);
        }

        writer.flush();
    }
    public static void saveFactories(List<Factory> factoriesList) throws IOException {
        FileWriter writer = new FileWriter(Main.FACTORIES_FILE_NAME);
        PrintWriter printer = new PrintWriter(writer);

        for(Factory factory : factoriesList) {
            printer.println(factory.getId().toString());
            printer.println(factory.getName());
            printer.println(factory.getAddress().getId().toString());
            List<Long> listItemId = factory.getItems().stream().map(s -> s.getId()).collect(Collectors.toList());
            String itemsInFactory = "";
            for(Long number : listItemId) {
                if(itemsInFactory.isEmpty()){
                    itemsInFactory += number.toString();
                }
                else{
                    itemsInFactory += "," + number.toString();
                }
            }
            printer.println(itemsInFactory);
        }

        writer.flush();
    }

    public static void saveAddress(List<Address> addressList) throws IOException {
        FileWriter writer = new FileWriter(Main.ADDRESSES_FILE_NAME);
        PrintWriter printer = new PrintWriter(writer);

        for(Address address : addressList) {
            printer.println(address.getId().toString());
            switch (address.getCity().getName()) {
                case "ZAGREB":
                    printer.println("1");
                    break;
                case "SPLIT":
                    printer.println("2");
                    break;
                case "DUBROVNIK":
                    printer.println("3");
                    break;
                case "ZADAR":
                    printer.println("4");
                    break;
                case "SIBENIK":
                    printer.println("5");
                    break;
            }
            printer.println(address.getStreet());
            printer.println(address.getHouseNumber());
        }

        writer.flush();
    }
}
