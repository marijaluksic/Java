package hr.java.production.enumeracija;

/**
 * moguci gradovi za selekciju prilikom unosa adrese tvornice
 */
public enum AvailableCities {
    /**
     * konstanta za grad Zagreb i njegov postanski broj
     */
    ZAGREB("ZAGREB", "10000"),
    /**
     * konstanta za grad Split i njegov postanski broj
     */
    SPLIT("SPLIT", "21000"),
    /**
     * konstanta za grad Dubrovnik i njegov postanski broj
     */
    DUBROVNIK("DUBROVNIK", "20000"),
    /**
     * konstanta za grad Zadar i njegov postanski broj
     */
    ZADAR("ZADAR","23000"),
    /**
     * konstanta za grad Sibenik i njegov postanski broj
     */
    SIBENIK("SIBENIK", "22000");
    /**
     * sprema postanski broj
     */
    private String postalCode;
    /**
     * sprema naziov grada
     */
    private String name;

    /**
     * privatni konstruktor enum klase za odabir grada i odgovarajuceg postanskog broja prilikom unosa adrese
     * @param name sprema naziv grada
     * @param postalCode sprema postanski broj
     */
    private AvailableCities(String name, String postalCode) {
        this.name = name;
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getName() {
        return name;
    }
}
