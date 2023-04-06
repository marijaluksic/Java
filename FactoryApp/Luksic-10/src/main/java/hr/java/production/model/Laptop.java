package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *  sluzi za stvaranje novog laptopa
 */
public final class Laptop extends Item implements Technical, Serializable {
    /**
     * sprema trajanje garancijskog roka laptopa u mjesecima
     */
    private Integer warranty;

    /**
     * konstruktor koji se poziva prilikom stvaranja ovjekta tipa Laptop
     * @param id unos id laptopa
     * @param width unos sirine laptopa
     * @param height unos visine laptopa
     * @param length unos duljine laptopa
     * @param productionCost unos cijene proizvodnje laptopa
     * @param sellingPrice unos prodajne cijene laptopa
     * @param discount unos popusta na laptop
     * @param warranty unos trajanja garancijekog roka laptopa
     */
    public Laptop(Long id, String name, BigDecimal width, BigDecimal height, BigDecimal length,
                  BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, Integer warranty) {
        super(id, name ,new Category(2l, Technical.class.getSimpleName(), hr.java.production.model.Laptop.class.getSimpleName()),
                width, height, length, productionCost, sellingPrice, discount);
        this.warranty = warranty;
    }

    public Integer getWarranty() {
        return warranty;
    }

    public void setWarranty(Integer warranty) {
        this.warranty = warranty;
    }

    /**
     * sluzi za izracun trajanja roka garancije
     * @return vraca cjelobrojnu vrijednost trajanja roka garancije laptopa
     */
    @Override
    public Integer calculateWarranty() {
        return warranty;
    }
    /**
     * sluzi za formatiranje ispisa laptopa
     * @return vraca formatirani ispis laptopa
     */
    @Override
    public String toString() {
        return "ID: " + this.getId() + "\nNaziv: " + this.getName() + "\nNaziv kategorije: " + this.getCategory().getName() +
                "\nOpis kategorije: " + this.getCategory().getDescription() + "\nŠirina: " + this.getWidth() +
                " cm\nVisina " + this.getHeight() + " cm\nDuljina: " + this.getLength() + " cm\nCijena proizvodnje: " +
                this.getProductionCost() + " kn\nProdajna cijena: " + this.getSellingPrice() + " kn\nPopust: " +
                this.getDiscount().discountAmount() + " %\nGarantni rok u mjesecima: " + this.warranty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        hr.java.production.model.Laptop laptop = (hr.java.production.model.Laptop) o;
        return Objects.equals(warranty, laptop.warranty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), warranty);
    }
}
