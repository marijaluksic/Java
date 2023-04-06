package hr.java.production.main;

import hr.java.production.enumeracija.AvailableCities;
import hr.java.production.exception.ArticleSelectorException;
import hr.java.production.genericsi.FoodStore;
import hr.java.production.genericsi.TechnicalStore;
import hr.java.production.model.*;
import hr.java.production.sort.VolumeCompare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sluzi za pokretanje aplikacije.
 */

public class Main {
    /**
     * konstanta koja odreduje broj razlicitih kategorija
     */
    //private static final int NUMBER_OF_CATEGORIES = 3;
    /**
     * konstanta koja odreduje broj artikala
     */
   //private static final int NUMBER_OF_ITEMS = 5;
    /**
     * konstanta koja odreduje broj tvornica
     */
    //private static final int NUMBER_OF_FACTORIES = 2;
    /**
     * konstanta koja odreduje broj ducana
     */
    //private static final int NUMBER_OF_STORES = 2;
    /**
     * konstanta koja odreduje lokaciju i naziv datoteke s adresama
     */
    public static final String ADDRESSES_FILE_NAME = "dat/addresses.txt";
    private static final Integer NUMBER_OF_LINES_FOR_ADDRESS = 4;
    /**
     * konstanta koja odreduje lokaciju i naziv datoteke s kategorijama
     */
    public static final String CATEGORIES_FILE_NAME = "dat/categories.txt";
    private static final Integer NUMBER_OF_LINES_FOR_CATEGORIES = 3;
    /**
     * konstanta koja odreduje lokaciju i naziv datoteke s tvornicama
     */
    public static final String FACTORIES_FILE_NAME = "dat/factories.txt";
    private static final Integer NUMBER_OF_LINES_FOR_FACTORIES = 4;
    /**
     * konstanta koja odreduje lokaciju i naziv datoteke s artiklima
     */
    public static final String ITEMS_FILE_NAME = "dat/items.txt";
    private static final Integer NUMBER_OF_LINES_FOR_ITEMS = 9;
    /**
     * konstanta koja odreduje lokaciju i naziv datoteke s ducanima
     */
    public static final String STORES_FILE_NAME = "dat/stores.txt";
    private static final Integer NUMBER_OF_LINES_FOR_STORES = 4;



    private static final String STORES_SERIALIZATION_FILE_NAME = "dat/stores.ser";
    private static final String FACTORIES_SERIALIZATION_FILE_NAME = "dat/factories.ser";

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss");



    /**
     * sluzi za zapisivanje informacija i mogucih gresaka prilikom korisnickog unosa
     */
    private static final Logger logger = LoggerFactory.getLogger(hr.java.production.main.Main.class);

    /**
     * main funkcija
     *
     * @param args ulazni argumenti main funkcije
     */

    public static void main(String[] args) throws IOException {
        //logger.info("Example log from {}", Main.class.getSimpleName());

        //Scanner input = new Scanner(System.in);



        Optional<List<Category>> categories = fetchCategoriesFromList();
        if (categories.isPresent()) {
            System.out.println("Lista kategorija:");
            categories.get().forEach(System.out::println);
        } else {
            System.out.println("There was no category data in file " + CATEGORIES_FILE_NAME);
        }
        Optional<List<Item>> items = fetchItemFromList(categories);
        if (items.isPresent()) {
            System.out.println("Lista artikala:");
            items.get().forEach(System.out::println);
        } else {
            System.out.println("There was no item data in file " + ITEMS_FILE_NAME);
        }

        Optional<List<Address>> addresses = fetchAddressesFromList();
        if (addresses.isPresent()) {
            System.out.println("Lista tvornica:");
            addresses.get().forEach(System.out::println);
        } else {
            System.out.println("There was no address data in file " + ADDRESSES_FILE_NAME);
        }
        Optional<List<Factory>> factories = fetchFactoriesFromList(items, addresses);
        if (factories.isPresent()) {
            System.out.println("Lista tvornica:");
            factories.get().forEach(System.out::println);
        } else {
            System.out.println("There was no factory data in file " + FACTORIES_FILE_NAME);
        }
        Optional<List<Store>> stores = fetchStoresFromList(items);
        if (stores.isPresent()) {
            System.out.println("Lista ducana:");
            stores.get().forEach(System.out::println);
        } else {
            System.out.println("There was no store data in file " + STORES_FILE_NAME);
        }


        TechnicalStore<Technical> tehnickiDucan = new TechnicalStore<Technical>(((long) stores.get().size() + 1),
                "Technical Store", "www.technicalstore.com",
                insertNewSet(Technical.class.getSimpleName(), items.get()), insertNewTechnicalList(items.get()));
        FoodStore<Edible> prehrambeniDucan = new FoodStore<Edible>(((long) stores.get().size() + 2), "Food Store",
                "www.foodstore.com", insertNewSet(Edible.class.getSimpleName(), items.get()),
                insertNewFoodList(items.get()));

        stores.get().add(tehnickiDucan);
        stores.get().add(prehrambeniDucan);

        List<Item> listaSvihArtikalaIzSvihDucana = new ArrayList<>();
        for (Store ducan : stores.get()) {
            List<Item> listaSvihArtikalaIzJednogDucana = ducan.getItems().stream().toList();
            for (Item artikl : listaSvihArtikalaIzJednogDucana) {
                listaSvihArtikalaIzSvihDucana.add(artikl);
            }
        }
        Instant separateStart = Instant.now();
        ispisSLambdama(listaSvihArtikalaIzSvihDucana, stores);
        Instant separateEnd = Instant.now();
        System.out.printf("Ukupno vrijeme izvršavanja operacija koristeći lambda izraze u nanosekundama: %d%n%n",
                Duration.between(separateStart, separateEnd).toNanos());
        Instant separateStart2 = Instant.now();
        ispisBezLambdi(listaSvihArtikalaIzSvihDucana, stores);
        Instant separateEnd2 = Instant.now();
        System.out.printf("Ukupno vrijeme izvršavanja operacija bez korištenja lambda izraza u nanosekundama: %d%n%n",
                Duration.between(separateStart2, separateEnd2).toNanos());
        int tkoJeBrzi = (Duration.between(separateStart, separateEnd)).
                compareTo(Duration.between(separateStart2, separateEnd2));
        if (tkoJeBrzi < 0) {
            System.out.println("Korištenje lambda izraza je brže!");
        } else if (tkoJeBrzi > 0) {
            System.out.println("Korištenje lambda izraza je sporije!");
        }

        Optional<Item> artikliSPopustomVecimOdNula = items.get().stream()
                .filter(artikl -> (artikl.getDiscount().discountAmount().compareTo(BigDecimal.valueOf(0)) > 0))
                .findAny();

        if (artikliSPopustomVecimOdNula.isPresent()) {
            System.out.println("Artikli na popustu:");
            items.get().stream()
                    .filter(artikl -> (artikl.getDiscount().discountAmount().compareTo(BigDecimal.valueOf(0)) > 0))
                    .forEach(System.out::println);
        } else {
            System.out.println("Niti jedan artikl nije na popustu, više sreće drugi put!");
        }
        stores.get().stream()
                .map(trgovina -> "Naziv trgovine: " + trgovina.getName() + "    Broj artikala: " +
                        trgovina.getItems().size())
                .forEach(System.out::println);


        if(stores.isPresent()) {
            try(ObjectOutputStream objectWriter
                        = new ObjectOutputStream(new FileOutputStream(STORES_SERIALIZATION_FILE_NAME))) {
                List<Store> ducaniSViseOdPetArtikala = stores.get().stream()
                        .filter(d -> d.getItems().size()>=5)
                        .collect(Collectors.toList());
                objectWriter.writeObject(ducaniSViseOdPetArtikala);
                }
            catch (IOException e) {
                e.printStackTrace();
            }

            try(ObjectInputStream objectReader
                        = new ObjectInputStream(new FileInputStream(STORES_SERIALIZATION_FILE_NAME))) {
                List<Store> deserializedStoreList = (List<Store>) objectReader.readObject();
                System.out.println("Deserialized stores:");
                deserializedStoreList.forEach(System.out::println);
            } catch(IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(factories.isPresent()) {
            try(ObjectOutputStream objectWriter
                        = new ObjectOutputStream(new FileOutputStream(FACTORIES_SERIALIZATION_FILE_NAME))) {
                List<Factory> tvorniceSViseOdPetArtikala = factories.get().stream()
                        .filter(d -> d.getItems().size()>=5)
                        .collect(Collectors.toList());
                objectWriter.writeObject(tvorniceSViseOdPetArtikala);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try(ObjectInputStream objectReader
                        = new ObjectInputStream(new FileInputStream(FACTORIES_SERIALIZATION_FILE_NAME))) {
                List<Factory> deserializedFactoryList = (List<Factory>) objectReader.readObject();
                System.out.println("Deserialized factories:");
                deserializedFactoryList.forEach(System.out::println);
            } catch(IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
/*
        List<Item> listaArtikala = new ArrayList<>();
        List<Item> listaTechnicalEdible = new ArrayList<>();
        //List<Item> listaTechnical = new ArrayList<>();

        for(Item artikl : items) {
            listaArtikala.add(artikl);
            if((artikl.getCategory().getName().equalsIgnoreCase(Technical.class.getSimpleName())) ||
                    (artikl.getCategory().getName().equalsIgnoreCase(Edible.class.getSimpleName()))) {
                listaTechnicalEdible.add(artikl);
                if(artikl.getCategory().getName().equalsIgnoreCase(Technical.class.getSimpleName())) {
                    listaTechnicalEdible.add(artikl);
                }
            }
        }

        pumpkinCalculatorDeluxe(listaArtikala);
        shortestWarranty(listaArtikala);
// SET ne podrzava duplikate, no provjera za iste kao i odgovarajuci exception ipak daju korisniku povratnu informaciju
// o svom izboru te mogucnost ponovnog unosa ukoliko zeli otkloniti gresku
        for (int i = 0; i < NUMBER_OF_FACTORIES; i++) {
            factories[i] = insertNewFactory(input, i, listaArtikala);
        }

        input.nextLine();

        for (int i = 0; i < NUMBER_OF_STORES; i++) {
            stores[i] = insertNewStore(input, i, listaArtikala);
        }

        factoryItemMaxVolume(factories);

        storeItemMinSellingPrice(stores);

        Collections.sort(listaArtikala, new ProductionSorter());
        Map<Category, List<Item>> articleMap = new HashMap<>();
        Map<Category, List<Item>> edibleTechnicalMap = new HashMap<>();

        for(Item artikl : listaArtikala) {
            if(articleMap.containsKey(artikl.getCategory())) {
                List<Item> listaArtikalaUKategoriji = articleMap.get(artikl.getCategory());
                listaArtikalaUKategoriji.add(artikl);
                articleMap.put(artikl.getCategory(), listaArtikalaUKategoriji);
            }
            else {
                List<Item> artikliUNovojKategoriji = new ArrayList<>();
                artikliUNovojKategoriji.add(artikl);
                articleMap.put(artikl.getCategory(), artikliUNovojKategoriji);
            }
        }

        for (Map.Entry<Category,List<Item>> entry : articleMap.entrySet()) {
            System.out.println("Kategorija = " + entry.getKey().getName() +
                    "\nNajjeftiniji artikl = " + entry.getValue().get(0).getName() + "    Cijena: " +
                    entry.getValue().get(0).getSellingPrice() + "kn"+
                    "\nNajskuplji artikl = " + entry.getValue().get(entry.getValue().size() - 1).getName() +
                    "    Cijena: " + entry.getValue().get(entry.getValue().size() - 1).getSellingPrice() + "kn");
        }

        for(Item artikl : listaTechnicalEdible) {
            if(edibleTechnicalMap.containsKey(artikl.getCategory())) {
                List<Item> listaArtikalaUKategoriji = edibleTechnicalMap.get(artikl.getCategory());
                listaArtikalaUKategoriji.add(artikl);
                edibleTechnicalMap.put(artikl.getCategory(), listaArtikalaUKategoriji);
            }
            else {
                List<Item> artikliUNovojKategoriji = new ArrayList<>();
                artikliUNovojKategoriji.add(artikl);
                edibleTechnicalMap.put(artikl.getCategory(), artikliUNovojKategoriji);
            }
        }
        for (Map.Entry<Category,List<Item>> entry : edibleTechnicalMap.entrySet()) {
            System.out.println("Kategorija = " + entry.getKey().getName() +
                    "\nNajjeftiniji artikl = " + entry.getValue().get(0).getName() + "    Cijena: " +
                    entry.getValue().get(0).getSellingPrice() + "kn"+
                    "\nNajskuplji artikl = " + entry.getValue().get(entry.getValue().size() - 1).getName() +
                    "    Cijena: " + entry.getValue().get(entry.getValue().size() - 1).getSellingPrice() + "kn");
        }
//PRIKAZ MOGUCNOSTI SILAZNOG SORTIRANJA

//        Collections.sort(listaArtikala, Collections.reverseOrder(new ProductionSorter()));
//        Map<Category, List<Item>> articleMapReverse = new HashMap<>();
//
//        for(Item artikl : listaArtikala) {
//            if(articleMapReverse.containsKey(artikl.getCategory())) {
//                List<Item> listaArtikalaUKategoriji = articleMapReverse.get(artikl.getCategory());
//                listaArtikalaUKategoriji.add(artikl);
//                articleMapReverse.put(artikl.getCategory(), listaArtikalaUKategoriji);
//            }
//            else {
//                List<Item> artikliUNovojKategoriji = new ArrayList<>();
//                artikliUNovojKategoriji.add(artikl);
//                articleMapReverse.put(artikl.getCategory(), artikliUNovojKategoriji);
//            }
//        }
//        System.out.println(articleMapReverse);
//        for (Map.Entry<Category,List<Item>> entry : articleMapReverse.entrySet()) {
//            System.out.println("Kategorija = " + entry.getKey().getName() +
//                    "\nNajskuplji artikl = " + entry.getValue().get(0).getName() + "    Cijena: " +
//                    entry.getValue().get(0).getSellingPrice() + "kn"+
//                    "\nNajjeftiniji artikl = " + entry.getValue().get(entry.getValue().size() - 1).getName() +
//                    "    Cijena: " + entry.getValue().get(entry.getValue().size() - 1).getSellingPrice() + "kn");
//        }

        TechnicalStore<Technical> tehnickiDucan = new TechnicalStore<Technical>("Technical Store",
                "www.technicalstore.com", insertNewSet(Technical.class.getSimpleName(), listaArtikala),
                insertNewTechnicalList(listaArtikala));
        FoodStore<Edible> prehrambeniDucan = new FoodStore<Edible>("Food Store", "www.foodstore.com",
                insertNewSet(Edible.class.getSimpleName(), listaArtikala), insertNewFoodList(listaArtikala));

        Store[] allStores = new Store[NUMBER_OF_STORES + 2];
        for(int i = 0; i < NUMBER_OF_STORES + 2; i++) {
            if(i == NUMBER_OF_STORES){
                allStores[i] = tehnickiDucan;
            }
            else if(i == (NUMBER_OF_STORES + 1)){
                allStores[i] = prehrambeniDucan;
            }
            else {
                allStores[i] = stores[i];
            }
        }
        List<Item> listaSvihArtikalaIzSvihDucana = new ArrayList<>();
        for(Store ducan : allStores) {
            List<Item> listaSvihArtikalaIzJednogDucana = ducan.getItems().stream().toList();
            for(Item artikl : listaSvihArtikalaIzJednogDucana) {
                listaSvihArtikalaIzSvihDucana.add(artikl);
            }
        }
        List<Store> listaSvihTrgovina = Arrays.stream(allStores).toList();
        Instant separateStart = Instant.now();
        ispisSLambdama(listaSvihArtikalaIzSvihDucana, allStores, listaSvihTrgovina);
        Instant separateEnd = Instant.now();
        System.out.printf("Ukupno vrijeme izvršavanja operacija koristeći lambda izraze u nanosekundama: %d%n%n",
                Duration.between(separateStart, separateEnd).toNanos());
        Instant separateStart2 = Instant.now();
        ispisBezLambdi(listaSvihArtikalaIzSvihDucana, allStores, listaSvihTrgovina);
        Instant separateEnd2 = Instant.now();
        System.out.printf("Ukupno vrijeme izvršavanja operacija bez korištenja lambda izraza u nanosekundama: %d%n%n",
                Duration.between(separateStart2, separateEnd2).toNanos());
        int tkoJeBrzi = (Duration.between(separateStart, separateEnd)).
                compareTo(Duration.between(separateStart2, separateEnd2));
        if(tkoJeBrzi < 0) {
            System.out.println("Korištenje lambda izraza je brže!");
        }
        else if(tkoJeBrzi > 0) {
            System.out.println("Korištenje lambda izraza je sporije!");
        }

        Optional<Item> artikliSPopustomVecimOdNula = listaArtikala.stream()
                .filter(artikl -> (artikl.getDiscount().discountAmount().compareTo(BigDecimal.valueOf(0)) > 0))
                .findAny();

        if(artikliSPopustomVecimOdNula.isPresent()){
            System.out.println("Artikli na popustu:");
            listaArtikala.stream()
                    .filter(artikl -> (artikl.getDiscount().discountAmount().compareTo(BigDecimal.valueOf(0)) > 0))
                    .forEach(System.out::println);
        }
        else{
            System.out.println("Niti jedan artikl nije na popustu, više sreće drugi put!");
        }
        listaSvihTrgovina.stream()
                .map(trgovina -> "Naziv trgovine: " + trgovina.getName() + "    Broj artikala: " +
                        trgovina.getItems().size())
                .forEach(System.out::println);*/
    }

    /**
     * cita podatke o trgovinama iz datoteke
     * @param items popis artikala
     * @return popis trgovina
     */
    public static Optional<List<Store>> fetchStoresFromList(Optional<List<Item>> items) {
        List<Store> storesList = new ArrayList<>();
        Set<Item> chosenItems = new HashSet<>();

        File storesFile = new File(STORES_FILE_NAME);

        try (BufferedReader storesFileReader = new BufferedReader(new FileReader(storesFile))) {

            String line;
            Integer lineCounter = 1;

            Optional<Long> id = Optional.empty();
            Optional<String> name = Optional.empty();
            Optional<String> webAddress = Optional.empty();

            while ((line = storesFileReader.readLine()) != null) {
                if (lineCounter % NUMBER_OF_LINES_FOR_STORES == 1) {
                    id = Optional.of(Long.parseLong(line));
                } else if (lineCounter % NUMBER_OF_LINES_FOR_STORES == 2) {
                    name = Optional.of(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_STORES == 3) {
                    webAddress = Optional.of(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_STORES == 0) {
                    String[] results = line.split(",");
                    for (String odabir : results) {
                        chosenItems.add(items.get().stream().filter(i -> i.getId() == Long.parseLong(odabir)).
                                findFirst().get());
                    }
                    storesList.add(new Store(id.get(), name.get(), webAddress.get(), chosenItems));
                    chosenItems = new HashSet<>();
                }
                lineCounter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (storesList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(storesList);
        }
    }

    /**
     * cita podatke o tvornicama iz datoteke
     * @param items popis artikala
     * @param addresses popis adresa
     * @return popis tvornica
     */
    public static Optional<List<Factory>> fetchFactoriesFromList(Optional<List<Item>> items,
                                                                 Optional<List<Address>> addresses) {
        List<Factory> factoriesList = new ArrayList<>();
        Set<Item> chosenItems = new HashSet<>();

        File factoriesFile = new File(FACTORIES_FILE_NAME);

        try (BufferedReader factoriesFileReader = new BufferedReader(new FileReader(factoriesFile))) {

            String line;
            Integer lineCounter = 1;

            Optional<Long> id = Optional.empty();
            Optional<String> name = Optional.empty();
            Optional<Address> address = Optional.empty();


            while ((line = factoriesFileReader.readLine()) != null) {
                if (lineCounter % NUMBER_OF_LINES_FOR_FACTORIES == 1) {
                    id = Optional.of(Long.parseLong(line));
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORIES == 2){
                    name = Optional.of(line);
                }
                else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORIES == 3) {
                    String finalLine = line;
                    address = Optional.of(addresses.get().stream().filter(i -> i.getId() == Long.parseLong(finalLine)).
                            findFirst().get());
                } else if (lineCounter % NUMBER_OF_LINES_FOR_FACTORIES == 0) {
                    String[] results = line.split(",");
                    for (String odabir : results) {
                        chosenItems.add(items.get().stream().filter(i -> i.getId() == Long.parseLong(odabir)).
                                findFirst().get());
                    }
                    factoriesList.add(new Factory(id.get(), name.get(), address.get(), chosenItems));
                    chosenItems = new HashSet<>();
                }

                lineCounter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (factoriesList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(factoriesList);
        }
    }

    /**
     * cita podatke o adresama iz datoteke
     * @return vraca popis adresa
     */
    public static Optional<List<Address>> fetchAddressesFromList() {
        List<Address> addressList = new ArrayList<>();

        File addressFile = new File(ADDRESSES_FILE_NAME);

        try (BufferedReader addressesFileReader = new BufferedReader(new FileReader(addressFile))) {

            String line;
            Integer lineCounter = 1;

            Address.Builder addressBuilder = new Address.Builder();

            while ((line = addressesFileReader.readLine()) != null) {
                if(lineCounter % NUMBER_OF_LINES_FOR_ADDRESS == 1){
                    addressBuilder.withId(Long.parseLong(line));
                }
                else if (lineCounter % NUMBER_OF_LINES_FOR_ADDRESS == 2) {
                    switch (Integer.valueOf(line)) {
                        case 1:
                            addressBuilder.atCityWithPostalCode(AvailableCities.ZAGREB);
                            break;
                        case 2:
                            addressBuilder.atCityWithPostalCode(AvailableCities.SPLIT);
                            break;
                        case 3:
                            addressBuilder.atCityWithPostalCode(AvailableCities.DUBROVNIK);
                            break;
                        case 4:
                            addressBuilder.atCityWithPostalCode(AvailableCities.ZADAR);
                            break;
                        case 5:
                            addressBuilder.atCityWithPostalCode(AvailableCities.SIBENIK);
                            break;
                    }
                } else if (lineCounter % NUMBER_OF_LINES_FOR_ADDRESS == 3) {
                    addressBuilder.atStreet(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_ADDRESS == 0) {
                    addressBuilder.atHouseNumber(line);
                    Address newAddress = addressBuilder.build();
                    addressList.add(newAddress);
                    addressBuilder = new Address.Builder();
                }
                lineCounter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(addressList);
        }
    }

    public static Optional<List<Item>> fetchItemsFromList(Optional<List<Category>> categories) {
        List<Item> itemsList = new ArrayList<>();

        File itemsFile = new File(ITEMS_FILE_NAME);

        try (BufferedReader itemsFileReader = new BufferedReader(new FileReader(itemsFile))) {

            String line;
            Integer lineCounter = 1;

            Optional<Long> id = Optional.empty();
            Optional<String> name = Optional.empty();
            Optional<Category> category = Optional.empty();
            Optional<BigDecimal> width = Optional.empty();
            Optional<BigDecimal> height = Optional.empty();
            Optional<BigDecimal> length = Optional.empty();
            Optional<BigDecimal> productionCost = Optional.empty();
            Optional<BigDecimal> sellingPrice = Optional.empty();
            Optional<Discount> discount = Optional.empty();
            Optional<BigDecimal> weight = Optional.empty();
            Optional<Integer> warranty = Optional.empty();


            while ((line = itemsFileReader.readLine()) != null) {
                    if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 1) {
                        Long categoryNumber = Long.parseLong(line);
                        category = categories.get().stream().filter(s -> s.getId() == categoryNumber).findFirst();
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 2) {
                        id = Optional.of(Long.parseLong(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 3) {
                        name = Optional.of(line);
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 4) {
                        width = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 5) {
                        height = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 6) {
                        length = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 7) {
                        productionCost = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 8) {
                        sellingPrice = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 0) {
                        discount = Optional.of(new Discount(new BigDecimal(line)));
                        BigDecimal hundred = new BigDecimal("100");
                        BigDecimal priceWithDiscount = hundred.subtract(discount.get().discountAmount());
                        priceWithDiscount = priceWithDiscount.divide(hundred);
                        sellingPrice = Optional.of(priceWithDiscount.multiply(sellingPrice.get()));
                        if (category.get().getId() == 1) {
                            weight = Optional.of(new BigDecimal(1));
                            BigDecimal testPrice = weight.get().multiply(sellingPrice.get());
                            if (name.get().equals(Hokkaido.class.getSimpleName())) {
                                itemsList.add(new Hokkaido(id.get(), name.get(), width.get(), height.get(),
                                        length.get(), productionCost.get(), testPrice, weight.get(), discount.get()));
                            } else if (name.get().equals(Butternut.class.getSimpleName())) {
                                itemsList.add(new Butternut(id.get(), name.get(), width.get(), height.get(),
                                        length.get(), productionCost.get(), testPrice, weight.get(), discount.get()));
                            }
                            else {
                                itemsList.add(new Hokkaido(id.get(), name.get(), width.get(), height.get(),
                                        length.get(), productionCost.get(), testPrice, weight.get(), discount.get()));
                            }
                        } else if (category.get().getId() == 2) {
                            warranty = Optional.of(1);
                            itemsList.add(new Laptop(id.get(), name.get(), width.get(), height.get(),
                                    length.get(), productionCost.get(), sellingPrice.get(), discount.get(), warranty.get()));
                        } else {
                            itemsList.add(new Item(id.get(), name.get(), category.get(), width.get(), height.get(),
                                    length.get(), productionCost.get(), sellingPrice.get(), discount.get()));
                        }
                    }

                lineCounter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (itemsList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(itemsList);
        }
    }

    /**
     * cita podatke o artiklima iz datoteke
     * @param categories popis kategorija
     * @return vraca popis artikala
     */
    public static Optional<List<Item>> fetchItemFromList(Optional<List<Category>> categories) {
        List<Item> itemsList = new ArrayList<>();

        File itemsFile = new File(ITEMS_FILE_NAME);

        try (BufferedReader itemsFileReader = new BufferedReader(new FileReader(itemsFile))) {

            String line;
            Integer lineCounter = 1;

            Optional<Long> id = Optional.empty();
            Optional<String> name = Optional.empty();
            Optional<Category> category = Optional.empty();
            Optional<BigDecimal> width = Optional.empty();
            Optional<BigDecimal> height = Optional.empty();
            Optional<BigDecimal> length = Optional.empty();
            Optional<BigDecimal> productionCost = Optional.empty();
            Optional<BigDecimal> sellingPrice = Optional.empty();
            Optional<Discount> discount = Optional.empty();
            Optional<BigDecimal> weight = Optional.empty();
            Optional<Integer> warranty = Optional.empty();
            Integer pumpkinPicker = 0;
            Integer laptopPicker = 0;


            while ((line = itemsFileReader.readLine()) != null) {
                if (pumpkinPicker == 1) {
                    weight = Optional.of(new BigDecimal(line));
                    pumpkinPicker = 0;
                    BigDecimal testPrice = weight.get().multiply(sellingPrice.get());
                    if (name.get().equals(Hokkaido.class.getSimpleName())) {
                        itemsList.add(new Hokkaido(id.get(), name.get(), width.get(), height.get(),
                                length.get(), productionCost.get(), testPrice, weight.get(), discount.get()));
                    } else if (name.get().equals(Butternut.class.getSimpleName())) {
                        itemsList.add(new Butternut(id.get(), name.get(), width.get(), height.get(),
                                length.get(), productionCost.get(), testPrice, weight.get(), discount.get()));
                    }
                } else if (laptopPicker == 1) {
                    warranty = Optional.of(Integer.valueOf(line));
                    laptopPicker = 0;
                    itemsList.add(new Laptop(id.get(), name.get(), width.get(), height.get(),
                            length.get(), productionCost.get(), sellingPrice.get(), discount.get(), warranty.get()));
                } else {
                    if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 1) {
                        if (line.equals("1")) {
                            category = categories.get().stream().filter(s -> s.getId() == 1).findFirst();
                        } else if (line.equals("2")) {
                            category = categories.get().stream().filter(s -> s.getId() == 2).findFirst();
                        } else if (line.equals("3")) {
                            category = categories.get().stream().filter(s -> s.getId() == 3).findFirst();
                        }
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 2) {
                        id = Optional.of(Long.parseLong(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 3) {
                        name = Optional.of(line);
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 4) {
                        width = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 5) {
                        height = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 6) {
                        length = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 7) {
                        productionCost = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 8) {
                        sellingPrice = Optional.of(new BigDecimal(line));
                    } else if (lineCounter % NUMBER_OF_LINES_FOR_ITEMS == 0) {
                        discount = Optional.of(new Discount(new BigDecimal(line)));
                        BigDecimal hundred = new BigDecimal("100");
                        BigDecimal priceWithDiscount = hundred.subtract(discount.get().discountAmount());
                        priceWithDiscount = priceWithDiscount.divide(hundred);
                        sellingPrice = Optional.of(priceWithDiscount.multiply(sellingPrice.get()));
                        if (category.get().getId() == 1) {
                            pumpkinPicker = 1;
                            lineCounter--;
                        } else if (category.get().getId() == 2) {
                            laptopPicker = 1;
                            lineCounter--;
                        } else if (category.get().getId() == 3) {
                            itemsList.add(new Item(id.get(), name.get(), category.get(), width.get(), height.get(),
                                    length.get(), productionCost.get(), sellingPrice.get(), discount.get()));
                        }
                    }
                }
                lineCounter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (itemsList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(itemsList);
        }
    }

    /**
     * cita podatke o kategorijama iz datoteke
     * @return vraca popis kategorija
     */
    public static Optional<List<Category>> fetchCategoriesFromList() {
        List<Category> categoriesList = new ArrayList<>();

        File categoryFile = new File(CATEGORIES_FILE_NAME);

        Optional<Long> id = Optional.empty();
        Optional<String> name = Optional.empty();
        Optional<String> description = Optional.empty();

        try (BufferedReader categoriesFileReader = new BufferedReader(new FileReader(categoryFile))) {

            String line;
            Integer lineCounter = 1;

            while ((line = categoriesFileReader.readLine()) != null) {
                if (lineCounter % NUMBER_OF_LINES_FOR_CATEGORIES == 1) {
                    id = Optional.of(Long.parseLong(line));
                } else if (lineCounter % NUMBER_OF_LINES_FOR_CATEGORIES == 2) {
                    name = Optional.of(line);
                } else if (lineCounter % NUMBER_OF_LINES_FOR_CATEGORIES == 0) {
                    description = Optional.of(line);
                    categoriesList.add(new Category(id.get(), name.get(), description.get()));
                }
                lineCounter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (categoriesList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(categoriesList);
        }
    }

    /**
     * izvrsava odredene kalkulacije bez koristenja lambdi
     * @param listaSvihArtikalaIzSvihDucana popis svih artiklaa iz svih ducana
     * @param stores popis svih ducana
     */
    public static void ispisBezLambdi(List<Item> listaSvihArtikalaIzSvihDucana, Optional<List<Store>> stores) {

        Collections.sort(listaSvihArtikalaIzSvihDucana, new VolumeCompare());
        System.out.println("Artikli sortirani prema volumenu:");
        for (Item artikl : listaSvihArtikalaIzSvihDucana) {
            System.out.println("Naziv: " + artikl.getName() + "    Volumen: " +
                    (artikl.getHeight()).multiply(artikl.getLength()).multiply(artikl.getWidth()));
        }

        Double ukupniVolumen = Double.valueOf(0);
        Integer brojArtikala = listaSvihArtikalaIzSvihDucana.size();

        for (Item artikl : listaSvihArtikalaIzSvihDucana) {
            ukupniVolumen += artikl.getHeight().doubleValue() * artikl.getLength().doubleValue() *
                    artikl.getWidth().doubleValue();
        }
        Double averagevolume = ukupniVolumen / brojArtikala;
        Double srednjaCijenaSvihArtikala = Double.valueOf(0);
        Integer srednjaCijenaBrojArtikala = 0;
        for (Item artikl : listaSvihArtikalaIzSvihDucana) {
            if (artikl.getHeight().doubleValue() * artikl.getLength().doubleValue() * artikl.getWidth().doubleValue()
                    > averagevolume) {
                srednjaCijenaSvihArtikala += artikl.getSellingPrice().doubleValue();
                srednjaCijenaBrojArtikala += 1;
            }
        }
        System.out.println("Srednja cijena svih artikala sa natprosječnim volumenom: " +
                srednjaCijenaSvihArtikala / srednjaCijenaBrojArtikala);

        int ukupanBrojArtikalaPoDucanu = 0;
        for (Store ducan : stores.get()) {
            ukupanBrojArtikalaPoDucanu += (ducan.getItems().size());
        }
        double prosjecnaKolicinaArtikala = ukupanBrojArtikalaPoDucanu / stores.get().size();
        System.out.println("Trgovine s natprosječnim brojem artikala:");
        for (Store trgovina : stores.get()) {
            if (trgovina.getItems().size() > prosjecnaKolicinaArtikala) {
                System.out.println("Naziv: " + trgovina.getName() + "    Web adresa: " + trgovina.getWebAddress());
            }
        }
    }

    /**
     * izvrsava odredene kalkulacije uz koristenje lambdi
     * @param listaSvihArtikalaIzSvihDucana popis svih artiklaa iz svih ducana
     * @param stores popis svih ducana
     */
    public static void ispisSLambdama(List<Item> listaSvihArtikalaIzSvihDucana, Optional<List<Store>> stores) {
        List<Item> sortiranaListaSvihArtikalaIzSvihDucana = listaSvihArtikalaIzSvihDucana.stream()
                .sorted((a, b) -> {
                    int comparator = ((a.getHeight()).multiply(a.getLength()).
                            multiply(a.getWidth())).compareTo((b.getHeight()).
                            multiply(b.getLength()).multiply(b.getWidth()));
                    return comparator;
                })
                .collect(Collectors.toList());
        System.out.println("Artikli sortirani prema volumenu:");
        sortiranaListaSvihArtikalaIzSvihDucana.stream().forEach(a -> System.out.println("Naziv: " + a.getName() +
                "    Volumen: " + (a.getHeight()).multiply(a.getLength()).multiply(a.getWidth())));
        //sortiranaListaSvihArtikalaIzSvihDucana.stream().forEach(h -> System.out.println(h.toString()));

//       List<Item> volumenSort = new ArrayList<>();
//        for(Store ducan : allStores) {
//            volumenSort = ducan.getItems().stream()
//                    .sorted((a, b) -> {
//                        int comparator = ((a.getHeight()).multiply(a.getLength()).
//                                multiply(a.getWidth())).compareTo((b.getHeight()).
//                                multiply(b.getLength()).multiply(b.getWidth()));
//                        return comparator;})
//                    .collect(Collectors.toList());
//            System.out.println("Artikli sortirani prema volumenu za dućan naziva " + ducan.getName() + ":\n");
//            volumenSort.forEach(artikl -> System.out.println("Naziv artikla: " + artikl.getName() + "    Volumen: " +
//                    (artikl.getHeight()).multiply(artikl.getLength()).multiply(artikl.getWidth())));
//        }
        Double ukupniVolumen = Double.valueOf(0);
        Integer brojArtikala = listaSvihArtikalaIzSvihDucana.size();
        for (Item artikl : listaSvihArtikalaIzSvihDucana) {
            ukupniVolumen += artikl.getHeight().doubleValue() * artikl.getLength().doubleValue() * artikl.getWidth().
                    doubleValue();
        }
        Double averagevolume = ukupniVolumen / brojArtikala;
        BigDecimal srednjaCijenaSvihArtikala = listaSvihArtikalaIzSvihDucana.stream()
                .filter(artikl -> artikl.getHeight().doubleValue() * artikl.getLength().doubleValue() * artikl.getWidth().
                        doubleValue() > averagevolume)
                .map(artikl -> artikl.getSellingPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long srednjaCijenaBrojArtikala = listaSvihArtikalaIzSvihDucana.stream()
                .filter(artikl -> artikl.getHeight().doubleValue() * artikl.getLength().doubleValue() * artikl.getWidth().
                        doubleValue() > averagevolume)
                .count();
        Double finalnaCijena = srednjaCijenaSvihArtikala.doubleValue() / srednjaCijenaBrojArtikala;
        System.out.println("Srednja cijena svih artikala sa natprosječnim volumenom: " + finalnaCijena);

        int ukupanBrojArtikalaPoDucanu = 0;
        for (Store ducan : stores.get()) {
            ukupanBrojArtikalaPoDucanu += (ducan.getItems().size());
        }
        double prosjecnaKolicinaArtikala = ukupanBrojArtikalaPoDucanu / stores.get().size();
        List<Store> trgovineSNatprosjecnimBrojemArtikala = stores.get().stream()
                .filter(trgovina -> trgovina.getItems().size() > prosjecnaKolicinaArtikala)
                .collect(Collectors.toList());
        System.out.println("Trgovine s natprosječnim brojem artikala:");
        trgovineSNatprosjecnimBrojemArtikala.stream().forEach(t -> System.out.println(t.getName()));
    }
    public static Set<Item> insertNewSet(String categoryName, List<Item> listaArtikala) {
        return listaArtikala.stream()
                .filter(artikl -> artikl.getCategory().getName().equalsIgnoreCase(categoryName))
                .collect(Collectors.toSet());
    }

    public static List<Edible> insertNewFoodList(List<Item> listaArtikala) {
        return listaArtikala.stream()
                .filter(artikl -> artikl.getCategory().getName().equalsIgnoreCase("Edible"))
                .map(artikl -> (Edible) artikl)
                .collect(Collectors.toList());
    }

    public static List<Technical> insertNewTechnicalList(List<Item> listaArtikala) {
        return listaArtikala.stream()
                .filter(artikl -> artikl.getCategory().getName().equalsIgnoreCase("Technical"))
                .map(artikl -> (Technical) artikl)
                .collect(Collectors.toList());
    }

    /**
     * Sluzi za ispis Item-a s najkracim garancijskim rokom.
     * @param items niz objekata klase Item
     */
    /*public static void shortestWarranty(List<Item> items) {
        Integer minWarranty = 0;
        Item itemMinWarranty = items.get(0);
        for (Item item : items) {
            boolean isLaptop = item instanceof Laptop;
            if (isLaptop) {
                minWarranty = ((Laptop) item).calculateWarranty();
                itemMinWarranty = ((Laptop) item);
            }
        }
        for (Item item : items) {
            boolean isLaptop = item instanceof Laptop;
            if (isLaptop) {
                if (((Laptop) item).calculateWarranty() < minWarranty) {
                    minWarranty = ((Laptop) item).calculateWarranty();
                    itemMinWarranty = ((Laptop) item);
                }
            }
        }
        boolean isLaptop = itemMinWarranty instanceof Laptop;
        if (isLaptop) {
            System.out.println("Informacije o laptopu koji sadrži najkraći garantni rok:\n" +
                    itemMinWarranty.toString());
        }
    }*/

    /**
     * Sluzi za ispis informacija o unesenim artiklima koji pirpadaju kategoriji Edible te za ispis
     * artikla sa najvecim brojem kalorija i najvecom ukupnom cijenom
     * @param items niz objekata klase Item
     */
    /*public static void pumpkinCalculatorDeluxe(List<Item> items) {
        Integer maxKcal = 0;
        BigDecimal maxPrice = new BigDecimal("0");
        Item itemMaxTotalKcal = items.get(0);
        Item itemMaxTotalPrice = items.get(0);

        for (Item item : items) {
            boolean isHokkaido = item instanceof Hokkaido;
            boolean isButternut = item instanceof Butternut;
            Integer testKcal = 0;
            BigDecimal testPrice = new BigDecimal("0");
            if (isHokkaido) {
                System.out.print("Informacije za artikl: " + item.getName() + "     ");
                System.out.print(((Hokkaido) item).calculateKilocalories() + " kcal/kg     ");
                System.out.println(item.getSellingPrice() + " kn/kg");
                testPrice = ((Hokkaido) item).calculatePrice();
                System.out.println("Ukupna cijena za " + ((Hokkaido) item).getWeight() + " kg proizvoda: " +
                        testPrice + " kn");
                ((Hokkaido) item).setSellingPrice(testPrice);
                testKcal = (((Hokkaido) item).getWeight().intValue() * ((Hokkaido) item).calculateKilocalories());
                System.out.println("Ukupnan broj kilokalorija za " + ((Hokkaido) item).getWeight() + " kg proizvoda: "
                        + testKcal + " kcal");
            } else if (isButternut) {
                System.out.print("Informacije za artikl: " + item.getName() + "     ");
                System.out.print(((Butternut) item).calculateKilocalories() + " kcal/kg     ");
                System.out.println(item.getSellingPrice() + " kn/kg");
                testPrice = ((Butternut) item).calculatePrice();
                System.out.println("Ukupna cijena za " + ((Butternut) item).getWeight() + " kg proizvoda: " +
                        testPrice + " kn");
                ((Butternut) item).setSellingPrice(testPrice);
                testKcal = (((Butternut) item).getWeight().intValue() * ((Butternut) item).calculateKilocalories());
                System.out.println("Ukupnan broj kilokalorija za " + ((Butternut) item).getWeight() + " kg proizvoda: "
                        + testKcal + " kcal");
            }
            if (testKcal > maxKcal) {
                maxKcal = testKcal;
                if (isHokkaido) {
                    itemMaxTotalKcal = ((Hokkaido) item);
                } else if (isButternut) {
                    itemMaxTotalKcal = ((Butternut) item);
                }
            }
            Integer compare = testPrice.compareTo(maxPrice);
            if (compare == 1) {
                maxPrice = testPrice;
                if (isHokkaido) {
                    itemMaxTotalPrice = ((Hokkaido) item);
                } else if (isButternut) {
                    itemMaxTotalPrice = ((Butternut) item);
                }
            }
        }
        boolean isPumpkin = itemMaxTotalKcal.getCategory().getName().equalsIgnoreCase(Edible.class.getSimpleName());
        if (isPumpkin) {
            System.out.println("Namirnica sa najvećim ukupnim brojem kilokalorija (kcal * kg): " +
                    itemMaxTotalKcal.toString());
            System.out.println("Namirnica sa najvećim ukupnom cijenom ((kn/kg) * kg): " + itemMaxTotalPrice.toString());
        }
    }*/

    /**
     * Sluzi za ispis naziva ducana koji prodaje najjeftiniji artikl
     * @param stores prethodno uneseni niz ducana
     */

    /*public static void storeItemMinSellingPrice(Store[] stores) {

        Integer indexMinSellingPrice = 0;

        BigDecimal minSellingPrice = new BigDecimal("0");

        Integer compare = 2;
        Integer loop = 0;
        Integer multipleLocations = 0;
        int j = 0;

        do {
            for (int i = 0; i < NUMBER_OF_STORES; i++) {
                Set<Item> items = stores[i].getItems();

                for (Item item : items) {
                    if(j == 0) {
                        minSellingPrice = item.getSellingPrice();
                        j++;
                    }
                    compare = item.getSellingPrice().compareTo(minSellingPrice);

                    if (compare == -1 && loop == 0) {
                        minSellingPrice = item.getSellingPrice();
                    }

                    if (compare == 0 && loop != 0) {
                        if (multipleLocations != 0) {
                            System.out.println("Najjeftiniji artikl se također prodaje u dućanu naziva: " +
                                    stores[i].getName());
                        }
                        if (multipleLocations == 0) {
                            System.out.println("Naziv dućana koji prodaje najjeftiniji artikl: " +
                                    stores[i].getName());
                            multipleLocations += 1;
                        }
                    }
                }

            }
            loop += 1;
        }
        while (loop < 2);

    }*/

    /**
     * Sluzi za ispis naziva tvornice koja proizvodi artikl sa najvecim volumenom
     * @param factories prethodno uneseni niz tvornica
     */

    /*public static void factoryItemMaxVolume(Factory[] factories) {

        BigDecimal maxVolume = new BigDecimal("0");
        Integer loop = 0;
        Integer multipleLocations = 0;
        do {
            for (int i = 0; i < NUMBER_OF_FACTORIES; i++) {
                Set<Item> items = factories[i].getItems();

                for (Item item : items) {

                    BigDecimal area = item.getWidth().multiply(item.getLength());
                    BigDecimal volume = area.multiply(item.getHeight());
                    Integer compare = volume.compareTo(maxVolume);

                    if (compare == 1 && loop == 0) {
                        maxVolume = volume;
                    }
                    if (compare == 0 && loop != 0) {
                        if (multipleLocations != 0) {
                            System.out.println("Artikl sa najvećim volumenom se također proizvodi u tvornici naziva: " +
                                    factories[i].getName());
                        }
                        if (multipleLocations == 0) {
                            System.out.println("Naziv tvornice koja proizvodi artikl koji ima najveći volumen: " +
                                    factories[i].getName());
                            multipleLocations += 1;
                        }
                    }

                }
            }
            loop += 1;
        }
        while (loop < 2);

    }*/

    /**
     * Sluzi za odabir kategorije kojoj artikl pripada prilikom stvaranja novog objekta tipa Item
     * @param input Scanner koji sluzi za ucitavanje korisnikova izbora kategorije artikla kojeg stvara
     * @param categories prethodno uneseni niz kategorija
     * @return nova instanca objekta tipa Category
     */

    /*public static Category categoryPicker(Scanner input, Category[] categories) {
        categoryList(categories);

        System.out.print(">> ");
        Integer pickCategory = -1;

        Boolean repeatInput = false;

        do {
            repeatInput = false;
            try {
                pickCategory = input.nextInt();
                if (pickCategory > NUMBER_OF_CATEGORIES || pickCategory < 1) {
                    logger.error("Neispravan unos, molimo unesite postojecu kategoriju! " + pickCategory);
                    repeatInput = true;
                    input.nextLine();
                    System.out.print("Krivi unos! Molimo izaberite postojeću kategoriju. >> ");
                }
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + pickCategory);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >> ");
            }
        }
        while (repeatInput);

        Category chosenCategory = categories[pickCategory - 1];

        return chosenCategory;
    }*/

    /**
     * Sluzi za ispis prethodno unesenog niza kategorija
     * @param categories prethodno uneseni niz kategorija
     */
    /*public static void categoryList(Category[] categories) {
        for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            System.out.println((i + 1) + ") " + categories[i].getName());
        }
    }*/

    /**
     * Sluzi za stvaranje novog niza artikala na nacin da se izabere iz postojeceg niza artikala
     * @param input Scanner koji sluzi za ucitavanje korisnikova zeljenog izbora artikala
     * @param items lista objekata klase Item
     * @param numberOfArticles broj razlicith artikala koje korisnik zeli izabrati
     * @return nova lista artikala sastavljena od artikala koje je korisnik izabrao
     * @throws ArticleSelectorException baca se u slucaju visestukog unosa istog artikla
     */
    /*public static List<Item> itemPicker(Scanner input, List<Item> items, int numberOfArticles) throws ArticleSelectorException {
        itemList(items);

        List<Item> chosenItems = new ArrayList<>();

        Integer[] articleChecker = new Integer[numberOfArticles];

        for (int i = 0; i < numberOfArticles; i++) {
            System.out.print(">> ");
            Integer pickItem = 0;

            Boolean repeatInput = false;

            do {
                repeatInput = false;
                try {
                    pickItem = input.nextInt();
                    logger.info("Unos broja uz artikl: " + pickItem);
                    if (pickItem > NUMBER_OF_ITEMS || pickItem < 1) {
                        logger.error("Neispravan unos artikla, molimo unesite jedan od ponudenih brojeva! "
                                + pickItem);
                        System.out.print("Krivi unos! Molimo izaberite postojeci artikl. >> ");
                        repeatInput = true;
                    }

                    for (int j = 0; j < i; j++) {
                        if (articleChecker[j] == pickItem && pickItem != 0) {
                            Integer[] articleDuplicateFound = new Integer[i+1];
                            for(int k = 0; k < i; k++) {
                                articleDuplicateFound[k] = articleChecker[k];
                            }
                            articleDuplicateFound[i] = pickItem;
                            throw new ArticleSelectorException("Krivi unos! Molimo izaberite RAZLICITE artikle. "
                                    + Arrays.toString(articleDuplicateFound));
                        }
                    }
                } catch (InputMismatchException ex) {
                    logger.error("Neispravan unos, molimo unesite broj! " + pickItem);
                    repeatInput = true;
                    input.nextLine();
                    System.out.print("Neispravan unos, molimo unesite broj! >> ");
                }
            }
            while (repeatInput);

            articleChecker[i] = pickItem;
            chosenItems.add(items.get(pickItem - 1));
        }
        return chosenItems;
    }*/

    /**
     * Sluzi za ispis postojece liste artikala
     * @param items postojeca lista objekata klase Item
     */
    /*public static void itemList(List<Item> items) {
        int i = 0;
        for (Item item : items) {
            System.out.println((i + 1) + ") " + item.getName());
            i += 1;
        }
    }*/

    /**
     * Sluzi za stvaranje novog objekta tipa Address
     * @param input Scanner koji sluzi za ucitavanje informacija o adresi tvornice koju korisnik stvara
     * @return nova instanca objekta tipa Address
     */
    /*public static Address insertAddress(Scanner input) {

        System.out.print("Molimo unesite naziv ulice: ");
        String street = input.nextLine();

        System.out.print("Molimo unesite kućni broj: ");
        String houseNumber = input.nextLine();
        String cityAddress = "Grad";
        String postalCode = "Postanski broj";
        System.out.println("Molimo izaberite grad unošenjem broja uz željeni grad: ");
        AvailableCities[] cities = AvailableCities.values();
        int i = 1;
        for(AvailableCities city : cities){
            System.out.print(i + ") ");
            System.out.println(city);
            i += 1;
        }
        Boolean repeatInput = false;
        int cityChoice = 0;
        do {
            repeatInput = false;
            try {
                cityChoice = input.nextInt();
                logger.info("Uneseni broj uz zeljeni grad: "+ cityChoice);
                if (cityChoice > 5 || cityChoice < 1) {
                    logger.error("Neispravan unos! Molimo unesite broj između 1 i 5. "
                            + cityChoice);
                    System.out.print("Neispravan unos! Molimo unesite broj između 1 i 5. >> ");
                    repeatInput = true;
                }
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + cityChoice);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >> ");
            }
        }
        while (repeatInput);

        switch(cityChoice) {
            case 1:
                return new Address.Builder()
                        .atCityWithPostalCode(AvailableCities.ZAGREB)
                        .atStreet(street)
                        .atHouseNumber(houseNumber)
                        .build();
            case 2:
                return new Address.Builder()
                        .atCityWithPostalCode(AvailableCities.SPLIT)
                        .atStreet(street)
                        .atHouseNumber(houseNumber)
                        .build();
            case 3:
                return new Address.Builder()
                        .atCityWithPostalCode(AvailableCities.DUBROVNIK)
                        .atStreet(street)
                        .atHouseNumber(houseNumber)
                        .build();
            case 4:
                return new Address.Builder()
                        .atCityWithPostalCode(AvailableCities.ZADAR)
                        .atStreet(street)
                        .atHouseNumber(houseNumber)
                        .build();
            case 5:
                return new Address.Builder()
                        .atCityWithPostalCode(AvailableCities.SIBENIK)
                        .atStreet(street)
                        .atHouseNumber(houseNumber)
                        .build();
        }
        input.nextLine();
        return new Address.Builder()
                .atCityWithPostalCode(AvailableCities.ZAGREB)
                .atStreet(street)
                .atHouseNumber(houseNumber)
                .build();
    }*/

    /**
     * Sluzi za stvaranje novog objekta tipa Store
     * @param input Scanner koji sluzi za ucitavanje informacija o ducanu kojeg korisnik stvara
     * @param i redni broj ducana o kojem korisnik unosi informacije
     * @param items postojeca lista objekata klase Item
     * @return nova instanca objekta tipa Store
     */
    /*public static Store insertNewStore(Scanner input, int i, List<Item> items) {

        System.out.print("Molimo unesite naziv " + (i + 1) + ". dućana: ");
        String storeName = input.nextLine();

        System.out.print("Molimo unesite web adresu " + (i + 1) + ". dućana: ");
        String webAddress = input.nextLine();

        System.out.print("Unesite broj razlicitih artikala koji se prodaju u ovom dućanu: ");

        Integer numberOfArticles = -1;
        Boolean repeatInput = false;
        do {
            repeatInput = false;
            try {
                numberOfArticles = input.nextInt();
                logger.info("Uneseni broj razlicitih artikala: "+ numberOfArticles);
                if (numberOfArticles > NUMBER_OF_ITEMS || numberOfArticles < 1) {
                    logger.error("Neispravan unos! Molimo unesite broj između 1 i " + NUMBER_OF_ITEMS + ". "
                            + numberOfArticles);
                    System.out.print("Neispravan unos! Molimo unesite broj između 1 i " + NUMBER_OF_ITEMS + ". >> ");
                    repeatInput = true;
                }
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + numberOfArticles);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >> ");
            }
        }
        while (repeatInput);


        System.out.println("Izaberite artikle koji se prodaju u ovom dućanu tako da unesete broj uz naziv artikla.");

        List<Item> chosenItems = new ArrayList<>();
        do {
            repeatInput = false;
            try {
                chosenItems = itemPicker(input, items, numberOfArticles);
            } catch (ArticleSelectorException ex) {
                logger.error(ex.getMessage());
                repeatInput = true;
                input.nextLine();
                System.out.println(ex.getMessage());
            }
        }
        while (repeatInput);

        input.nextLine();

        Set<Item> chosenItemsSet = chosenItems.stream().collect(Collectors.toSet());

        return new Store(storeName, webAddress, chosenItemsSet);

    }*/

    /**
     * Sluzi za stvaranje novog objekta tipa Factory
     * @param input Scanner koji sluzi za ucitavanje informacija o tvornici koju korisnik stvara
     * @param i redni broj tvornice o kojoj korisnik unosi informacije
     * @param items postojeca lista objekata klase Item
     * @return nova instanca objekta tipa Factory
     */
/*    public static Factory insertNewFactory(Scanner input, int i, List<Item> items) {

        input.nextLine();

        System.out.print("Molimo unesite naziv " + (i + 1) + ". tvornice: ");
        String factoryName = input.nextLine();

        System.out.println("Molimo unesite informacije vezane za adresu " + (i + 1) + ". tvornice. ");

        Address address = insertAddress(input);

        System.out.print("Unesite broj razlicitih artikala koji se proizvode u ovoj tvornici: ");

        Integer numberOfArticles = -1;
        Boolean repeatInput = false;

        do {
            repeatInput = false;
            try {
                numberOfArticles = input.nextInt();
                logger.info("Uneseni broj razlicitih artikala: " + numberOfArticles);
                if (numberOfArticles < 1 || numberOfArticles > NUMBER_OF_ITEMS) {
                    logger.error("Broj različitih artikala je neispravan! " + numberOfArticles);
                    System.out.print("Broj različitih artikala je neispravan. Molimo unesite broj od 1 do " +
                            NUMBER_OF_ITEMS + ". >> ");
                    repeatInput = true;
                }
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + numberOfArticles);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >> ");
            }
        }
        while (repeatInput);

        System.out.println("Izaberite artikle koji se proizvode u ovoj tvornici tako da unesete broj uz naziv artikla.");
        List<Item> chosenItems = new ArrayList<>();
        do {
            repeatInput = false;
            try {
                chosenItems = itemPicker(input, items, numberOfArticles);
            } catch (ArticleSelectorException ex) {
                logger.error(ex.getMessage());
                repeatInput = true;
                input.nextLine();
                System.out.println(ex.getMessage());
            }
        }
        while (repeatInput);

        Set<Item> chosenItemsSet = chosenItems.stream().collect(Collectors.toSet());

        return new Factory(factoryName, address, chosenItemsSet);
    }*/

    /**
     * Sluzi za stvaranje novog objekta tipa Item
     * @param input Scanner koji sluzi za ucitavanje informacija o artiklu kojeg korisnik stvara
     * @param i redni broj artikla o kojem korisnik unosi informacije
     * @param categories postojeci niz obejkata tipa Category
     * @return nova instanca objekta tipa Item
     */
    /*public static Item insertNewItem(Scanner input, int i, Category[] categories) {


        System.out.println("Molimo odaberite kojoj kategoriji " + (i + 1) + ". artikl pripada tako da unesete broj " +
                "uz naziv kategorije. ");

        Category category = categoryPicker(input, categories);


        String itemName = "Name";
        Integer pumpkinPicker = 0;
        BigDecimal weight = new BigDecimal("0");
        Integer warranty = 0;

        if (category.getName().equalsIgnoreCase(Edible.class.getSimpleName())) {
            System.out.println("U ponudi imamo:");
            System.out.println("1) " + Hokkaido.class.getSimpleName());
            System.out.println("2) " + Butternut.class.getSimpleName());
            System.out.print("Upišite broj uz želejnu vrstu tikve: >> ");


            Boolean repeatInput = false;

            do {
                repeatInput = false;
                try {
                    pumpkinPicker = input.nextInt();
                    logger.info("Unos broja uz zeljenu vrstu tikve: " + pumpkinPicker);
                    if(pumpkinPicker < 1 || pumpkinPicker > 2){
                        repeatInput = true;
                        logger.error("Neispravan unos, molimo unesite jedan od ponudenih brojeva! "
                                + pumpkinPicker);
                        System.out.print("Neispravan unos, molimo unesite jedan od ponudenih brojeva! >> ");
                    }
                } catch (InputMismatchException ex) {
                    logger.error("Neispravan unos, molimo unesite broj! " + pumpkinPicker);
                    repeatInput = true;
                    input.nextLine();
                    System.out.print("Neispravan unos, molimo unesite broj! >> ");
                }
            }
            while (repeatInput);
            repeatInput = false;

            System.out.print("Molimo unesite masu tikve u kilogramima. >> ");

            do {
                repeatInput = false;
                try {
                    weight = input.nextBigDecimal();
                    logger.info("Unesena masa: " + weight);
                } catch (InputMismatchException ex) {
                    logger.error("Neispravan unos, molimo unesite broj! " + weight);
                    repeatInput = true;
                    input.nextLine();
                    System.out.print("Neispravan unos, molimo unesite broj! >> ");
                }
            }
            while (repeatInput);
            repeatInput = false;


        } else if (category.getName().equalsIgnoreCase(Technical.class.getSimpleName())) {
            System.out.print("Molimo unesite broj mjeseci trajanja garantnog roka laptopa: ");

            Boolean repeatInput = false;
            do {
                repeatInput = false;
                try {
                    warranty = input.nextInt();
                    logger.info("Uneseno trajanje garancije: " + warranty);
                } catch (InputMismatchException ex) {
                    logger.error("Neispravan unos, molimo unesite broj! " + warranty);
                    repeatInput = true;
                    input.nextLine();
                    System.out.print("Neispravan unos, molimo unesite broj! >> ");
                }
            }
            while (repeatInput);

            pumpkinPicker = 3;
        } else {
            input.nextLine();
            System.out.print("Molimo unesite naziv " + (i + 1) + ". artikla: ");
            itemName = input.nextLine();
        }
        System.out.print("Molimo unesite širinu " + (i + 1) + ". artikla: ");

        BigDecimal width = new BigDecimal(0);

        Boolean repeatInput = false;
        do {
            repeatInput = false;
            try {
                width = input.nextBigDecimal();
                logger.info("Unesena sirina: " + width);
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + width);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >> ");
            }
        }
        while (repeatInput);
        repeatInput = false;

        System.out.print("Molimo unesite visinu " + (i + 1) + ". artikla: ");
        BigDecimal height = new BigDecimal(0);


        do {
            repeatInput = false;
            try {
                height = input.nextBigDecimal();
                logger.info("Unesena visina: " + height);
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + height);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >> ");
            }
        }
        while (repeatInput);
        repeatInput = false;

        System.out.print("Molimo unesite duljinu " + (i + 1) + ". artikla: ");
        BigDecimal length = new BigDecimal(0);


        do {
            repeatInput = false;
            try {
                length = input.nextBigDecimal();
                logger.info("Unesena duljina: " + length);
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + length);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >> ");
            }
        }
        while (repeatInput);
        repeatInput = false;

        System.out.print("Molimo unesite cijenu proizvodnje " + (i + 1) + ". artikla: ");
        BigDecimal productionCost = new BigDecimal(0);


        do {
            repeatInput = false;
            try {
                productionCost = input.nextBigDecimal();
                logger.info("Unesena cijena proizvodnje: " + productionCost);
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + productionCost);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >>");
            }
        }
        while (repeatInput);
        repeatInput = false;

        System.out.print("Molimo unesite prodajnu cijenu " + (i + 1) + ". artikla: ");
        BigDecimal sellingPrice = new BigDecimal(0);


        do {
            repeatInput = false;
            try {
                sellingPrice = input.nextBigDecimal();
                logger.info("Unos prodajne cijene: " + sellingPrice);
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + sellingPrice);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >>");
            }
        }
        while (repeatInput);
        repeatInput = false;

        System.out.print("Molimo unesite iznos popusta na " + (i + 1) + ". artikl u postocima: ");
        BigDecimal discountAmount = new BigDecimal(0);


        do {
            repeatInput = false;
            try {
                discountAmount = input.nextBigDecimal();
                logger.info("Uneseni popust: " + discountAmount);
            } catch (InputMismatchException ex) {
                logger.error("Neispravan unos, molimo unesite broj! " + discountAmount);
                repeatInput = true;
                input.nextLine();
                System.out.print("Neispravan unos, molimo unesite broj! >>");
            }
        }
        while (repeatInput);
        repeatInput = false;

        Discount discount = new Discount(discountAmount);

        BigDecimal hundred = new BigDecimal("100");
        BigDecimal priceWithDiscount = hundred.subtract(discountAmount);
        priceWithDiscount = priceWithDiscount.divide(hundred);
        priceWithDiscount = priceWithDiscount.multiply(sellingPrice);

        if (pumpkinPicker == 1) {
            return new Hokkaido(width, height, length, productionCost, priceWithDiscount, weight, discount);
        } else if (pumpkinPicker == 2) {
            return new Butternut(width, height, length, productionCost, priceWithDiscount, weight, discount);
        } else if (pumpkinPicker == 3) {
            return new Laptop(width, height, length, productionCost, priceWithDiscount, discount, warranty);
        } else {
            return new Item(itemName, category, width, height, length, productionCost, priceWithDiscount, discount);
        }
    }/*

   /**
     * Sluzi za provjeru ispravnosti unesenog niza kategorija
     * @param categories uneseni niz objekata tipa Category
     * @throws CategoryCreatorException baca se u slucaju otkrivanja istoimenih kategorija
     */

    /*public static void NewCategoryDuplicateName(Category[] categories) throws CategoryCreatorException {
        for (int i = 0; i < (NUMBER_OF_CATEGORIES - 1); i++) {
            for(int j = (i + 1); j < NUMBER_OF_CATEGORIES; j++) {
                if (categories[i].getName().equalsIgnoreCase(categories[j].getName())) {
                    String[] duplicateCategoriesFound = new String[NUMBER_OF_CATEGORIES];
                    for(int k = 0; k < NUMBER_OF_CATEGORIES; k++) {
                        duplicateCategoriesFound[k] = categories[k].getName();
                    }
                    throw new CategoryCreatorException("Neispravan unos! Naziv kategorije MORA biti UNIKATAN!"
                            + Arrays.toString(duplicateCategoriesFound));
                }
            }
        }
    }*/

    /**
     * Sluzi za stvaranje novog objekta tipa Category
     * @param input Scanner sluzi za unos informacija o kategoriji koju korisnik zeli stvoriti
     * @param i redni broj kategorije koju korinik stvara
     * @return nova instanca objekta tipa Category
     */
    /*public static Category insertNewCategory(Scanner input, int i) {
        System.out.print("Molimo unesite naziv " + (i + 1) + ". kategorije: ");
        String name = input.nextLine();
        System.out.print("Molimo unesite opis " + (i + 1) + ". kategorije: ");
        String description = input.nextLine();
        return new Category(name, description);
    }*/
}