package hr.java.production.genericsi;

import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.model.Technical;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class TechnicalStore <T extends Technical> extends Store implements Serializable {
    private List<T> technicalItems;

    public TechnicalStore(Long id, String name, String webAddress, Set<Item> items, List<T> technicalItems) {
        super(id, name, webAddress, items);
        this.technicalItems = technicalItems;
    }

    public List<T> getTechnicalItems() {
        return technicalItems;
    }
}
