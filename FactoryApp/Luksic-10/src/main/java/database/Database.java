package database;

import hr.java.production.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class Database {
    public static Boolean activeConnectionWithDatabase = false;

    private static List<Category> categoryList = new ArrayList<>();
    private static List<Item> itemList = new ArrayList<>();
    private static List<Factory> factoryList = new ArrayList<>();
    private static List<Store> storeList = new ArrayList<>();
    private static List<Address> addressList = new ArrayList<>();


    private static Factory tvornica;
    private static Store ducan;
    private static Address adresa;

    public static synchronized Connection connectToDatabase() throws SQLException, IOException {
        activeConnectionWithDatabase = true;
        Properties configuration = new Properties();
        configuration.load(new FileReader("dat/database.properties"));

        String databaseURL = configuration.getProperty("databaseURL");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");

        Connection connection = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);

        return connection;
    }

    public static synchronized void disconnectFromDataBase(Connection connection){
        try {
            connection.close();
            activeConnectionWithDatabase = false;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void getAllCategoriesFromDatabase(Connection connection) throws SQLException, IOException {

        List<Category> categoryyList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet categoryResultSet = sqlStatement.executeQuery("SELECT * FROM CATEGORY");

        while(categoryResultSet.next()) {
            Long categoryId = categoryResultSet.getLong("ID");
            String categoryName = categoryResultSet.getString("NAME");
            String categoryDescription = categoryResultSet.getString("DESCRIPTION");

            Category newCategory = new Category(categoryId, categoryName, categoryDescription);

            categoryyList.add(newCategory);
        }

        categoryList = categoryyList;
    }

    public static void getAllItemsFromDatabase(Connection connection) throws SQLException, IOException {
        List<Item> itemsList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet itemResultSet = sqlStatement.executeQuery("SELECT * FROM ITEM");

        while(itemResultSet.next()) {
            Long itemId = itemResultSet.getLong("ID");
            Long itemCategoryId = itemResultSet.getLong("CATEGORY_ID");
            Database.getAllCategoriesFromDatabase(connection);
            Category newCategory = categoryList.get(Math.toIntExact(itemCategoryId)-1);
            String itemName = itemResultSet.getString("NAME");
            BigDecimal itemWidth = itemResultSet.getBigDecimal("WIDTH");
            BigDecimal itemHeight = itemResultSet.getBigDecimal("HEIGHT");
            BigDecimal itemLength = itemResultSet.getBigDecimal("LENGTH");
            BigDecimal itemProductionCost = itemResultSet.getBigDecimal("PRODUCTION_COST");
            BigDecimal itemSellingPrice = itemResultSet.getBigDecimal("SELLING_PRICE");

            Item newItem = new Item(itemId, itemName, newCategory, itemWidth, itemHeight, itemLength,
                    itemProductionCost, itemSellingPrice, new Discount(BigDecimal.ZERO));

            itemsList.add(newItem);
        }

        itemList = itemsList;
    }

    public static void getAllAddressesFromDatabase(Connection connection) throws SQLException, IOException {

        List<Address> addressesList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet addressResultSet = sqlStatement.executeQuery("SELECT * FROM ADDRESS");

        while(addressResultSet.next()) {
            Long addressId = addressResultSet.getLong("ID");
            String street = addressResultSet.getString("STREET");
            String houseNumber = addressResultSet.getString("HOUSE_NUMBER");
            String city = addressResultSet.getString("CITY");
            String postalCode = addressResultSet.getString("POSTAL_CODE");

            Address newAddress = new Address(addressId, street, houseNumber, city, postalCode);

            addressesList.add(newAddress);
        }

        addressList = addressesList;
    }

    public static void getAllFactoriesFromDatabase(Connection connection) throws SQLException, IOException {

        List<Factory> factoriesList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet factoryResultSet = sqlStatement.executeQuery("SELECT * FROM FACTORY");

        while(factoryResultSet.next()) {
            Long factoryId = factoryResultSet.getLong("ID");
            String factoryName= factoryResultSet.getString("NAME");
            String addressId = factoryResultSet.getString("ADDRESS_ID");
            Database.getAllAddressesFromDatabase(connection);
            Address adresa = addressList.get(Integer.parseInt(addressId)-1);

            Factory newFactory = new Factory(factoryId, factoryName, adresa);

            newFactory.setItems(Database.getAllItemsForFactoryFromDatabase(connection, Math.toIntExact(factoryId)));

            factoriesList.add(newFactory);
        }

        factoryList = factoriesList;
    }

    public static void getAllStoresFromDatabase(Connection connection) throws SQLException, IOException {

        List<Store> storesList = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet storeResultSet = sqlStatement.executeQuery("SELECT * FROM STORE");

        while(storeResultSet.next()) {
            Long storeId = storeResultSet.getLong("ID");
            String storeName= storeResultSet.getString("NAME");
            String webAddress= storeResultSet.getString("WEB_ADDRESS");

            Store newStore = new Store(storeId, storeName, webAddress);

            newStore.setItems(Database.getAllItemsForStoreFromDatabase(connection, Math.toIntExact(storeId)));

            storesList.add(newStore);
        }

        storeList = storesList;
    }

    public static Set<Item> getAllItemsForStoreFromDatabase(Connection connection, Integer storeId) throws SQLException, IOException {

        Set<Item> itemSet = new HashSet<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet storeItemResultSet = sqlStatement.executeQuery(
                "SELECT * FROM STORE_ITEM SI, ITEM I WHERE SI.STORE_ID= " + storeId + " AND SI.ITEM_ID = I.ID");

        while(storeItemResultSet.next()) {
            Long itemId = storeItemResultSet.getLong("ID");
            Long itemCategoryId = storeItemResultSet.getLong("CATEGORY_ID");
            Database.getAllCategoriesFromDatabase(connection);
            Category newCategory = categoryList.get(Math.toIntExact(itemCategoryId)-1);
            String itemName = storeItemResultSet.getString("NAME");
            BigDecimal itemWidth = storeItemResultSet.getBigDecimal("WIDTH");
            BigDecimal itemHeight = storeItemResultSet.getBigDecimal("HEIGHT");
            BigDecimal itemLength = storeItemResultSet.getBigDecimal("LENGTH");
            BigDecimal itemProductionCost = storeItemResultSet.getBigDecimal("PRODUCTION_COST");
            BigDecimal itemSellingPrice = storeItemResultSet.getBigDecimal("SELLING_PRICE");

            Item newItem = new Item(itemId, itemName, newCategory, itemWidth, itemHeight, itemLength,
                    itemProductionCost, itemSellingPrice, new Discount(BigDecimal.ZERO));

            itemSet.add(newItem);
        }

        return itemSet;
    }

    public static Set<Item> getAllItemsForFactoryFromDatabase(Connection connection, Integer factoryId) throws SQLException, IOException {

        Set<Item> itemSet = new HashSet<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet factoryItemResultSet = sqlStatement.executeQuery(
                "SELECT * FROM FACTORY_ITEM FI, ITEM I WHERE FI.FACTORY_ID = " + factoryId +" AND FI.ITEM_ID = I.ID");

        while(factoryItemResultSet.next()) {
            Long itemId = factoryItemResultSet.getLong("ID");
            Long itemCategoryId = factoryItemResultSet.getLong("CATEGORY_ID");
            Database.getAllCategoriesFromDatabase(connection);
            Category newCategory = categoryList.get(Math.toIntExact(itemCategoryId)-1);
            String itemName = factoryItemResultSet.getString("NAME");
            BigDecimal itemWidth = factoryItemResultSet.getBigDecimal("WIDTH");
            BigDecimal itemHeight = factoryItemResultSet.getBigDecimal("HEIGHT");
            BigDecimal itemLength = factoryItemResultSet.getBigDecimal("LENGTH");
            BigDecimal itemProductionCost = factoryItemResultSet.getBigDecimal("PRODUCTION_COST");
            BigDecimal itemSellingPrice = factoryItemResultSet.getBigDecimal("SELLING_PRICE");

            Item newItem = new Item(itemId, itemName, newCategory, itemWidth, itemHeight, itemLength,
                    itemProductionCost, itemSellingPrice, new Discount(BigDecimal.ZERO));

            itemSet.add(newItem);
        }

        return itemSet;
    }

    public static void insertNewCategoryToDatabase(Connection connection, Category category) throws SQLException, IOException {

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO CATEGORY(NAME, DESCRIPTION) VALUES(?, ?)");

        stmt.setString(1, category.getName());
        stmt.setString(2, category.getDescription());

        stmt.executeUpdate();

    }

    public static void insertNewItemToDatabase(Connection connection, Item item) throws SQLException, IOException {

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)");

        stmt.setString(1, item.getCategory().getId().toString());
        stmt.setString(2, item.getName());
        stmt.setString(3, item.getWidth().toString());
        stmt.setString(4, item.getHeight().toString());
        stmt.setString(5, item.getLength().toString());
        stmt.setString(6, item.getProductionCost().toString());
        stmt.setString(7, item.getSellingPrice().toString());

        stmt.executeUpdate();

    }

    public static void insertNewAddressToDatabase(Connection connection, Address address) throws SQLException, IOException {

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO ADDRESS(STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) VALUES(?, ?, ?, ?)");

        stmt.setString(1, address.getStreet());
        stmt.setString(2, address.getHouseNumber());
        stmt.setString(3, address.getCityy());
        stmt.setString(4, address.getPostalCode());

        stmt.executeUpdate();

    }

    public static void insertNewFactoryToDatabase(Connection connection, Factory factory) throws SQLException, IOException {

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO FACTORY(NAME, ADDRESS_ID) VALUES(?, ?)");

        stmt.setString(1, factory.getName());
        stmt.setString(2, factory.getAddress().getId().toString());

        stmt.executeUpdate();

    }

    public static void insertNewStoreToDatabase(Connection connection, Store store) throws SQLException, IOException {

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO STORE(NAME, WEB_ADDRESS) VALUES(?, ?)");

        stmt.setString(1, store.getName());
        stmt.setString(2, store.getWebAddress());

        stmt.executeUpdate();

    }

    public static void insertItemsToNewFactoryToDatabase(Connection connection, Factory factory, Item item) throws SQLException, IOException {

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(?, ?)");

        stmt.setString(1, factory.getId().toString());
        stmt.setString(2, item.getId().toString());

        stmt.executeUpdate();

    }

    public static void insertItemsToNewStoreToDatabase(Connection connection, Store store, Item item) throws SQLException, IOException {

        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(?, ?)");

        stmt.setString(1, store.getId().toString());
        stmt.setString(2, item.getId().toString());

        stmt.executeUpdate();

    }

    public static void getFactoryFromDatabase(Connection connection, Factory factory) throws SQLException, IOException {

        Statement sqlStatement = connection.createStatement();

        Factory newFactory = factory;

        ResultSet factoryResultSet = sqlStatement.executeQuery("SELECT * FROM FACTORY WHERE NAME = '" + factory.getName() + "'");

        while(factoryResultSet.next()) {
            Long factoryId = factoryResultSet.getLong("ID");
            String addressId = factoryResultSet.getString("ADDRESS_ID");

            Database.getAllAddressesFromDatabase(connection);
            Address adresa = addressList.get(Integer.parseInt(addressId)-1);

            newFactory.setId(factoryId);
            newFactory.setAddress(adresa);

            newFactory.setItems(Database.getAllItemsForFactoryFromDatabase(connection, Math.toIntExact(factoryId)));
        }

        tvornica = newFactory;
    }

    public static void getStoreFromDatabase(Connection connection, Store store) throws SQLException, IOException {

        Statement sqlStatement = connection.createStatement();

        ResultSet storeResultSet = sqlStatement.executeQuery("SELECT * FROM STORE WHERE NAME = '" + store.getName() + "'");

        Store newStore = store;

        while(storeResultSet.next()) {
            Long storeId = storeResultSet.getLong("ID");
            String webAddress= storeResultSet.getString("WEB_ADDRESS");

            newStore.setId(storeId);
            newStore.setWebAddress(webAddress);

            newStore.setItems(Database.getAllItemsForStoreFromDatabase(connection, Math.toIntExact(storeId)));
        }

        ducan = newStore;
    }

    public static void getAddressFromDatabase(Connection connection, Address address) throws SQLException, IOException {

        Statement sqlStatement = connection.createStatement();

        ResultSet addressResultSet = sqlStatement.executeQuery("SELECT * FROM ADDRESS WHERE STREET = '" + address.getStreet() + "' AND HOUSE_NUMBER = '" + address.getHouseNumber() + "' AND CITY = '" + address.getCityy() + "'");

        Address newAddress = address;

        while(addressResultSet.next()) {
            Long addressId = addressResultSet.getLong("ID");
            newAddress.setId(addressId);
        }

        adresa = newAddress;
    }

    /*public static Category getCategoryFromDatabaseById(Long categoryId) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        Category newCategory = new Category(categoryId);

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM CATEGORY WHERE ID = ?");

        stmt.setLong(1, categoryId);

        ResultSet categoryResultSet = stmt.executeQuery();

        while(categoryResultSet.next()) {
            String categoryName = categoryResultSet.getString("NAME");
            String categoryDescription = categoryResultSet.getString("DESCRIPTION");

            newCategory.setName(categoryName);
            newCategory.setDescription(categoryDescription);
        }

        connection.close();

        return newCategory;
    }

    public static Item getItemFromDatabaseById(Long itemId) throws SQLException, IOException {

        Connection connection = connectToDatabase();

        Item newItem = new Item(itemId);

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ITEM WHERE ID = ?");

        stmt.setLong(1, itemId);

        ResultSet itemResultSet = stmt.executeQuery();

        while(itemResultSet.next()) {
            Long itemCategoryId = itemResultSet.getLong("CATEGORY_ID");
            Category newCategory = getAllCategoriesFromDatabase().get(Math.toIntExact(itemCategoryId)-1);
            String itemName = itemResultSet.getString("NAME");
            BigDecimal itemWidth = itemResultSet.getBigDecimal("WIDTH");
            BigDecimal itemHeight = itemResultSet.getBigDecimal("HEIGHT");
            BigDecimal itemLength = itemResultSet.getBigDecimal("LENGTH");
            BigDecimal itemProductionCost = itemResultSet.getBigDecimal("PRODUCTION_COST");
            BigDecimal itemSellingPrice = itemResultSet.getBigDecimal("SELLING_PRICE");

            newItem.setName(itemName);
            newItem.setCategory(newCategory);
            newItem.setId(itemId);
            newItem.setHeight(itemHeight);
            newItem.setLength(itemLength);
            newItem.setWidth(itemWidth);
            newItem.setProductionCost(itemProductionCost);
            newItem.setSellingPrice(itemSellingPrice);

        }
        connection.close();

        return newItem;
    }

    public static Address getAddressFromDatabaseById(Long addressId) throws SQLException, IOException {
        Connection connection = connectToDatabase();

        Address newAddress = new Address(addressId);

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM ADDRESS WHERE ID = ?");

        stmt.setLong(1, addressId);

        ResultSet addressResultSet = stmt.executeQuery();

        while(addressResultSet.next()) {
            String street = addressResultSet.getString("STREET");
            String houseNumber = addressResultSet.getString("HOUSE_NUMBER");
            String city = addressResultSet.getString("CITY");
            String postalCode = addressResultSet.getString("POSTAL_CODE");

            newAddress.setCityy(city);
            newAddress.setHouseNumber(houseNumber);
            newAddress.setPostalCode(postalCode);
            newAddress.setStreet(street);
        }

        connection.close();

        return newAddress;
    }

    public static Factory getFactoryFromDatabaseById(Long factoryId) throws SQLException, IOException {
        Connection connection = connectToDatabase();

        Factory newFactory = new Factory(factoryId);

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM FACTORY WHERE ID = ?");

        stmt.setLong(1, factoryId);

        ResultSet factoryResultSet = stmt.executeQuery();

        while(factoryResultSet.next()) {
            String factoryName= factoryResultSet.getString("NAME");
            String addressId = factoryResultSet.getString("ADDRESS_ID");
            Address adresa = getAllAddressesFromDatabase().get(Integer.parseInt(addressId)-1);

            newFactory.setName(factoryName);
            newFactory.setAddress(adresa);
            newFactory.setItems(getAllItemsForFactoryFromDatabase(Math.toIntExact(factoryId)));
        }

        connection.close();

        return newFactory;
    }

    public static Store getStoreFromDatabaseById(Long storeId) throws SQLException, IOException {
        Connection connection = connectToDatabase();

        Store newStore = new Store(storeId);

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM STORE WHERE ID = ?");

        stmt.setLong(1, storeId);

        ResultSet storeResultSet = stmt.executeQuery();

        while(storeResultSet.next()) {
            String storeName= storeResultSet.getString("NAME");
            String webAddress= storeResultSet.getString("WEB_ADDRESS");

            newStore.setName(storeName);
            newStore.setWebAddress(webAddress);
            newStore.setItems(getAllItemsForStoreFromDatabase(Math.toIntExact(storeId)));
        }

        connection.close();

        return newStore;
    }*/

    public static List<Category> getCategoryList() {
        return categoryList;
    }

    public static List<Item> getItemList() {
        return itemList;
    }

    public static List<Factory> getFactoryList() {
        return factoryList;
    }

    public static List<Store> getStoreList() {
        return storeList;
    }

    public static List<Address> getAddressList() {
        return addressList;
    }

    public static Factory getTvornica() {
        return tvornica;
    }

    public static Store getDucan() {
        return ducan;
    }

    public static Address getAdresa() {
        return adresa;
    }
}
