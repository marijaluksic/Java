package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * sluzi za stvaranje novog ducana
 */
public class Store extends NamedEntity implements Serializable {

    /**
     * varijabla u koju se sprema id
     */
    private Long id;
    /**
     * sprema naziv ducana
     */
    private String name;
    /**
     * sprema web adresu ducana
     */
    private String webAddress;
    /**
     * sprema niz artikala koji se prodaju u ducanu
     */
    private Set<Item> items;

    /**
     * konstruktor koji se poziva prilikom stvaranmja novog ducana
     * @param id unos id ducana
     * @param name unos naziva ducana
     * @param webAddress  unos web adrese ducana
     * @param items unos niza artikala koji se prodaju u ducanu
     */
    public Store(Long id, String name,String webAddress, Set<Item> items) {
        super(id, name);
        this.webAddress = webAddress;
        this.items = items;
    }

    public Store(Long id, String name, String webAddress) {
        super(id, name);
        this.webAddress = webAddress;
    }

    public Store(String name, String webAddress, Set<Item> items) {
        super(name);
        this.webAddress = webAddress;
        this.items = items;
    }

    public Store(String name) {
        super(name);
    }

    public Store(Long id) {
        super(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
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
        hr.java.production.model.Store store = (hr.java.production.model.Store) o;
        return Objects.equals(id, store.id) && Objects.equals(name, store.name) && Objects.equals(webAddress, store.webAddress) && Objects.equals(items, store.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, webAddress, items);
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", webAddress='" + webAddress + '\'' +
                ", items=" + items +
                '}';
    }
}
