package hr.java.production.model;

import java.math.BigDecimal;

/**
 * navodi metode klasa koje implementiraju sucelje Edible
 */
public interface Edible {
    /**
     * sluzi za izracun broja kilokalorija artikla
     * @return vraca cjelobrojnu vrijednost kilokalorija artikla
     */
    Integer calculateKilocalories();
    /**
     * sluzi za izracun ukupne cijene artikla
     * @return vraca BigDecimal vrijednost ukupne cijene artikla
     */
    BigDecimal calculatePrice();
}
