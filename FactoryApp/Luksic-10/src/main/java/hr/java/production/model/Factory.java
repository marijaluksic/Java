package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * sluzi za stvaranje nove tvornice
 */
public class Factory extends NamedEntity implements Serializable {
    /**
 * varijabla u koju se sprema id
 */
private Long id;
    /**
     * sprema naziv tvornice
     */
    private String name;
    /**
     * sprema adresu tvornice
     */
    private Address address;
    /**
     * sprema niz artikala
     */
    private Set<Item> items;

    /**
     * konstruktor objekta klase Factory
     * @param id unos id tvornice
     * @param name unos naziva tvornice
     * @param address unos adrese tvornice
     * @param items unos niza artikala koje tvornica proizvodi
     */
    public Factory(Long id, String name, Address address, Set<Item> items) {
        super(id, name);
        this.address = address;
        this.items = items;
    }

    public Factory(Long id, String name, Address address) {
        super(id, name);
        this.address = address;
    }

    public Factory(String name, Address address, Set<Item> items) {
        super(name);
        this.address = address;
        this.items = items;
    }

    public Factory(String name) {
        super(name);
    }

    public Factory(Long id) {
        super(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        hr.java.production.model.Factory factory = (hr.java.production.model.Factory) o;
        return Objects.equals(id, factory.id) && Objects.equals(name, factory.name) && Objects.equals(address, factory.address) && Objects.equals(items, factory.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, address, items);
    }

    @Override
    public String toString() {
        return "id tvornice: " + this.getId() +
                "\tNaziv tvornice: " + this.getName() + "\n" + address + "\nProizvodi koji se proizvede u tvornici: " +
                items;
    }
}
