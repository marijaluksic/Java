package hr.java.production.genericsi;

import hr.java.production.model.Edible;
import hr.java.production.model.Item;
import hr.java.production.model.Store;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class FoodStore <T extends Edible> extends Store implements Serializable {
    private List<T> edibleItems;

    public FoodStore(Long id, String name, String webAddress, Set<Item> items, List<T> edibleItems) {
        super(id, name, webAddress, items);
        this.edibleItems = edibleItems;
    }

    public List<T> getEdibleItems() {
        return edibleItems;
    }
}