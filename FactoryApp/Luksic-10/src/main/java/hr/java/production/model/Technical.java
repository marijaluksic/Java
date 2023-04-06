package hr.java.production.model;

/**
 * zapecaceno sucelje koje navodi metodu koju implementira iskljucivo klasa Laptop
 */
public sealed interface Technical permits Laptop {
    /**
     * sluzi za izracun duljine trajanja roka garancije u mjesecima
     * @return vraca cjelobrojnu vrijednost duljine trajanja roka garancije u mjesecima
     */
    Integer calculateWarranty();
}
