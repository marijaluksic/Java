package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * sluzi za stvaranje novog artikla
 */
public class Item extends NamedEntity implements Serializable {
    /**
 * varijabla u koju se sprema id
 */
private Long id;
    /**
     * sprema ime artikla
     */
    private String name;
    /**
     * sprema kategoriju artikla
     */
    private Category category;
    /**
     * sprema sirinu artikla
     */
    private BigDecimal width;
    /**
     * sprema visinu artikla
     */
    private BigDecimal height;
    /**
     * sprema duljinu artikla
     */
    private BigDecimal length;
    /**
     * sprema cijenu proizvodnje artikla
     */
    private BigDecimal productionCost;
    /**
     * sprema prodajnu cijenu artikla
     */
    private BigDecimal sellingPrice;
    /**
     * sprema iznos popusta na artikl
     */
    private Discount discount;

    /**
     * konstruktor koji se poziva prilikom stvaranja ovjekta tipa Item
     * @param id unos id artikla
     * @param name unos naziva artikla
     * @param category unos kategorije artikla
     * @param width unos sirine artikla
     * @param height unos visine artikla
     * @param length unos duljine artikla
     * @param productionCost unos cijene proizvodnje artikla
     * @param sellingPrice unos prodajne cijene artikla
     * @param discount unos popusta na artikl
     */
    public Item(Long id, String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                BigDecimal productionCost, BigDecimal sellingPrice, Discount discount) {
        super(id, name);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
    }
    public Item(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                BigDecimal productionCost, BigDecimal sellingPrice, Discount discount) {
        super(name);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
    }
    public Item(String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                BigDecimal productionCost, BigDecimal sellingPrice) {
        super(name);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
    }

    public Item(Long id) {
        super(id);
    }

    public Item(String name, BigDecimal sellingPrice) {
        super(name);
        this.sellingPrice = sellingPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(BigDecimal productionCost) {
        this.productionCost = productionCost;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Discount getDiscount() {
        return discount;
    }

    /**
     * sluzi za formatiranje ispisa artikla
     * @return vraca formatirani ispis artikla
     */
    /*public String toString() {
        return "ID: " + this.getId() + "\nNaziv: " + this.getName() + "\nNaziv kategorije: " + this.getCategory().getName() +
                "\nOpis kategorije: " + this.getCategory().getDescription() + "\nŠirina: " + this.getWidth() +
                " cm\nVisina " + this.getHeight() + " cm\nDuljina: " + this.getLength() + " cm\nCijena proizvodnje: " +
                this.getProductionCost() + " kn\nProdajna cijena: " + this.getSellingPrice() + " kn\nPopust: " +
                this.getDiscount().discountAmount() + " %";
    }*/

    public String toString() {
        return "ID: " + this.getId() + "\nNaziv: " + this.getName() + "\nNaziv kategorije: " + this.getCategory().getName() +
                "\nOpis kategorije: " + this.getCategory().getDescription() + "\nŠirina: " + this.getWidth() +
                " cm\nVisina " + this.getHeight() + " cm\nDuljina: " + this.getLength() + " cm\nCijena proizvodnje: " +
                this.getProductionCost() + " kn\nProdajna cijena: " + this.getSellingPrice() + " kn";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        hr.java.production.model.Item item = (hr.java.production.model.Item) o;
        return Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(category, item.category) && Objects.equals(width, item.width) && Objects.equals(height, item.height) && Objects.equals(length, item.length) && Objects.equals(productionCost, item.productionCost) && Objects.equals(sellingPrice, item.sellingPrice) && Objects.equals(discount, item.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, category, width, height, length, productionCost, sellingPrice, discount);
    }
}
