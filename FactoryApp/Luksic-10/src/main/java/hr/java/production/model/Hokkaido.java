package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * sluzi za stvaranje objekta tipa Hokkaido koja nasljeduje klasu Item i implementira sucelje Edible
 */
public class Hokkaido extends Item implements Edible, Serializable {
    /**
     * sprema masu artikla
     */
    private BigDecimal weight;
    /**
     * iznos kilokalorija po kilogramu artikla
     */
    private static final int KILOCALORIES_PER_KG = 260;
    /**
     * sluzi za stvaranje objekta tipa Hokkaido
     * @param id unos id artikla
     * @param width varijabla koja oznacava sirinu artikla
     * @param height varijabla koja oznacava visinu artikla
     * @param length varijabla koja oznacava duljinu artikla
     * @param productionCost varijabla koja oznacava cijenu proizvodnje artikla
     * @param sellingPrice varijabla koja oznacava prodajnu cijenu artikla
     * @param weight varijabla koja oznacava masu artikla
     * @param discount varijabla koja oznacava iznos popusta na artikl
     */
    public Hokkaido(Long id, String name, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost,
                    BigDecimal sellingPrice, BigDecimal weight, Discount discount) {
        super(id, name, new Category(1l, Edible.class.getSimpleName(),
                        "Category of edible items"), width, height,
                length, productionCost, sellingPrice, discount);
        this.weight = weight;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
    /**
     * sluzi za izracun broja kilokalorija artikla
     * @return vraca cjelobrojnu vrijednost kilokalorija artikla
     */
    @Override
    public Integer calculateKilocalories() {
        return KILOCALORIES_PER_KG;
    }
    /**
     * sluzi za izracun ukupne cijene artikla
     * @return vraca BigDecimal vrijednost ukupne cijene artikla
     */
    @Override
    public BigDecimal calculatePrice() {
        return (this.weight.multiply(this.getSellingPrice()));
    }
    /**
     * sluzi za formatiranje ispisa artikla
     * @return vraca formatirani ispis artikla
     */
    @Override
    public String toString() {
        return "ID: " + this.getId() +  "\n" +  this.getName() + "\nNaziv kategorije: " + this.getCategory().getName() +
                "\nOpis kategorije: " + this.getCategory().getDescription() + "\nŠirina: " + this.getWidth() +
                " cm\nVisina " + this.getHeight() + " cm\nDuljina: " + this.getLength() + " cm\nCijena proizvodnje: " +
                this.getProductionCost() + " kn\nProdajna cijena: " + this.getSellingPrice() + " kn\nMasa: " +
                this.getWeight() +  " kg\nEnergijska vrijednost: " + KILOCALORIES_PER_KG + " kcal/kg\nPopust: " +
                this.getDiscount().discountAmount() + " %";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        hr.java.production.model.Hokkaido hokkaido = (hr.java.production.model.Hokkaido) o;
        return Objects.equals(weight, hokkaido.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weight);
    }
}
